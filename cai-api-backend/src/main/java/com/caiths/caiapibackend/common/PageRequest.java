package com.caiths.caiapibackend.common;

import com.caiths.caiapibackend.constant.CommonConstant;
import lombok.Data;

/**
 * 分页请求类，用于封装分页相关的请求参数。
 * <p>
 * 该类包含当前页号、页面大小、排序字段和排序顺序等信息，用于支持分页功能。
 * </p>
 *
 * <p>
 * 示例：
 * <pre>{@code
 * PageRequest pageRequest = new PageRequest();
 * pageRequest.setCurrent(2);
 * pageRequest.setPageSize(20);
 * pageRequest.setSortField("createTime");
 * pageRequest.setSortOrder(CommonConstant.SORT_ORDER_DESC);
 * }</pre>
 * </p>
 *
 * @author
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class PageRequest {

    /**
     * 当前页号，默认为1。
     */
    private long current = 1;

    /**
     * 页面大小，默认为10。
     */
    private long pageSize = 10;

    /**
     * 排序字段，指定按照哪个字段进行排序。
     * <p>
     * 例如："createTime" 表示按照创建时间排序。
     * </p>
     */
    private String sortField;

    /**
     * 排序顺序，默认为升序。
     * <p>
     * 可选值：
     * <ul>
     *     <li>{@link CommonConstant#SORT_ORDER_ASC} 升序</li>
     *     <li>{@link CommonConstant#SORT_ORDER_DESC} 降序</li>
     * </ul>
     * </p>
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
