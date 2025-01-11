package com.caiths.caiapibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiths.caiapibackend.common.ErrorCode;
import com.caiths.caiapibackend.exception.BusinessException;
import com.caiths.caiapibackend.mapper.ProductInfoMapper;
import com.caiths.caiapibackend.model.entity.ProductInfo;
import com.caiths.caiapibackend.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * ProductInfoServiceImpl 是产品信息服务的实现类。
 * <p>
 * 该类负责处理产品信息的验证、创建与更新等业务逻辑，
 * 确保产品信息的合法性和数据的一致性。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo>
        implements ProductInfoService {

    /**
     * 验证产品信息的合法性。
     *
     * @param productInfo 产品信息实体
     * @param add         是否为新增操作
     */
    @Override
    public void validProductInfo(ProductInfo productInfo, boolean add) {
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = productInfo.getName();
        String description = productInfo.getDescription();
        Integer total = productInfo.getTotal();
        Date expirationTime = productInfo.getExpirationTime();
        String productType = productInfo.getProductType();
        Integer addPoints = productInfo.getAddPoints();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (addPoints < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "增加积分不能为负数");
        }
        if (total < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "售卖金额不能为负数");
        }
    }
}
