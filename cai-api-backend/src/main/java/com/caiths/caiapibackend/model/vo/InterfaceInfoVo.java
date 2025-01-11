package com.caiths.caiapibackend.model.vo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.caiths.caiapicommon.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口信息的值对象，用于封装接口信息的分页查询结果。
 * <p>
 * 包含接口信息记录列表、分页相关信息及排序条件等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class InterfaceInfoVo implements Serializable {
    private static final long serialVersionUID = 5713565919036400439L;

    /**
     * 接口信息记录列表。
     */
    private List<InterfaceInfo> records;

    /**
     * 总记录数。
     */
    private long total;

    /**
     * 每页记录数。
     */
    private long size;

    /**
     * 当前页码。
     */
    private long current;

    /**
     * 排序条件列表。
     */
    private List<OrderItem> orders;

    /**
     * 是否优化统计 SQL。
     */
    private boolean optimizeCountSql;

    /**
     * 是否进行计数查询。
     */
    private boolean searchCount;

    /**
     * 是否优化联接统计 SQL。
     */
    private boolean optimizeJoinOfCountSql;

    /**
     * 计数 ID。
     */
    private String countId;

    /**
     * 最大限制。
     */
    private Long maxLimit;
}
