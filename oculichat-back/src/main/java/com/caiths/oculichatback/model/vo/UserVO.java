package com.caiths.oculichatback.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户的值对象，用于封装用户相关的信息。
 * <p>
 * 包含用户的基本信息，如昵称、邮箱、邀请码、账号状态、钱包余额等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识 ID。
     */
    private Long id;

    /**
     * 用户昵称。
     */
    private String userName;

    /**
     * 用户邮箱。
     */
    private String email;

    /**
     * 邀请码。
     */
    private String invitationCode;

    /**
     * 账号状态。
     * <p>
     * 0 - 正常<br>
     * 1 - 封号
     * </p>
     */
    private Integer status;

    /**
     * 钱包余额（单位：分）。
     */
    private Integer balance;

    /**
     * 用户账号。
     */
    private String userAccount;

    /**
     * 用户头像的 URL。
     */
    private String userAvatar;

    /**
     * 访问密钥。
     */
    private String accessKey;

    /**
     * 秘密密钥。
     */
    private String secretKey;

    /**
     * 用户性别。
     */
    private String gender;

    /**
     * 用户角色。
     * <p>
     * 可能的角色包括：
     * <ul>
     *     <li>user - 普通用户</li>
     *     <li>admin - 管理员</li>
     * </ul>
     * </p>
     */
    private String userRole;

    /**
     * 账号创建时间。
     */
    private Date createTime;

    /**
     * 账号更新时间。
     */
    private Date updateTime;
}
