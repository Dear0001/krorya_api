package com.kshrd.kroya_api.repository.Credencials;

import com.kshrd.kroya_api.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<CredentialEntity,Long> {
      CredentialEntity findByUserId(Integer userId);
}
