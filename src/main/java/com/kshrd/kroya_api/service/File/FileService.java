package com.kshrd.kroya_api.service.File;

import com.kshrd.kroya_api.entity.FileEntity;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    FileEntity InsertFile(FileEntity fileEntity);

    String Uplaodfile(MultipartFile file) throws IOException, MinioException;

    String getFile(String fileName) throws IOException, MinioException;
}