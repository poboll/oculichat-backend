package com.caiths.caiapibackend.model.dto.productinfo;

import com.caiths.caiapibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 产品信息搜索文本请求数据传输对象。
 * <p>
 * 该类用于封装基于文本的产品信息搜索请求，包括搜索关键字和分页信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductInfoSearchTextRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -6337349622479990038L;

    /**
     * 搜索文本，用于匹配产品名称或描述中的关键字。
     */
    private String searchText;
}
