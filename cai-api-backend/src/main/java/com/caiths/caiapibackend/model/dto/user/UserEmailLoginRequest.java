package com.caiths.caiapibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户电子邮件登录请求数据传输对象。
 * <p>
 * 该类用于封装用户通过电子邮件进行登录所需的参数，包括电子邮件账号和验证码。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserEmailLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户的电子邮件账号，用于登录。
     */
    private String emailAccount;

    /**
     * 验证码，用于验证用户的身份。
     */
    private String captcha;
}
