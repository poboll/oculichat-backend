package com.caiths.oculichatback.constant;

/**
 * 定义与用户相关的常量。
 * <p>
 * 包括用户登录状态、系统用户 ID、权限角色以及安全相关的常量。
 * </p>
 *
 * @author
 * @version 1.0
 * @since 2024-04-27
 */
public interface UserConstant {

    /**
     * 用户登录状态的键值，用于在会话或上下文中存储用户的登录状态。
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 系统用户的唯一标识 ID，用于表示系统或虚拟用户。
     */
    long SYSTEM_USER_ID = 0L;

    //  region 权限

    /**
     * 默认用户角色，赋予普通用户的权限。
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员用户角色，赋予具有管理权限的用户。
     */
    String ADMIN_ROLE = "admin";

    // endregion

    /**
     * 用于密码加密的盐值，增加密码的安全性。
     */
    String SALT = "poboll";

    /**
     * 用于混淆访问密钥和密钥秘密的常量。
     */
    String VOUCHER = "accessKey_secretKey";

}
