package com.caiths.caiapiinterface.repository;

import com.caiths.caiapiinterface.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据 accessKey 查找用户
    User findByAccessKey(String accessKey);
}
