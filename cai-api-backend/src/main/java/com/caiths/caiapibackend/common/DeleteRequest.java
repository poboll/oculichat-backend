package com.caiths.caiapibackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求类，用于封装删除操作所需的请求参数。
 * <p>
 * 该类包含一个主要属性 {@code id}，用于标识需要删除的资源的唯一标识符。
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 创建一个删除请求实例
 * DeleteRequest deleteRequest = new DeleteRequest();
 * deleteRequest.setId(123L);
 *
 * // 在控制器中接收删除请求
 * @DeleteMapping("/resource")
 * public ResponseEntity<BaseResponse<Void>> deleteResource(@RequestBody DeleteRequest deleteRequest) {
 *     // 调用服务层进行删除操作
 *     resourceService.deleteResource(deleteRequest.getId());
 *     return ResponseEntity.ok(BaseResponse.success());
 * }
 * }</pre>
 *
 * <p>适用范围：</p>
 * <ul>
 *     <li>所有需要根据唯一标识符删除资源的 API 接口。</li>
 *     <li>服务内部方法调用中需要删除操作的场景。</li>
 * </ul>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 需要删除的资源的唯一标识符。
     * <p>
     * 该 ID 通常对应数据库中资源的主键，用于定位具体需要删除的资源。
     * </p>
     *
     * <p>示例：</p>
     * <ul>
     *     <li>用户删除请求中的用户 ID。</li>
     *     <li>商品删除请求中的商品 ID。</li>
     * </ul>
     */
    private Long id;

    /**
     * 无参构造方法。
     * <p>
     * 该构造方法由 Lombok 的 {@code @Data} 注解自动生成，允许创建一个空的 {@code DeleteRequest} 实例。
     * </p>
     */
    // Lombok @Data 注解已经自动生成了无参构造方法

    /**
     * 全参构造方法。
     * <p>
     * 该构造方法由 Lombok 的 {@code @Data} 注解自动生成，允许在创建实例时初始化所有字段。
     * </p>
     *
     * @param id 需要删除的资源的唯一标识符
     */
    // Lombok @Data 注解已经自动生成了全参构造方法

    /**
     * 获取需要删除的资源的唯一标识符。
     *
     * @return 资源的唯一标识符 {@code id}
     */
    // Lombok @Data 注解已经自动生成了 getter 方法

    /**
     * 设置需要删除的资源的唯一标识符。
     *
     * @param id 资源的唯一标识符 {@code id}
     */
    // Lombok @Data 注解已经自动生成了 setter 方法
}
