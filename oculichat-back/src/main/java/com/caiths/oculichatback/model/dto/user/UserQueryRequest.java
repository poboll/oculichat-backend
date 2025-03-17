package com.caiths.oculichatback.model.dto.user;

import com.caiths.oculichatback.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求数据传输对象。
 * <p>
 * 该类用于封装查询用户信息的请求参数，包括用户ID、昵称、账号、性别、角色以及分页信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识符，用于指定查询的用户。
     */
    private Long id;

    /**
     * 用户昵称，用于按昵称筛选用户。
     */
    private String userName;

    /**
     * 用户账号，用于按账号筛选用户。
     */
    private String userAccount;

    /**
     * 性别，用于按性别筛选用户。
     */
    private String gender;

    /**
     * 用户角色，用于按角色筛选用户。
     * <p>示例角色包括：user（普通用户）、admin（管理员）。</p>
     */
    private String userRole;
}
