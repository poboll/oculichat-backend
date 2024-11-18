package com.caiths.api.model.vo;

import com.caiths.caiapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvokeInterfaceInfoVO extends InterfaceInfo {

    /**
     * 接口调用次数
     */
    private Integer invokeNum;

    private static final long serialVersionUID = 1L;

}