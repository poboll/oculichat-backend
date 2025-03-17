package com.caiths.oculichatcommon.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图，表示对外展示的用户信息。
 *
 * <p>包括用户的基本信息、账号状态、权限信息等，用于前端展示。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识。
     */
    private Long id;

    /**
     * 用户的昵称，用于展示给其他用户。
     */
    private String userName;

    /**
     * 用户的邮箱地址。
     */
    private String email;

    /**
     * 用户的邀请码，用于邀请新用户注册。
     */
    private String invitationCode;

    /**
     * 账号状态（0- 正常，1- 封号）。
     */
    private Integer status;

    /**
     * 用户的钱包余额（以分为单位）。
     */
    private Integer balance;

    /**
     * 用户的账号名称，用于登录系统。
     */
    private String userAccount;

    /**
     * 用户的头像 URL，用于展示用户的图像。
     */
    private String userAvatar;

    /**
     * 用户的访问密钥，用于 API 访问授权。
     */
    private String accessKey;

    /**
     * 用户的秘密密钥，用于高级权限操作。
     */
    private String secretKey;

    /**
     * 用户的性别信息。
     */
    private String gender;

    /**
     * 用户的角色，区分普通用户和管理员（如 user, admin）。
     */
    private String userRole;

    /**
     * 账号的创建时间。
     */
    private Date createTime;

    /**
     * 账号的最后更新时间。
     */
    private Date updateTime;
}
