package com.caiths.oculichatback.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求数据传输对象。
 * <p>
 * 该类用于封装更新用户信息所需的参数，包括用户ID、昵称、账号、头像、性别、角色、账号状态、密码以及钱包余额。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识符，用于指定需要更新的用户。
     */
    private Long id;

    /**
     * 用户昵称，用于显示和标识用户。
     */
    private String userName;

    /**
     * 用户账号，用于登录和识别。
     */
    private String userAccount;

    /**
     * 用户头像，存储用户的头像图片URL或路径。
     */
    private String userAvatar;

    /**
     * 性别，标识用户的性别信息。
     */
    private String gender;

    /**
     * 用户角色，用于定义用户的权限和访问级别。
     * <p>示例角色包括：user（普通用户）、admin（管理员）。</p>
     */
    private String userRole;

    /**
     * 账号状态，表示用户账号的当前状态。
     * <p>示例状态包括：
     * <ul>
     *     <li>0 - 正常</li>
     *     <li>1 - 封号</li>
     * </ul>
     * </p>
     */
    private Integer status;

    /**
     * 用户密码，用于身份验证。
     */
    private String userPassword;

    /**
     * 钱包余额（单位：分），表示用户的账户余额。
     */
    private Integer balance;
}
