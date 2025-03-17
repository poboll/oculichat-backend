package com.caiths.oculichatback.constant;

/**
 * 通用常量接口，定义了应用程序中常用的常量值。
 * <p>
 * 该接口包含了用于排序操作的常量值，便于在不同模块中统一使用和维护。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>定义排序方式的常量，用于数据查询和结果排序。</li>
 *   <li>集中管理常用常量，减少硬编码，提升代码的可维护性和一致性。</li>
 * </ul>
 *
 * <p><strong>示例使用：</strong></p>
 * <pre>
 * // 使用常量进行排序
 * String sortOrder = CommonConstant.SORT_ORDER_ASC;
 *
 * // 在查询中应用排序
 * query.orderBy(CommonConstant.SORT_ORDER_ASC);
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface CommonConstant {

    /**
     * 升序排序方式。
     * <p>
     * 该常量用于表示升序（ascending）排序方式，常用于数据库查询或集合排序操作中。
     * </p>
     *
     * <p>示例值：{@code "ascend"}</p>
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序排序方式。
     * <p>
     * 该常量用于表示降序（descending）排序方式，常用于数据库查询或集合排序操作中。
     * </p>
     *
     * <p>示例值：{@code "descend"}</p>
     */
    String SORT_ORDER_DESC = "descend";
}
