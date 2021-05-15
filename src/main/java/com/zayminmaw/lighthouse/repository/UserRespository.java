package com.zayminmaw.lighthouse.repository;

import com.zayminmaw.lighthouse.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
}
