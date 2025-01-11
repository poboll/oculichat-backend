package com.caiths.caiapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caiths.caiapibackend.model.entity.ProductOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 产品订单映射器接口。
 * <p>
 * 负责处理与 {@link ProductOrder} 实体相关的数据库操作。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

}
