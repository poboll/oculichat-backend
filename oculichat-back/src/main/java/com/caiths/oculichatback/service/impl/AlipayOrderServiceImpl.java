package com.caiths.oculichatback.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.caiths.oculichatback.common.ErrorCode;
import com.caiths.oculichatback.config.AliPayAccountConfig;
import com.caiths.oculichatback.config.EmailConfig;
import com.caiths.oculichatback.exception.BusinessException;
import com.caiths.oculichatback.mapper.ProductOrderMapper;
import com.caiths.oculichatback.model.alipay.AliPayAsyncResponse;
import com.caiths.oculichatback.model.entity.ProductInfo;
import com.caiths.oculichatback.model.entity.ProductOrder;
import com.caiths.oculichatback.model.entity.RechargeActivity;
import com.caiths.oculichatback.model.entity.User;
import com.caiths.oculichatback.model.enums.AlipayTradeStatusEnum;
import com.caiths.oculichatback.model.enums.PaymentStatusEnum;
import com.caiths.oculichatback.model.vo.PaymentInfoVo;
import com.caiths.oculichatback.model.vo.ProductOrderVo;
import com.caiths.oculichatback.model.vo.UserVO;
import com.caiths.oculichatback.service.PaymentInfoService;
import com.caiths.oculichatback.service.ProductOrderService;
import com.caiths.oculichatback.service.RechargeActivityService;
import com.caiths.oculichatback.service.UserService;
import com.caiths.oculichatback.utils.EmailUtil;
import com.caiths.oculichatback.utils.RedissonLockUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

import static com.caiths.oculichatback.constant.PayConstant.*;
import static com.caiths.oculichatback.model.enums.PayTypeStatusEnum.ALIPAY;
import static com.caiths.oculichatback.model.enums.PaymentStatusEnum.*;


/**
 * AlipayOrderServiceImpl 是支付宝订单服务的实现类。
 * <p>
 * 该类负责处理与支付宝支付相关的订单创建、查询、关闭等操作，
 * 并管理订单状态的更新和支付记录的保存。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Service
@Slf4j
@Qualifier("ALIPAY")
public class AlipayOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService {
    @Resource
    private EmailConfig emailConfig;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private AliPayAccountConfig aliPayAccountConfig;
    @Resource
    private UserService userService;
    @Resource
    private ProductInfoServiceImpl productInfoService;

    @Resource
    private PaymentInfoService paymentInfoService;
    @Resource
    private RedissonLockUtil redissonLockUtil;
    @Resource
    private RechargeActivityService rechargeActivityService;

    /**
     * 获取产品订单信息。
     *
     * @param productId 产品ID
     * @param loginUser 登录用户信息
     * @param payType   支付类型
     * @return {@link ProductOrderVo} 产品订单视图对象
     */
    @Override
    public ProductOrderVo getProductOrder(Long productId, UserVO loginUser, String payType) {
        LambdaQueryWrapper<ProductOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProductOrder::getProductId, productId);
        lambdaQueryWrapper.eq(ProductOrder::getStatus, PaymentStatusEnum.NOTPAY.getValue());
        lambdaQueryWrapper.eq(ProductOrder::getPayType, payType);
        lambdaQueryWrapper.eq(ProductOrder::getUserId, loginUser.getId());
        ProductOrder oldOrder = this.getOne(lambdaQueryWrapper);
        if (oldOrder == null) {
            return null;
        }
        ProductOrderVo productOrderVo = new ProductOrderVo();
        BeanUtils.copyProperties(oldOrder, productOrderVo);
        productOrderVo.setProductInfo(JSONUtil.toBean(oldOrder.getProductInfo(), ProductInfo.class));
        productOrderVo.setTotal(oldOrder.getTotal().toString());
        return productOrderVo;
    }

    /**
     * 保存新的产品订单。
     *
     * @param productId 产品ID
     * @param loginUser 登录用户信息
     * @return {@link ProductOrderVo} 产品订单视图对象
     */
    @Override
    public ProductOrderVo saveProductOrder(Long productId, UserVO loginUser) {
        ProductInfo productInfo = productInfoService.getById(productId);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        // 5分钟有效期
        Date date = DateUtil.date(System.currentTimeMillis());
        Date expirationTime = DateUtil.offset(date, DateField.MINUTE, 5);
        String orderNo = ORDER_PREFIX + RandomUtil.randomNumbers(20);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setUserId(loginUser.getId());
        productOrder.setOrderNo(orderNo);
        productOrder.setProductId(productInfo.getId());
        productOrder.setOrderName(productInfo.getName());
        productOrder.setTotal(productInfo.getTotal());
        productOrder.setStatus(PaymentStatusEnum.NOTPAY.getValue());
        productOrder.setPayType(ALIPAY.getValue());
        productOrder.setExpirationTime(expirationTime);
        productOrder.setProductInfo(JSONUtil.toJsonPrettyStr(productInfo));
        productOrder.setAddPoints(productInfo.getAddPoints());

        boolean saveResult = this.save(productOrder);

        // 构建支付宝支付请求
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderNo);
        model.setSubject(productInfo.getName());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        // 金额四舍五入
        BigDecimal scaledAmount = new BigDecimal(productInfo.getTotal()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        model.setTotalAmount(String.valueOf(scaledAmount));
        model.setBody(productInfo.getDescription());

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(aliPayAccountConfig.getNotifyUrl());
        request.setReturnUrl(aliPayAccountConfig.getReturnUrl());

        try {
            AlipayTradePagePayResponse alipayTradePagePayResponse = AliPayApi.pageExecute(request);
            String payUrl = alipayTradePagePayResponse.getBody();
            productOrder.setFormData(payUrl);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        boolean updateResult = this.updateProductOrder(productOrder);
        if (!updateResult & !saveResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 构建视图对象vo
        ProductOrderVo productOrderVo = new ProductOrderVo();
        BeanUtils.copyProperties(productOrder, productOrderVo);
        productOrderVo.setProductInfo(productInfo);
        productOrderVo.setTotal(productInfo.getTotal().toString());
        return productOrderVo;
    }

    /**
     * 更新产品订单信息。
     *
     * @param productOrder 产品订单实体
     * @return boolean 是否更新成功
     */
    @Override
    public boolean updateProductOrder(ProductOrder productOrder) {
        String formData = productOrder.getFormData();
        Long id = productOrder.getId();
        ProductOrder updateCodeUrl = new ProductOrder();
        updateCodeUrl.setFormData(formData);
        updateCodeUrl.setId(id);
        return this.updateById(updateCodeUrl);
    }

    /**
     * 按订单号更新订单状态。
     *
     * @param outTradeNo  外部交易号
     * @param orderStatus 订单状态
     * @return boolean 是否更新成功
     */
    @Override
    public boolean updateOrderStatusByOrderNo(String outTradeNo, String orderStatus) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setStatus(orderStatus);
        LambdaQueryWrapper<ProductOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProductOrder::getOrderNo, outTradeNo);
        return this.update(productOrder, lambdaQueryWrapper);
    }

    /**
     * 关闭订单。
     *
     * @param outTradeNo 外部交易号
     * @throws AlipayApiException 支付宝API异常
     */
    @Override
    public void closedOrderByOrderNo(String outTradeNo) throws AlipayApiException {
        AlipayTradeCloseModel alipayTradeCloseModel = new AlipayTradeCloseModel();
        alipayTradeCloseModel.setOutTradeNo(outTradeNo);
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(alipayTradeCloseModel);
        AliPayApi.doExecute(request);
    }

    /**
     * 通过外部交易号获取产品订单。
     *
     * @param outTradeNo 外部交易号
     * @return {@link ProductOrder} 产品订单实体
     */
    @Override
    public ProductOrder getProductOrderByOutTradeNo(String outTradeNo) {
        LambdaQueryWrapper<ProductOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProductOrder::getOrderNo, outTradeNo);
        return this.getOne(lambdaQueryWrapper);
    }

    /**
     * 处理超时订单。
     *
     * @param productOrder 产品订单实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processingTimedOutOrders(ProductOrder productOrder) {
        String orderNo = productOrder.getOrderNo();
        try {
            // 查询订单
            AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
            alipayTradeQueryModel.setOutTradeNo(orderNo);
            AlipayTradeQueryResponse alipayTradeQueryResponse = AliPayApi.tradeQueryToResponse(alipayTradeQueryModel);

            // 本地创建了订单,但是用户没有扫码,支付宝端没有订单
            if (!alipayTradeQueryResponse.getCode().equals(RESPONSE_CODE_SUCCESS)) {
                // 更新本地订单状态
                this.updateOrderStatusByOrderNo(orderNo, PaymentStatusEnum.CLOSED.getValue());
                log.info("超时订单{},更新成功", orderNo);
                return;
            }
            String tradeStatus = AlipayTradeStatusEnum.findByName(alipayTradeQueryResponse.getTradeStatus()).getPaymentStatusEnum().getValue();
            // 订单没有支付就关闭订单,更新本地订单状态
            if (tradeStatus.equals(NOTPAY.getValue()) || tradeStatus.equals(CLOSED.getValue())) {
                closedOrderByOrderNo(orderNo);
                this.updateOrderStatusByOrderNo(orderNo, PaymentStatusEnum.CLOSED.getValue());
                log.info("超时订单{},关闭成功", orderNo);
                return;
            }
            if (tradeStatus.equals(SUCCESS.getValue())) {
                // 订单已支付更新商户端的订单状态
                boolean updateOrderStatus = this.updateOrderStatusByOrderNo(orderNo, SUCCESS.getValue());
                // 补发积分到用户钱包
                boolean addWalletBalance = userService.addWalletBalance(productOrder.getUserId(), productOrder.getAddPoints());
                // 保存支付记录
                PaymentInfoVo paymentInfoVo = new PaymentInfoVo();
                paymentInfoVo.setAppid(aliPayAccountConfig.getAppId());
                paymentInfoVo.setOutTradeNo(alipayTradeQueryResponse.getOutTradeNo());
                paymentInfoVo.setTransactionId(alipayTradeQueryResponse.getTradeNo());
                paymentInfoVo.setTradeType("电脑网站支付");
                paymentInfoVo.setTradeState(alipayTradeQueryResponse.getTradeStatus());
                paymentInfoVo.setTradeStateDesc("支付成功");
                paymentInfoVo.setSuccessTime(String.valueOf(alipayTradeQueryResponse.getSendPayDate()));
                WxPayOrderQueryV3Result.Payer payer = new WxPayOrderQueryV3Result.Payer();
                payer.setOpenid(alipayTradeQueryResponse.getBuyerOpenId());
                paymentInfoVo.setPayer(payer);
                WxPayOrderQueryV3Result.Amount amount = new WxPayOrderQueryV3Result.Amount();
                amount.setTotal(new BigDecimal(alipayTradeQueryResponse.getTotalAmount()).multiply(new BigDecimal("100")).intValue());
                amount.setPayerTotal(new BigDecimal(alipayTradeQueryResponse.getReceiptAmount()).multiply(new BigDecimal("100")).intValue());
                amount.setCurrency(alipayTradeQueryResponse.getPayCurrency());
                amount.setPayerCurrency(alipayTradeQueryResponse.getPayCurrency());
                paymentInfoVo.setAmount(amount);
                boolean paymentResult = paymentInfoService.createPaymentInfo(paymentInfoVo);
                if (!updateOrderStatus & !addWalletBalance & !paymentResult) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }
                // 更新活动表
                saveRechargeActivity(productOrder);
                // 发送成功邮件
                sendSuccessEmail(productOrder, alipayTradeQueryResponse.getTotalAmount());
                log.info("超时订单{},更新成功", orderNo);
            }
        } catch (AlipayApiException e) {
            log.error("订单{} 处理失败", orderNo);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 发送支付成功电子邮件。
     *
     * @param productOrder 产品订单实体
     * @param orderTotal   订单总额
     */
    private void sendSuccessEmail(ProductOrder productOrder, String orderTotal) {
        // 发送邮件
        User user = userService.getById(productOrder.getUserId());
        if (StringUtils.isNotBlank(user.getEmail())) {
            try {
                ProductOrder productOrderByOutTradeNo = this.getProductOrderByOutTradeNo(productOrder.getOrderNo());
                new EmailUtil().sendPaySuccessEmail(user.getEmail(), mailSender, emailConfig, productOrderByOutTradeNo.getOrderName(),
                        String.valueOf(orderTotal));
                log.info("发送邮件：{}，成功", user.getEmail());
            } catch (Exception e) {
                log.error("发送邮件：{}，失败：{}", user.getEmail(), e.getMessage());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String doPaymentNotify(String notifyData, HttpServletRequest request) {
        Map<String, String> params = AliPayApi.toMap(request);
        AliPayAsyncResponse aliPayAsyncResponse = JSONUtil.toBean(JSONUtil.toJsonStr(params), AliPayAsyncResponse.class);
        String lockName = "notify:AlipayOrder:lock:" + aliPayAsyncResponse.getOutTradeNo();
        return redissonLockUtil.redissonDistributedLocks(lockName, "【支付宝异步回调异常】:", () -> {
            String result;
            try {
                result = checkAlipayOrder(aliPayAsyncResponse, params);
            } catch (AlipayApiException e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
            }
            if (!"success".equals(result)) {
                return result;
            }
            String doAliPayOrderBusinessResult = this.doAliPayOrderBusiness(aliPayAsyncResponse);
            if (StringUtils.isBlank(doAliPayOrderBusinessResult)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            return doAliPayOrderBusinessResult;
        });
    }

    private String checkAlipayOrder(AliPayAsyncResponse response, Map<String, String> params) throws AlipayApiException {
        String result = "failure";
        boolean verifyResult = AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(),
                AliPayApiConfigKit.getAliPayApiConfig().getCharset(),
                AliPayApiConfigKit.getAliPayApiConfig().getSignType());
        if (!verifyResult) {
            return result;
        }
        // 1.验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
        ProductOrder productOrder = this.getProductOrderByOutTradeNo(response.getOutTradeNo());
        if (productOrder == null) {
            log.error("订单不存在");
            return result;
        }
        // 2.判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）。
        int totalAmount = new BigDecimal(response.getTotalAmount()).multiply(new BigDecimal("100")).intValue();
        if (totalAmount != productOrder.getTotal()) {
            log.error("订单金额不一致");
            return result;
        }
        // 3.校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商家可能有多个 seller_id/seller_email）。
        String sellerId = aliPayAccountConfig.getSellerId();
        if (!response.getSellerId().equals(sellerId)) {
            log.error("卖家账号校验失败");
            return result;
        }
        // 4.验证 app_id 是否为该商家本身。
        String appId = aliPayAccountConfig.getAppId();
        if (!response.getAppId().equals(appId)) {
            log.error("校验失败");
            return result;
        }
        // 状态 TRADE_SUCCESS 的通知触发条件是商家开通的产品支持退款功能的前提下，买家付款成功。
        String tradeStatus = response.getTradeStatus();
        if (!tradeStatus.equals(TRADE_SUCCESS)) {
            log.error("交易失败");
            return result;
        }
        return "success";
    }

    @SneakyThrows
    protected String doAliPayOrderBusiness(AliPayAsyncResponse response) {
        String outTradeNo = response.getOutTradeNo();
        ProductOrder productOrder = this.getProductOrderByOutTradeNo(outTradeNo);
        // 处理重复通知
        if (SUCCESS.getValue().equals(productOrder.getStatus())) {
            return "success";
        }
        // 业务代码
        // 更新订单状态
        boolean updateOrderStatus = this.updateOrderStatusByOrderNo(outTradeNo, SUCCESS.getValue());
        // 更新用户积分
        boolean addWalletBalance = userService.addWalletBalance(productOrder.getUserId(), productOrder.getAddPoints());
        // 保存支付记录
        PaymentInfoVo paymentInfoVo = new PaymentInfoVo();
        paymentInfoVo.setAppid(response.getAppId());
        paymentInfoVo.setOutTradeNo(response.getOutTradeNo());
        paymentInfoVo.setTransactionId(response.getTradeNo());
        paymentInfoVo.setTradeType("电脑网站支付");
        paymentInfoVo.setTradeState(response.getTradeStatus());
        paymentInfoVo.setTradeStateDesc("支付成功");
        paymentInfoVo.setSuccessTime(response.getNotifyTime());
        WxPayOrderQueryV3Result.Payer payer = new WxPayOrderQueryV3Result.Payer();
        payer.setOpenid(response.getBuyerId());
        paymentInfoVo.setPayer(payer);
        WxPayOrderQueryV3Result.Amount amount = new WxPayOrderQueryV3Result.Amount();
        amount.setTotal(new BigDecimal(response.getTotalAmount()).multiply(new BigDecimal("100")).intValue());
        amount.setPayerTotal(new BigDecimal(response.getReceiptAmount()).multiply(new BigDecimal("100")).intValue());
        amount.setCurrency("CNY");
        amount.setPayerCurrency("CNY");
        paymentInfoVo.setAmount(amount);
        boolean paymentResult = paymentInfoService.createPaymentInfo(paymentInfoVo);
        // 更新活动表
        boolean rechargeActivity = saveRechargeActivity(productOrder);
        if (paymentResult && updateOrderStatus && addWalletBalance && rechargeActivity) {
            log.info("【支付回调通知处理成功】");
            // 发送邮件
            sendSuccessEmail(productOrder, response.getTotalAmount());
            return "success";
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR);
    }


    /**
     * 保存充值活动记录。
     *
     * @param productOrder 产品订单实体
     * @return boolean 是否保存成功
     */
    private boolean saveRechargeActivity(ProductOrder productOrder) {
        RechargeActivity rechargeActivity = new RechargeActivity();
        rechargeActivity.setUserId(productOrder.getUserId());
        rechargeActivity.setProductId(productOrder.getProductId());
        rechargeActivity.setOrderNo(productOrder.getOrderNo());
        boolean save = rechargeActivityService.save(rechargeActivity);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存失败");
        }
        return true;
    }
}




