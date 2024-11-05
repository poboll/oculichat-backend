package com.caiths.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiths.api.model.entity.InterfaceInfo;

/**
* @author mdo
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-10-11 14:28:49
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
