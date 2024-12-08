package com.kshrd.kroya_api.repository.Bank;

import com.kshrd.kroya_api.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {
    Optional<BankEntity> findByUserId(Integer userId);
}
