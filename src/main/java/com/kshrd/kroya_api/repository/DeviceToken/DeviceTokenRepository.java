package com.kshrd.kroya_api.repository.DeviceToken;

import com.kshrd.kroya_api.entity.DeviceTokenEntity;
import com.kshrd.kroya_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceTokenEntity,Long> {
    DeviceTokenEntity findByUserId(Integer userId);
    DeviceTokenEntity findByUser(UserEntity seller);
}
