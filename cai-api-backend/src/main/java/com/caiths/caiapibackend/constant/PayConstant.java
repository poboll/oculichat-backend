package com.caiths.caiapibackend.constant;

/**
 * 支付相关常量接口，定义了应用程序中支付功能所需的全局常量。
 * <p>
 * 该接口包含了查询订单状态、订单前缀、响应代码及交易状态等相关常量，用于统一管理和引用。
 * </p>
 *
 * <p><strong>主要常量：</strong></p>
 * <ul>
 *   <li>{@link #QUERY_ORDER_STATUS} - 查询订单状态的缓存键前缀。</li>
 *   <li>{@link #ORDER_PREFIX} - 订单编号的前缀。</li>
 *   <li>{@link #QUERY_ORDER_INFO} - 查询订单信息的缓存键前缀。</li>
 *   <li>{@link #RESPONSE_CODE_SUCCESS} - 支付宝响应代码表示成功。</li>
 *   <li>{@link #TRADE_SUCCESS} - 交易成功状态。</li>
 * </ul>
 *
 * <p><strong>示例使用：</strong></p>
 * <pre>
 * // 使用常量构建查询订单状态的缓存键
 * String cacheKey = PayConstant.QUERY_ORDER_STATUS + orderId;
 *
 * // 检查支付宝响应代码是否表示成功
 * if (responseCode.equals(PayConstant.RESPONSE_CODE_SUCCESS)) {
 *     // 处理成功逻辑
 * }
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface PayConstant {

    /**
     * 查询订单状态的缓存键前缀。
     * <p>
     * 用于在缓存中存储和检索订单状态信息，通常与订单ID拼接使用。
     * </p>
     * <p>示例值：{@code "query:orderStatus:"}</p>
     */
    String QUERY_ORDER_STATUS = "query:orderStatus:";

    /**
     * 订单编号的前缀。
     * <p>
     * 用于生成订单编号，确保订单编号的唯一性和可识别性。
     * </p>
     * <p>示例值：{@code "order_"}</p>
     */
    String ORDER_PREFIX = "order_";

    /**
     * 查询订单信息的缓存键前缀。
     * <p>
     * 用于在缓存中存储和检索订单详细信息，通常与订单ID拼接使用。
     * </p>
     * <p>示例值：{@code "query:orderInfo:"}</p>
     */
    String QUERY_ORDER_INFO = "query:orderInfo:";

    /**
     * 支付宝响应代码表示成功。
     * <p>
     * 在支付宝支付响应中，{@code "10000"} 表示交易成功。
     * </p>
     * <p>示例值：{@code "10000"}</p>
     */
    String RESPONSE_CODE_SUCCESS = "10000";

    /**
     * 交易成功状态。
     * <p>
     * 表示商户签约的产品支持退款功能的前提下，买家付款成功。
     * </p>
     * <p>示例值：{@code "TRADE_SUCCESS"}</p>
     */
    String TRADE_SUCCESS = "TRADE_SUCCESS";
}
