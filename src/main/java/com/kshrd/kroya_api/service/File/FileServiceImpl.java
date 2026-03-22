package com.kshrd.kroya_api.service.File;

import com.kshrd.kroya_api.entity.FileEntity;
import com.kshrd.kroya_api.repository.File.FileRepository;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private final FileRepository fileRepository;
    private final MinioClient minioClient;
    private final String bucketName;
    private final String minioUrl;

    public FileServiceImpl(
            FileRepository fileRepository,
            MinioClient minioClient,
            @Value("${minio.bucketName}") String bucketName,
            @Value("${minio.url}") String minioUrl
    ) {
        this.fileRepository = fileRepository;
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        this.minioUrl = minioUrl;
    }

    @PostConstruct
    public void ensureBucketExists() {
        // Queue 4 requirement: force local MinIO only.
        if (!(minioUrl.contains("localhost") || minioUrl.contains("127.0.0.1"))) {
            throw new IllegalStateException("Only local MinIO endpoints are allowed for file storage.");
        }
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize MinIO bucket: " + bucketName, e);
        }
    }

    @Override
    public FileEntity InsertFile(FileEntity fileEntity) {
        return fileRepository.save(fileEntity);
    }

    @Override
    public String Uplaodfile(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                return fileName;
            } else {
                return "File Not Found!";
            }
        } catch (Exception ex) {
            throw new IOException("File not found!");
        }
    }

    @Override
    public Resource getFile(String fileName) {
        try {
            // Find the file entity from the repository
            FileEntity files = fileRepository.findByFileName(fileName);
            if (files == null) {
                throw new FileNotFoundException("File not found with name: " + fileName);
            }

            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(files.getFileName())
                            .build()
            )) {
                return new ByteArrayResource(stream.readAllBytes());
            }

        } catch (FileNotFoundException e) {
            // Handle the case when the file entity is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);

        } catch (IOException e) {
            // Handle issues with file access
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file: " + fileName, e);

        } catch (Exception e) {
            // Handle any other unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }
    }

}