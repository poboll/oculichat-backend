package com.caiths.oculichatback.model.dto.productinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求数据传输对象。
 * <p>
 * 该类用于封装更新产品信息所需的参数，包括产品ID、名称、描述、金额、积分、类型以及过期时间。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class ProductInfoUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品的唯一标识符，用于指定需要更新的产品。
     */
    private long id;

    /**
     * 产品名称，用于标识和描述产品。
     */
    private String name;

    /**
     * 产品描述，提供关于产品的详细信息。
     */
    private String description;

    /**
     * 金额（单位：分），表示产品的价格。
     */
    private Integer total;

    /**
     * 增加积分个数，用于激励用户购买或使用产品。
     */
    private Integer addPoints;

    /**
     * 产品类型，用于区分不同类别的产品。
     * <p>示例类型包括：VIP-会员、RECHARGE-充值。</p>
     */
    private String productType;

    /**
     * 过期时间，表示产品的有效期。
     */
    private Date expirationTime;
}
