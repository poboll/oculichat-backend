package com.caiths.oculichatback.job;

import com.caiths.oculichatback.model.entity.ProductOrder;
import com.caiths.oculichatback.service.OrderService;
import com.caiths.oculichatback.service.ProductOrderService;
import com.caiths.oculichatback.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.caiths.oculichatback.model.enums.PayTypeStatusEnum.ALIPAY;
import static com.caiths.oculichatback.model.enums.PayTypeStatusEnum.WX;

/**
 * 支付任务调度类，负责定时处理未支付订单和清理过期订单。
 * <p>
 * 该类使用 {@code @Component} 注解，表示它是一个 Spring 组件。
 * 通过 {@code @Scheduled} 注解配置多个定时任务，分别处理微信和支付宝的未支付订单确认，
 * 以及定期清理过期的未支付且已关闭的订单。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Slf4j
@Component
public class PayJob {

    /**
     * 产品订单服务，用于操作产品订单相关的数据。
     */
    @Resource
    private ProductOrderService productOrderService;

    /**
     * 订单服务，用于获取未支付订单和相关操作。
     */
    @Resource
    private OrderService orderService;

    /**
     * Redisson 分布式锁工具，用于确保定时任务在分布式环境中的互斥执行。
     */
    @Resource
    private RedissonLockUtil redissonLockUtil;

    /**
     * 微信订单确认任务。
     * <p>
     * 该任务每25秒执行一次，查询超过5分钟且未支付的微信订单，并进行确认处理。
     * 使用分布式锁确保在分布式环境中任务的互斥执行。
     * </p>
     *
     * <p>计划表达式: {@code cron = "0/25 * * * * ?"}，表示每25秒执行一次。</p>
     */
    @Scheduled(cron = "0/25 * * * * ?")
    public void wxOrderConfirm() {
        redissonLockUtil.redissonDistributedLocks("wxOrderConfirm", () -> {
            // 获取超过5分钟且未支付的微信订单列表
            List<ProductOrder> orderList = orderService.getNoPayOrderByDuration(5, false, WX.getValue());
            // 获取对应支付类型的产品订单服务
            ProductOrderService productOrderService = orderService.getProductOrderServiceByPayType(WX.getValue());

            for (ProductOrder productOrder : orderList) {
                String orderNo = productOrder.getOrderNo();
                try {
                    // 处理超时订单
                    productOrderService.processingTimedOutOrders(productOrder);
                } catch (Exception e) {
                    log.error("微信超时订单, 订单号: {}, 确认异常: {}", orderNo, e.getMessage());
                    // 根据业务需求决定是否继续处理下一个订单，这里选择中断循环
                    break;
                }
            }
        });
    }

    /**
     * 支付宝订单确认任务。
     * <p>
     * 该任务每20秒执行一次，查询超过5分钟且未支付的支付宝订单，并进行确认处理。
     * 使用分布式锁确保在分布式环境中任务的互斥执行。
     * </p>
     *
     * <p>计划表达式: {@code cron = "0/20 * * * * ?"}，表示每20秒执行一次。</p>
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void aliPayOrderConfirm() {
        redissonLockUtil.redissonDistributedLocks("aliPayOrderConfirm", () -> {
            // 获取超过5分钟且未支付的支付宝订单列表
            List<ProductOrder> orderList = orderService.getNoPayOrderByDuration(5, false, ALIPAY.getValue());
            // 获取对应支付类型的产品订单服务
            ProductOrderService productOrderService = orderService.getProductOrderServiceByPayType(ALIPAY.getValue());

            for (ProductOrder productOrder : orderList) {
                String orderNo = productOrder.getOrderNo();
                try {
                    // 处理超时订单
                    productOrderService.processingTimedOutOrders(productOrder);
                } catch (Exception e) {
                    log.error("支付宝超时订单, 订单号: {}, 确认异常: {}", orderNo, e.getMessage());
                    // 根据业务需求决定是否继续处理下一个订单，这里选择中断循环
                    break;
                }
            }
        });
    }

    /**
     * 过期订单清理任务。
     * <p>
     * 该任务每天凌晨2点执行一次，删除15天前的未支付且已关闭的订单。
     * 使用分布式锁确保在分布式环境中任务的互斥执行。
     * </p>
     *
     * <p>计划表达式: {@code cron = "0 0 2 * * ?"}，表示每天凌晨2点执行一次。</p>
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearOverdueOrders() {
        redissonLockUtil.redissonDistributedLocks("clearOverdueOrders", () -> {
            // 获取15天前的未支付且已关闭的订单列表
            List<ProductOrder> orderList = orderService.getNoPayOrderByDuration(15 * 24 * 60, true, "");
            // 批量删除订单
            boolean removeResult = productOrderService.removeBatchByIds(orderList);
            if (removeResult) {
                log.info("成功清除15天前的过期未支付且已关闭的订单");
            } else {
                log.warn("清除15天前的过期未支付且已关闭的订单时发生问题");
            }
        });
    }
}
