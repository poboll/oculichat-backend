package com.caiths.caiapibackend.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caiths.caiapicommon.model.entity.InterfaceInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.caiths.caiapibackend.annotation.AuthCheck;
import com.caiths.caiapibackend.common.*;
import com.caiths.caiapibackend.constant.CommonConstant;
import com.caiths.caiapibackend.exception.BusinessException;
import com.caiths.caiapibackend.model.dto.interfaceinfo.*;
import com.caiths.caiapibackend.model.entity.User;
import com.caiths.caiapibackend.model.enums.InterfaceStatusEnum;
import com.caiths.caiapibackend.model.vo.UserVO;
import com.caiths.caiapibackend.service.InterfaceInfoService;
import com.caiths.caiapibackend.service.UserService;
import com.caiths.caiapisdk.client.CaiApiClient;
import com.caiths.caiapisdk.model.request.CurrencyRequest;
import com.caiths.caiapisdk.model.response.ResultResponse;
import com.caiths.caiapisdk.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.caiths.caiapibackend.constant.UserConstant.ADMIN_ROLE;

/**
 * 接口信息控制器，处理与接口信息相关的所有请求。
 * <p>
 * 提供接口信息的增删改查功能，包括发布、下线以及调用接口等操作。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private ApiService apiService;

    private final Gson gson = new Gson();

    // region 增删改查

    /**
     * 添加接口信息。
     * <p>
     * 仅管理员用户可以执行此操作。此方法负责将新的接口信息保存到数据库中。
     * </p>
     *
     * @param interfaceInfoAddRequest 接口信息添加请求，包含接口的详细信息
     * @param request                 当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Long}> 新添加的接口信息的 ID
     * @throws BusinessException 参数错误或操作失败时抛出业务异常
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口信息请求为空");
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();

        // 处理请求参数
        if (CollectionUtils.isNotEmpty(interfaceInfoAddRequest.getRequestParams())) {
            List<RequestParamsField> requestParamsFields = interfaceInfoAddRequest.getRequestParams().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldName()))
                    .collect(Collectors.toList());
            String requestParams = JSONUtil.toJsonStr(requestParamsFields);
            interfaceInfo.setRequestParams(requestParams);
        }

        // 处理响应参数
        if (CollectionUtils.isNotEmpty(interfaceInfoAddRequest.getResponseParams())) {
            List<ResponseParamsField> responseParamsFields = interfaceInfoAddRequest.getResponseParams().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldName()))
                    .collect(Collectors.toList());
            String responseParams = JSONUtil.toJsonStr(responseParamsFields);
            interfaceInfo.setResponseParams(responseParams);
        }

        // 拷贝属性
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);

        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);

        // 获取当前登录用户
        UserVO loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());

        // 保存接口信息
        boolean saveResult = interfaceInfoService.save(interfaceInfo);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口信息保存失败");
        }

        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除接口信息。
     * <p>
     * 仅接口拥有者或管理员用户可以执行此操作。此方法负责从数据库中删除指定的接口信息。
     * </p>
     *
     * @param deleteRequest 删除请求，包含要删除的接口信息 ID
     * @param request       当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 删除操作是否成功
     * @throws BusinessException 参数错误、接口信息不存在或无权限时抛出业务异常
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(deleteRequest, deleteRequest.getId()) || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除请求参数不合法");
        }

        UserVO user = userService.getLoginUser(request);
        long id = deleteRequest.getId();

        // 判断接口信息是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口信息不存在");
        }

        // 检查权限：仅接口拥有者或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该接口信息");
        }

        // 执行删除操作
        boolean deleteResult = interfaceInfoService.removeById(id);
        return ResultUtils.success(deleteResult);
    }

    /**
     * 更新接口头像 URL。
     * <p>
     * 仅管理员用户可以执行此操作。此方法负责更新指定接口信息的头像 URL。
     * </p>
     *
     * @param interfaceInfoUpdateAvatarRequest 接口信息更新头像请求，包含接口信息 ID 和新的头像 URL
     * @param request                           当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 更新操作是否成功
     * @throws BusinessException 参数错误或更新失败时抛出业务异常
     */
    @PostMapping("/updateInterfaceInfoAvatar")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfoAvatarUrl(@RequestBody InterfaceInfoUpdateAvatarRequest interfaceInfoUpdateAvatarRequest,
                                                              HttpServletRequest request) {
        if (ObjectUtils.anyNull(interfaceInfoUpdateAvatarRequest, interfaceInfoUpdateAvatarRequest.getId()) || interfaceInfoUpdateAvatarRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新头像请求参数不合法");
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateAvatarRequest, interfaceInfo);

        // 执行更新操作
        boolean updateResult = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateResult);
    }

    /**
     * 更新接口信息。
     * <p>
     * 仅管理员用户可以执行此操作。此方法负责更新指定的接口信息，包括请求参数和响应参数。
     * </p>
     *
     * @param interfaceInfoUpdateRequest 接口信息更新请求，包含接口信息的详细更新内容
     * @param request                    当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 更新操作是否成功
     * @throws BusinessException 参数错误、接口信息不存在、无权限或更新失败时抛出业务异常
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (ObjectUtils.anyNull(interfaceInfoUpdateRequest, interfaceInfoUpdateRequest.getId()) || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新请求参数不合法");
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();

        // 处理请求参数
        if (CollectionUtils.isNotEmpty(interfaceInfoUpdateRequest.getRequestParams())) {
            List<RequestParamsField> requestParamsFields = interfaceInfoUpdateRequest.getRequestParams().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldName()))
                    .collect(Collectors.toList());
            String requestParams = JSONUtil.toJsonStr(requestParamsFields);
            interfaceInfo.setRequestParams(requestParams);
        } else {
            interfaceInfo.setRequestParams("[]");
        }

        // 处理响应参数
        if (CollectionUtils.isNotEmpty(interfaceInfoUpdateRequest.getResponseParams())) {
            List<ResponseParamsField> responseParamsFields = interfaceInfoUpdateRequest.getResponseParams().stream()
                    .filter(field -> StringUtils.isNotBlank(field.getFieldName()))
                    .collect(Collectors.toList());
            String responseParams = JSONUtil.toJsonStr(responseParamsFields);
            interfaceInfo.setResponseParams(responseParams);
        } else {
            interfaceInfo.setResponseParams("[]");
        }

        // 拷贝属性
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);

        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);

        // 获取当前登录用户
        UserVO user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();

        // 判断接口信息是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口信息不存在");
        }

        // 检查权限：仅接口拥有者或管理员可修改
        if (!userService.isAdmin(request) && !oldInterfaceInfo.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改该接口信息");
        }

        // 执行更新操作
        boolean updateResult = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateResult);
    }

    /**
     * 通过 ID 获取接口信息。
     *
     * @param id 接口信息的唯一标识 ID
     * @return {@link BaseResponse}<{@link InterfaceInfo}> 包含指定 ID 的接口信息
     * @throws BusinessException 参数错误或接口信息不存在时抛出业务异常
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口信息 ID 不合法");
        }

        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口信息不存在");
        }

        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取所有接口信息列表（仅管理员可使用）。
     *
     * @param interfaceInfoQueryRequest 接口信息查询请求，包含过滤条件
     * @return {@link BaseResponse}<{@link List}<{@link InterfaceInfo}>>} 包含所有符合条件的接口信息列表
     * @throws BusinessException 参数错误时抛出业务异常
     */
    @AuthCheck(mustRole = ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(@ModelAttribute InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询请求参数为空");
        }

        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取接口信息列表。
     *
     * @param interfaceInfoQueryRequest 接口信息查询请求，包含分页和排序信息
     * @param request                   当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Page}<{@link InterfaceInfo}>>} 分页后的接口信息列表
     * @throws BusinessException 参数错误时抛出业务异常
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@ModelAttribute InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页查询请求参数为空");
        }

        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);

        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String url = interfaceInfoQueryRequest.getUrl();

        String name = interfaceInfoQueryRequest.getName();
        long current = interfaceInfoQueryRequest.getCurrent();
        String method = interfaceInfoQueryRequest.getMethod();
        String description = interfaceInfoQueryRequest.getDescription();
        Integer status = interfaceInfoQueryRequest.getStatus();
        Integer reduceScore = interfaceInfoQueryRequest.getReduceScore();
        String returnFormat = interfaceInfoQueryRequest.getReturnFormat();

        // 限制每页最大条数，防止爬虫恶意请求
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "每页查询数量不能超过50");
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();

        // 添加过滤条件
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(description)) {
            queryWrapper.and(qw -> qw.like("name", name).or().like("description", description));
        }

        queryWrapper
                .like(StringUtils.isNotBlank(url), "url", url)
                .like(StringUtils.isNotBlank(returnFormat), "returnFormat", returnFormat)
                .eq(StringUtils.isNotBlank(method), "method", method)
                .eq(ObjectUtils.isNotEmpty(status), "status", status)
                .eq(ObjectUtils.isNotEmpty(reduceScore), "reduceScore", reduceScore);

        // 添加排序条件
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);

        User user = userService.isTourist(request);

        // 非管理员用户仅可查看已上线的接口信息
        if (user == null || !user.getUserRole().equals(ADMIN_ROLE)) {
            List<InterfaceInfo> filteredList = interfaceInfoPage.getRecords().stream()
                    .filter(interfaceInfo -> interfaceInfo.getStatus().equals(InterfaceStatusEnum.ONLINE.getValue()))
                    .collect(Collectors.toList());
            interfaceInfoPage.setRecords(filteredList);
        }

        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 按搜索文本分页查询接口信息。
     *
     * @param interfaceInfoQueryRequest 接口信息搜索文本查询请求，包含搜索文本和分页信息
     * @param request                   当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Page}<{@link InterfaceInfo}>>} 分页后的接口信息列表
     * @throws BusinessException 参数错误时抛出业务异常
     */
    @GetMapping("/get/searchText")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoBySearchTextPage(@ModelAttribute InterfaceInfoSearchTextRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "搜索文本查询请求参数为空");
        }

        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);

        String searchText = interfaceInfoQueryRequest.getSearchText();
        long size = interfaceInfoQueryRequest.getPageSize();
        long current = interfaceInfoQueryRequest.getCurrent();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();

        // 添加搜索文本过滤条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like(StringUtils.isNotBlank(searchText), "name", searchText)
                    .or()
                    .like(StringUtils.isNotBlank(searchText), "description", searchText));
        }

        // 添加排序条件
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);

        // 非管理员用户仅可查看已上线的接口信息
        if (!userService.isAdmin(request)) {
            List<InterfaceInfo> filteredList = interfaceInfoPage.getRecords().stream()
                    .filter(interfaceInfo -> interfaceInfo.getStatus().equals(InterfaceStatusEnum.ONLINE.getValue()))
                    .collect(Collectors.toList());
            interfaceInfoPage.setRecords(filteredList);
        }

        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 发布接口信息。
     * <p>
     * 将指定的接口信息状态设置为上线。仅管理员用户可以执行此操作。
     * </p>
     *
     * @param idRequest 发布请求，包含要发布的接口信息 ID
     * @param request    当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 发布操作是否成功
     * @throws BusinessException 参数错误或接口信息不存在时抛出业务异常
     */
    @AuthCheck(mustRole = ADMIN_ROLE)
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(idRequest, idRequest.getId()) || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "发布请求参数不合法");
        }

        Long id = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口信息不存在");
        }

        interfaceInfo.setStatus(InterfaceStatusEnum.ONLINE.getValue());
        boolean updateResult = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateResult);
    }

    /**
     * 下线接口信息。
     * <p>
     * 将指定的接口信息状态设置为下线。仅管理员用户可以执行此操作。
     * </p>
     *
     * @param idRequest 下线请求，包含要下线的接口信息 ID
     * @param request    当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 下线操作是否成功
     * @throws BusinessException 参数错误或接口信息不存在时抛出业务异常
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(idRequest, idRequest.getId()) || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "下线请求参数不合法");
        }

        Long id = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口信息不存在");
        }

        interfaceInfo.setStatus(InterfaceStatusEnum.OFFLINE.getValue());
        boolean updateResult = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateResult);
    }

    // endregion

    /**
     * 调用指定的接口信息。
     * <p>
     * 此方法负责根据接口信息的配置，构建请求并调用外部接口服务。调用前会进行必要的权限和状态检查。
     * </p>
     *
     * @param invokeRequest 调用请求，包含接口信息 ID 及请求参数
     * @param request       当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Object}> 接口调用的返回结果
     * @throws BusinessException 参数错误、接口信息不存在、接口未上线或调用失败时抛出业务异常
     */
    @PostMapping("/invoke")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Object> invokeInterface(@RequestBody InvokeRequest invokeRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(invokeRequest, invokeRequest.getId()) || invokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "调用请求参数不合法");
        }

        Long id = invokeRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口信息不存在");
        }

        if (!interfaceInfo.getStatus().equals(InterfaceStatusEnum.ONLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未开启");
        }

        // 构建请求参数
        List<InvokeRequest.Field> fieldList = invokeRequest.getRequestParams();
        String requestParams = "{}";
        if (fieldList != null && !fieldList.isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            for (InvokeRequest.Field field : fieldList) {
                jsonObject.addProperty(field.getFieldName(), field.getValue());
            }
            requestParams = gson.toJson(jsonObject);
        }

        Map<String, Object> params = gson.fromJson(requestParams, new TypeToken<Map<String, Object>>() {}.getType());

        // 获取当前登录用户的访问密钥
        UserVO loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        try {
            CaiApiClient caiApiClient = new CaiApiClient(accessKey, secretKey);
            CurrencyRequest currencyRequest = new CurrencyRequest();
            currencyRequest.setMethod(interfaceInfo.getMethod());
            currencyRequest.setPath(interfaceInfo.getUrl());
            currencyRequest.setRequestParams(params);

            // 调用外部 API 服务
            ResultResponse response = apiService.request(caiApiClient, currencyRequest);
            return ResultUtils.success(response.getData());
        } catch (Exception e) {
            log.error("接口调用失败，接口ID={}，错误信息={}", id, e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用失败: " + e.getMessage());
        }
    }
}
