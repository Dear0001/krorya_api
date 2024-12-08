package com.kshrd.kroya_api.repository.User;

import com.kshrd.kroya_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity getById(Integer userId);

    UserEntity findByEmail(String email);

    UserEntity findByPhoneNumber(String phoneNumber);

    UserEntity findFirstByEmail(String email);
}
