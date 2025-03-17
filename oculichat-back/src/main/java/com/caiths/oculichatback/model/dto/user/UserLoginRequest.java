package com.caiths.oculichatback.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求数据传输对象。
 * <p>
 * 该类用于封装用户通过账号和密码进行登录所需的参数，包括账号和密码。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号，用于登录。
     */
    private String userAccount;

    /**
     * 用户密码，用于身份验证。
     */
    private String userPassword;
}
