package com.caiths.caiapibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户电子邮件注册请求数据传输对象。
 * <p>
 * 该类用于封装用户通过电子邮件进行注册所需的参数，包括电子邮件账号、验证码、用户名、邀请码以及用户协议的同意情况。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserEmailRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户的电子邮件账号，用于注册。
     */
    private String emailAccount;

    /**
     * 验证码，用于验证电子邮件的有效性。
     */
    private String captcha;

    /**
     * 用户昵称，用于显示和标识用户。
     */
    private String userName;

    /**
     * 邀请码，用于推荐或邀请注册。
     */
    private String invitationCode;

    /**
     * 是否同意用户协议，表示用户是否同意相关条款。
     */
    private String agreeToAnAgreement;
}
