package com.kshrd.kroya_api.repository.Code;

import com.kshrd.kroya_api.entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, Long> {
    CodeEntity findByEmail(String email);
}
