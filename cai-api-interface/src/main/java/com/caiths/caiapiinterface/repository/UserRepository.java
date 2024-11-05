package com.caiths.caiapiinterface.repository;

import com.caiths.caiapiinterface.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户数据存储库，提供与用户相关的数据库操作。
 * 继承 JpaRepository 提供基本的 CRUD 操作。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 通过 accessKey 查找用户。
     * 如果未找到用户，将返回 null。
     * @param accessKey 用户的 accessKey
     * @return 返回用户对象，如果找不到用户则返回 null。
     */
    User findByAccessKey(String accessKey);

}
