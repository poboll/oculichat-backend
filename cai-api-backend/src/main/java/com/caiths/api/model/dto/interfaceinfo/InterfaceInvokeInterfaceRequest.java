package com.caiths.api.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用接口参数
 *
 * @author xuan
 */
@Data
public class InterfaceInvokeInterfaceRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 请求参数
     */
    private String userRequestParams;

}