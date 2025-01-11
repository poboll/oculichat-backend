package com.caiths.caiapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类。
 * <p>
 * 该类映射到数据库中的 `user` 表，用于记录用户的详细信息，包括昵称、账号、邮箱、角色等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户昵称，用于显示和标识用户。
     */
    private String userName;

    /**
     * 用户邮箱，用于通信和账号验证。
     */
    private String email;

    /**
     * 用户账号，用于登录和识别。
     */
    private String userAccount;

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
     * 邀请码，用于推荐或邀请注册。
     */
    private String invitationCode;

    /**
     * 访问密钥，用于API调用或其他认证需求。
     */
    private String accessKey;

    /**
     * 钱包余额（单位：分），表示用户的账户余额。
     */
    private Integer balance;

    /**
     * 秘密密钥，用于API调用的安全认证。
     */
    private String secretKey;

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
     * <p>示例角色包括：
     * <ul>
     *     <li>user - 普通用户</li>
     *     <li>admin - 管理员</li>
     * </ul>
     * </p>
     */
    private String userRole;

    /**
     * 用户密码，用于身份验证。
     */
    private String userPassword;

    /**
     * 创建时间，记录用户信息的创建时间。
     */
    private Date createTime;

    /**
     * 更新时间，记录用户信息的最后更新时间。
     */
    private Date updateTime;

    /**
     * 是否删除，逻辑删除标识。
     * <p>示例值包括：
     * <ul>
     *     <li>0 - 未删除</li>
     *     <li>1 - 已删除</li>
     * </ul>
     * </p>
     */
    @TableLogic
    private Integer isDelete;
}
