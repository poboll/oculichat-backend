package com.caiths.oculichatback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caiths.oculichatback.annotation.AuthCheck;
import com.caiths.oculichatback.common.*;
import com.caiths.oculichatback.constant.CommonConstant;
import com.caiths.oculichatback.exception.BusinessException;
import com.caiths.oculichatback.model.dto.productinfo.*;
import com.caiths.oculichatback.model.entity.ProductInfo;
import com.caiths.oculichatback.model.enums.ProductInfoStatusEnum;
import com.caiths.oculichatback.model.vo.UserVO;
import com.caiths.oculichatback.service.ProductInfoService;
import com.caiths.oculichatback.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.caiths.oculichatback.constant.UserConstant.ADMIN_ROLE;

/**
 * 产品信息控制器，处理与产品信息相关的请求。
 * <p>
 * 提供产品信息的增删改查接口，包括添加新产品、删除产品、更新产品信息、获取单个产品信息、
 * 获取产品列表（支持分页和搜索）、以及发布和下线产品。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/productInfo")
@Slf4j
public class ProductInfoController {

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private UserService userService;


    // region 增删改查

    /**
     * 添加产品信息。
     * <p>
     * 该方法处理添加新产品的信息，包括校验输入参数、设置创建者用户ID，并将产品信息保存到数据库中。
     * </p>
     *
     * @param productInfoAddRequest 产品信息添加请求，包含产品的详细信息
     * @param request               当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Long}> 成功时返回新创建的产品ID
     * @throws BusinessException 当请求参数无效或操作失败时抛出业务异常
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addProductInfo(@RequestBody ProductInfoAddRequest productInfoAddRequest, HttpServletRequest request) {
        if (productInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoAddRequest, productInfo);
        // 校验产品信息
        productInfoService.validProductInfo(productInfo, true);
        // 获取当前登录用户信息
        UserVO loginUser = userService.getLoginUser(request);
        productInfo.setUserId(loginUser.getId());
        boolean result = productInfoService.save(productInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "产品信息保存失败");
        }
        long newProductInfoId = productInfo.getId();
        return ResultUtils.success(newProductInfoId);
    }

    /**
     * 删除产品信息。
     * <p>
     * 该方法处理删除指定ID的产品信息，只有产品创建者或管理员有权限执行此操作。
     * </p>
     *
     * @param deleteRequest 删除请求，包含要删除的产品ID
     * @param request       当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 成功时返回 {@code true}，表示删除成功
     * @throws BusinessException 当请求参数无效、产品不存在或用户无权限删除时抛出业务异常
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteProductInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(deleteRequest, deleteRequest.getId()) || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的删除请求参数");
        }
        UserVO user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断产品是否存在
        ProductInfo oldProductInfo = productInfoService.getById(id);
        if (oldProductInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品信息不存在");
        }
        // 检查是否有权限删除
        if (!oldProductInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该产品信息");
        }
        boolean isDeleted = productInfoService.removeById(id);
        return ResultUtils.success(isDeleted);
    }

    /**
     * 更新产品信息。
     * <p>
     * 该方法处理更新指定ID的产品信息，只有产品创建者或管理员有权限执行此操作。
     * </p>
     *
     * @param productInfoUpdateRequest 产品信息更新请求，包含更新后的产品详细信息
     * @param request                  当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Boolean}> 成功时返回 {@code true}，表示更新成功
     * @throws BusinessException 当请求参数无效、产品不存在或用户无权限更新时抛出业务异常
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> updateProductInfo(@RequestBody ProductInfoUpdateRequest productInfoUpdateRequest,
                                                   HttpServletRequest request) {
        if (ObjectUtils.anyNull(productInfoUpdateRequest, productInfoUpdateRequest.getId()) || productInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的更新请求参数");
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoUpdateRequest, productInfo);
        // 校验产品信息
        productInfoService.validProductInfo(productInfo, false);
        // 获取当前登录用户信息
        UserVO user = userService.getLoginUser(request);
        long id = productInfoUpdateRequest.getId();
        // 判断产品是否存在
        ProductInfo oldProductInfo = productInfoService.getById(id);
        if (oldProductInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品信息不存在");
        }
        // 检查是否有权限更新
        if (!userService.isAdmin(request) && !oldProductInfo.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限更新该产品信息");
        }
        boolean isUpdated = productInfoService.updateById(productInfo);
        return ResultUtils.success(isUpdated);
    }

    /**
     * 通过ID获取产品信息。
     * <p>
     * 该方法处理根据产品ID获取对应的产品详细信息。
     * </p>
     *
     * @param id 产品ID
     * @return {@link BaseResponse}<{@link ProductInfo}> 成功时返回对应的产品信息
     * @throws BusinessException 当产品ID无效或产品不存在时抛出业务异常
     */
    @GetMapping("/get")
    public BaseResponse<ProductInfo> getProductInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的产品ID");
        }
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品信息不存在");
        }
        return ResultUtils.success(productInfo);
    }

    /**
     * 获取产品信息列表（仅管理员可使用）。
     * <p>
     * 该方法处理获取所有产品信息的列表，支持根据查询条件过滤产品信息。
     * 仅具有管理员角色的用户可以调用此接口。
     * </p>
     *
     * @param productInfoQueryRequest 产品信息查询请求，包含过滤条件
     * @return {@link BaseResponse}<{@link List}<{@link ProductInfo}>> 成功时返回产品信息列表
     * @throws BusinessException 当请求参数无效时抛出业务异常
     */
    @AuthCheck(mustRole = ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<ProductInfo>> listProductInfo(ProductInfoQueryRequest productInfoQueryRequest) {
        if (productInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        ProductInfo productInfoQuery = new ProductInfo();
        BeanUtils.copyProperties(productInfoQueryRequest, productInfoQuery);

        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>(productInfoQuery);
        List<ProductInfo> productInfoList = productInfoService.list(queryWrapper);
        return ResultUtils.success(productInfoList);
    }

    /**
     * 分页获取产品信息列表。
     * <p>
     * 该方法处理根据分页参数和查询条件获取产品信息的分页列表，支持排序。
     * 如果用户不是管理员，则只能查看已上线的产品。
     * </p>
     *
     * @param productInfoQueryRequest 产品信息查询请求，包含分页和过滤条件
     * @param request                 当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Page}<{@link ProductInfo}>> 成功时返回分页的产品信息列表
     * @throws BusinessException 当请求参数无效时抛出业务异常
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<ProductInfo>> listProductInfoByPage(ProductInfoQueryRequest productInfoQueryRequest, HttpServletRequest request) {
        if (productInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        ProductInfo productInfoQuery = new ProductInfo();
        BeanUtils.copyProperties(productInfoQueryRequest, productInfoQuery);
        long size = productInfoQueryRequest.getPageSize();
        String sortField = productInfoQueryRequest.getSortField();
        String sortOrder = productInfoQueryRequest.getSortOrder();

        String name = productInfoQueryRequest.getName();
        long current = productInfoQueryRequest.getCurrent();
        String description = productInfoQueryRequest.getDescription();
        String productType = productInfoQueryRequest.getProductType();
        Integer addPoints = productInfoQueryRequest.getAddPoints();
        Integer total = productInfoQueryRequest.getTotal();
        // 限制请求大小，防止爬虫攻击
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求大小超出限制");
        }

        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(description), "description", description)
                .eq(StringUtils.isNotBlank(productType), "productType", productType)
                .eq(ObjectUtils.isNotEmpty(addPoints), "addPoints", addPoints)
                .eq(ObjectUtils.isNotEmpty(total), "total", total);
        // 根据金额升序排列
        queryWrapper.orderByAsc("total");
        Page<ProductInfo> productInfoPage = productInfoService.page(new Page<>(current, size), queryWrapper);
        // 如果用户不是管理员，只显示已上线的产品
        if (!userService.isAdmin(request)) {
            List<ProductInfo> filteredList = productInfoPage.getRecords().stream()
                    .filter(productInfo -> productInfo.getStatus().equals(ProductInfoStatusEnum.ONLINE.getValue()))
                    .collect(Collectors.toList());
            productInfoPage.setRecords(filteredList);
        }
        return ResultUtils.success(productInfoPage);
    }

    /**
     * 按搜索文本分页查询产品信息。
     * <p>
     * 该方法处理根据搜索文本（如产品名称或描述）分页查询产品信息的请求，支持排序。
     * 如果用户不是管理员，则只能查看已上线的产品。
     * </p>
     *
     * @param productInfoSearchTextRequest 产品信息搜索文本分页请求，包含搜索文本和分页参数
     * @param request                      当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse}<{@link Page}<{@link ProductInfo}>> 成功时返回分页的产品信息列表
     * @throws BusinessException 当请求参数无效时抛出业务异常
     */
    @GetMapping("/get/searchText")
    public BaseResponse<Page<ProductInfo>> listProductInfoBySearchTextPage(ProductInfoSearchTextRequest productInfoSearchTextRequest, HttpServletRequest request) {
        if (productInfoSearchTextRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        ProductInfo productInfoQuery = new ProductInfo();
        BeanUtils.copyProperties(productInfoSearchTextRequest, productInfoQuery);

        String searchText = productInfoSearchTextRequest.getSearchText();
        long size = productInfoSearchTextRequest.getPageSize();
        long current = productInfoSearchTextRequest.getCurrent();
        String sortField = productInfoSearchTextRequest.getSortField();
        String sortOrder = productInfoSearchTextRequest.getSortOrder();

        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or()
                    .like("description", searchText));
        }
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<ProductInfo> productInfoPage = productInfoService.page(new Page<>(current, size), queryWrapper);
        // 如果用户不是管理员，只显示已上线的产品
        if (!userService.isAdmin(request)) {
            List<ProductInfo> filteredList = productInfoPage.getRecords().stream()
                    .filter(productInfo -> productInfo.getStatus().equals(ProductInfoStatusEnum.ONLINE.getValue()))
                    .collect(Collectors.toList());
            productInfoPage.setRecords(filteredList);
        }
        return ResultUtils.success(productInfoPage);
    }

    /**
     * 发布产品信息。
     * <p>
     * 该方法处理将指定ID的产品状态设置为“上线”，只有管理员有权限执行此操作。
     * </p>
     *
     * @param idRequest 发布请求，包含要发布的产品ID
     * @return {@link BaseResponse}<{@link Boolean}> 成功时返回 {@code true}，表示发布成功
     * @throws BusinessException 当请求参数无效或产品不存在时抛出业务异常
     */
    @AuthCheck(mustRole = ADMIN_ROLE)
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineProductInfo(@RequestBody IdRequest idRequest) {
        if (ObjectUtils.anyNull(idRequest, idRequest.getId()) || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的发布请求参数");
        }
        Long id = idRequest.getId();
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品信息不存在");
        }
        productInfo.setStatus(ProductInfoStatusEnum.ONLINE.getValue());
        boolean isUpdated = productInfoService.updateById(productInfo);
        return ResultUtils.success(isUpdated);
    }

    /**
     * 下线产品信息。
     * <p>
     * 该方法处理将指定ID的产品状态设置为“下线”，只有管理员有权限执行此操作。
     * </p>
     *
     * @param idRequest 下线请求，包含要下线的产品ID
     * @return {@link BaseResponse}<{@link Boolean}> 成功时返回 {@code true}，表示下线成功
     * @throws BusinessException 当请求参数无效或产品不存在时抛出业务异常
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> offlineProductInfo(@RequestBody IdRequest idRequest) {
        if (ObjectUtils.anyNull(idRequest, idRequest.getId()) || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的下线请求参数");
        }
        Long id = idRequest.getId();
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品信息不存在");
        }
        productInfo.setStatus(ProductInfoStatusEnum.OFFLINE.getValue());
        boolean isUpdated = productInfoService.updateById(productInfo);
        return ResultUtils.success(isUpdated);
    }
    // endregion
}
