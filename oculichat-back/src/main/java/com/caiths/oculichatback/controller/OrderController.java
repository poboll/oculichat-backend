package com.caiths.oculichatback.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caiths.oculichatback.common.BaseResponse;
import com.caiths.oculichatback.common.ErrorCode;
import com.caiths.oculichatback.common.ResultUtils;
import com.caiths.oculichatback.exception.BusinessException;
import com.caiths.oculichatback.model.dto.pay.PayCreateRequest;
import com.caiths.oculichatback.model.dto.productorder.ProductOrderQueryRequest;
import com.caiths.oculichatback.model.entity.ProductInfo;
import com.caiths.oculichatback.model.entity.ProductOrder;
import com.caiths.oculichatback.model.enums.PaymentStatusEnum;
import com.caiths.oculichatback.model.vo.OrderVo;
import com.caiths.oculichatback.model.vo.ProductOrderVo;
import com.caiths.oculichatback.model.vo.UserVO;
import com.caiths.oculichatback.service.OrderService;
import com.caiths.oculichatback.service.ProductOrderService;
import com.caiths.oculichatback.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.caiths.oculichatback.constant.PayConstant.QUERY_ORDER_STATUS;
import static com.caiths.oculichatback.model.enums.PaymentStatusEnum.SUCCESS;

/**
 * 订单控制器，处理与产品订单相关的请求。
 * <p>
 * 提供创建订单、取消订单、删除订单、查询订单状态以及分页获取订单列表等功能。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Resource
    private UserService userService;

    @Resource
    private ProductOrderService productOrderService;

    @Resource
    private OrderService orderService;

    @Resource
    private RedisTemplate<String, Boolean> redisTemplate;

    // region 增删改查

    /**
     * 取消订单接口。
     * <p>
     * 根据订单号取消对应的订单，并更新订单状态为已关闭。
     * </p>
     *
     * @param orderNo 要取消的订单号
     * @return {@link BaseResponse} 包含操作结果的响应对象
     *         成功时返回 {@code true}，表示订单已成功取消
     * @throws BusinessException 当订单号为空、订单不存在或取消失败时抛出业务异常
     */
    @PostMapping("/closed")
    public BaseResponse<Boolean> closedProductOrder(@RequestParam("orderNo") String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单号不能为空");
        }
        // 判断订单是否存在
        ProductOrder productOrder = productOrderService.getProductOrderByOutTradeNo(orderNo);
        if (productOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 根据支付类型获取相应的订单服务
        ProductOrderService orderServiceByPayType = orderService.getProductOrderServiceByPayType(productOrder.getPayType());
        // 更新订单状态为已关闭
        boolean closedResult = orderServiceByPayType.updateOrderStatusByOrderNo(orderNo, PaymentStatusEnum.CLOSED.getValue());
        if (!closedResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单取消失败");
        }
        return ResultUtils.success(closedResult);
    }

    /**
     * 删除订单接口。
     * <p>
     * 根据订单 ID 删除对应的订单。只有订单的所有者或管理员有权限删除订单。
     * </p>
     *
     * @param id      要删除的订单 ID
     * @param request 当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse} 包含操作结果的响应对象
     *         成功时返回 {@code true}，表示订单已成功删除
     * @throws BusinessException 当订单 ID 为空、订单不存在或无权限删除时抛出业务异常
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteProductOrder(@RequestParam("id") String id, HttpServletRequest request) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单 ID 不能为空");
        }
        UserVO loginUser = userService.getLoginUser(request);
        // 校验订单是否存在
        ProductOrder productOrder = productOrderService.getById(id);
        if (productOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 仅本人或管理员可删除
        if (!productOrder.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该订单");
        }
        boolean deleteResult = productOrderService.removeById(id);
        if (!deleteResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单删除失败");
        }
        return ResultUtils.success(deleteResult);
    }

    /**
     * 按 ID 获取产品订单信息。
     *
     * @param id 要查询的订单 ID
     * @return {@link BaseResponse} 包含订单信息的响应对象
     *         成功时返回 {@link ProductOrderVo}，包含订单的详细信息
     * @throws BusinessException 当订单 ID 为空或订单不存在时抛出业务异常
     */
    @GetMapping("/get")
    public BaseResponse<ProductOrderVo> getProductOrderById(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单 ID 不能为空");
        }
        ProductOrder productOrder = productOrderService.getById(id);
        if (productOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        ProductOrderVo productOrderVo = formatProductOrderVo(productOrder);
        return ResultUtils.success(productOrderVo);
    }

    /**
     * 分页获取订单列表。
     * <p>
     * 根据查询条件分页获取当前用户的订单列表。如果用户不是管理员，仅返回已支付的订单。
     * </p>
     *
     * @param productOrderQueryRequest 订单查询请求，包含分页和筛选条件
     * @param request                   当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse} 包含分页订单信息的响应对象
     *         成功时返回 {@link OrderVo}，包含订单的分页数据
     * @throws BusinessException 当查询请求为空或参数不合法时抛出业务异常
     */
    @GetMapping("/list/page")
    public BaseResponse<OrderVo> listProductOrderByPage(ProductOrderQueryRequest productOrderQueryRequest, HttpServletRequest request) {
        if (productOrderQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询请求不能为空");
        }
        // 参数复制
        ProductOrder productOrder = new ProductOrder();
        BeanUtils.copyProperties(productOrderQueryRequest, productOrder);
        long size = productOrderQueryRequest.getPageSize();
        String orderName = productOrderQueryRequest.getOrderName();
        String orderNo = productOrderQueryRequest.getOrderNo();
        Integer total = productOrderQueryRequest.getTotal();
        String status = productOrderQueryRequest.getStatus();
        String productInfo = productOrderQueryRequest.getProductInfo();
        String payType = productOrderQueryRequest.getPayType();
        Integer addPoints = productOrderQueryRequest.getAddPoints();
        long current = productOrderQueryRequest.getCurrent();

        // 限制查询数量，防止爬虫攻击
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询数量过大");
        }
        UserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        QueryWrapper<ProductOrder> queryWrapper = new QueryWrapper<>();

        // 构建查询条件
        queryWrapper.like(StringUtils.isNotBlank(orderName), "orderName", orderName)
                .like(StringUtils.isNotBlank(productInfo), "productInfo", productInfo)
                .eq("userId", userId)
                .eq(StringUtils.isNotBlank(orderNo), "orderNo", orderNo)
                .eq(StringUtils.isNotBlank(status), "status", status)
                .eq(StringUtils.isNotBlank(payType), "payType", payType)
                .eq(ObjectUtils.isNotEmpty(addPoints), "addPoints", addPoints)
                .eq(ObjectUtils.isNotEmpty(total), "total", total);
        // 未支付的订单优先显示
        queryWrapper.last("ORDER BY CASE WHEN status = 'NOTPAY' THEN 0 ELSE 1 END, status");
        Page<ProductOrder> productOrderPage = productOrderService.page(new Page<>(current, size), queryWrapper);
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(productOrderPage, orderVo);
        // 处理订单信息
        List<ProductOrderVo> productOrders = productOrderPage.getRecords().stream()
                .map(this::formatProductOrderVo)
                .collect(Collectors.toList());
        orderVo.setRecords(productOrders);
        return ResultUtils.success(orderVo);
    }
    // endregion

    /**
     * 创建订单接口。
     * <p>
     * 根据支付创建请求创建新的产品订单。
     * </p>
     *
     * @param payCreateRequest 创建订单的请求参数，包含产品 ID 和支付类型
     * @param request          当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse} 包含订单信息的响应对象
     *         成功时返回 {@link ProductOrderVo}，包含新创建订单的详细信息
     * @throws BusinessException 当请求参数不合法或订单创建失败时抛出业务异常
     */
    @PostMapping("/create")
    public BaseResponse<ProductOrderVo> createOrder(@RequestBody PayCreateRequest payCreateRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(payCreateRequest) || StringUtils.isBlank(payCreateRequest.getProductId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不完整");
        }
        Long productId;
        try {
            productId = Long.valueOf(payCreateRequest.getProductId());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "产品 ID 格式不正确");
        }
        String payType = payCreateRequest.getPayType();
        if (StringUtils.isBlank(payType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "支付方式不能为空");
        }
        UserVO loginUser = userService.getLoginUser(request);
        ProductOrderVo productOrderVo = orderService.createOrderByPayType(productId, payType, loginUser);
        if (productOrderVo == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单创建失败，请稍后再试");
        }
        return ResultUtils.success(productOrderVo);
    }

    /**
     * 查询订单状态接口。
     * <p>
     * 根据订单号查询订单的支付状态。
     * </p>
     *
     * @param productOrderQueryRequest 查询订单状态的请求，包含订单号
     * @return {@link BaseResponse} 包含订单状态的响应对象
     *         成功时返回 {@code true} 表示订单已支付，{@code false} 表示订单未支付
     * @throws BusinessException 当请求参数不合法或订单查询失败时抛出业务异常
     */
    @PostMapping("/query/status")
    public BaseResponse<Boolean> queryOrderStatus(@RequestBody ProductOrderQueryRequest productOrderQueryRequest) {
        if (ObjectUtils.isEmpty(productOrderQueryRequest) || StringUtils.isBlank(productOrderQueryRequest.getOrderNo())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单号不能为空");
        }
        String orderNo = productOrderQueryRequest.getOrderNo();
        Boolean cachedStatus = redisTemplate.opsForValue().get(QUERY_ORDER_STATUS + orderNo);
        if (Boolean.FALSE.equals(cachedStatus)) {
            return ResultUtils.success(cachedStatus);
        }
        ProductOrder productOrder = productOrderService.getProductOrderByOutTradeNo(orderNo);
        if (productOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        if (SUCCESS.getValue().equals(productOrder.getStatus())) {
            return ResultUtils.success(true);
        }
        // 将查询结果缓存到 Redis，避免频繁查询数据库
        redisTemplate.opsForValue().set(QUERY_ORDER_STATUS + orderNo, false, 5, TimeUnit.MINUTES);
        return ResultUtils.success(false);
    }

    /**
     * 解析订单通知结果接口。
     * <p>
     * 处理第三方支付平台的订单通知，并根据通知数据更新订单状态。
     * 通知频率为 15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h，总计 24h4m。
     * </p>
     *
     * @param notifyData 通知数据，通常为第三方支付平台发送的 JSON 数据
     * @param request    当前的 HTTP 请求
     * @return {@link String} 返回处理结果的响应内容
     * @throws BusinessException 当通知数据处理失败时抛出业务异常
     */
    @PostMapping("/notify/order")
    public String parseOrderNotifyResult(@RequestBody String notifyData, HttpServletRequest request) {
        return orderService.doOrderNotify(notifyData, request);
    }

    /**
     * 格式化产品订单信息为视图对象。
     *
     * @param productOrder 原始产品订单实体
     * @return {@link ProductOrderVo} 格式化后的订单视图对象
     */
    private ProductOrderVo formatProductOrderVo(ProductOrder productOrder) {
        ProductOrderVo productOrderVo = new ProductOrderVo();
        BeanUtils.copyProperties(productOrder, productOrderVo);
        ProductInfo prodInfo = JSONUtil.toBean(productOrder.getProductInfo(), ProductInfo.class);
        productOrderVo.setDescription(prodInfo.getDescription());
        productOrderVo.setProductType(prodInfo.getProductType());
        String voTotal = String.valueOf(prodInfo.getTotal());
        BigDecimal total = new BigDecimal(voTotal).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        productOrderVo.setTotal(total.toString());
        return productOrderVo;
    }
}
