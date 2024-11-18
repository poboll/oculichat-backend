package com.caiths.api.controller;

import com.caiths.api.annotation.AuthCheck;
import com.caiths.api.common.BaseResponse;
import com.caiths.api.common.ResultUtils;
import com.caiths.api.mapper.UserInterfaceInfoMapper;
import com.caiths.api.model.vo.InvokeInterfaceInfoVO;
import com.caiths.api.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分析控制器
 *
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InvokeInterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        // 查询调用次数最多的前 3 个接口
        List<InvokeInterfaceInfoVO> invokeInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        return ResultUtils.success(invokeInfoList);
    }
}
