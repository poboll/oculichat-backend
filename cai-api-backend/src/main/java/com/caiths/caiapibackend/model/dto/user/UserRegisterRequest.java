package com.caiths.caiapibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求数据传输对象。
 * <p>
 * 该类用于封装用户注册所需的参数，包括账号、密码、用户名、确认密码、邀请码以及用户协议的同意情况。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号，用于登录和识别。
     */
    private String userAccount;

    /**
     * 用户密码，用于身份验证。
     */
    private String userPassword;

    /**
     * 用户昵称，用于显示和标识用户。
     */
    private String userName;

    /**
     * 确认密码，用于验证用户输入的密码一致性。
     */
    private String checkPassword;

    /**
     * 邀请码，用于推荐或邀请注册。
     */
    private String invitationCode;

    /**
     * 是否同意用户协议，表示用户是否同意相关条款。
     */
    private String agreeToAnAgreement;
}
