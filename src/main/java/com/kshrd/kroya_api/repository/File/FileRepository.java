package com.kshrd.kroya_api.repository.File;

import com.kshrd.kroya_api.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileName(String filename);
}
