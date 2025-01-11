package com.caiths.caiapicommon.service.inner;

import com.caiths.caiapicommon.model.vo.UserVO;

/**
 * 用户服务，提供与用户相关的操作。
 *
 * <p>该服务用于获取和管理系统中的用户信息。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface InnerUserService {

    /**
     * 通过访问密钥获取调用用户的信息。
     *
     * <p>根据提供的访问密钥，查找并返回对应的用户信息。</p>
     *
     * @param accessKey 用户的访问密钥
     * @return {@link UserVO} 对应访问密钥的用户视图对象，如果未找到则返回 {@code null}
     */
    UserVO getInvokeUserByAccessKey(String accessKey);
}
