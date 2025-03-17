package com.caiths.oculichatback.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户取消绑定电子邮件请求数据传输对象。
 * <p>
 * 该类用于封装用户取消绑定电子邮件所需的参数，包括电子邮件账号和验证码。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class UserUnBindEmailRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户的电子邮件账号，用于取消绑定操作。
     */
    private String emailAccount;

    /**
     * 验证码，用于验证电子邮件的有效性。
     */
    private String captcha;
}
