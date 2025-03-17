package com.caiths.oculichatback.model.dto.productinfo;

import com.caiths.oculichatback.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求数据传输对象。
 * <p>
 * 该类用于封装查询产品信息的请求参数，包括产品名称、描述、金额、积分、类型以及分页信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductInfoQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称，用于按名称筛选产品。
     */
    private String name;

    /**
     * 增加积分个数，用于按积分筛选产品。
     */
    private Integer addPoints;

    /**
     * 产品描述，用于按描述内容筛选产品。
     */
    private String description;

    /**
     * 金额（单位：分），用于按价格筛选产品。
     */
    private Integer total;

    /**
     * 产品类型，用于按类别筛选产品。
     * <p>示例类型包括：VIP-会员、RECHARGE-充值。</p>
     */
    private String productType;
}
