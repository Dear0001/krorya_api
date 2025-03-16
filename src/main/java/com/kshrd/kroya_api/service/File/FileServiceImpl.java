package com.kshrd.kroya_api.service.File;

import com.kshrd.kroya_api.entity.FileEntity;
import com.kshrd.kroya_api.repository.File.FileRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final MinioClient minioClient;
    private final FileRepository fileRepository;

    @Value("${minio.bucketName}")
    private String bucketName;

    public FileServiceImpl(MinioClient minioClient, FileRepository fileRepository) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
    }

    @Override
    public FileEntity InsertFile(FileEntity fileEntity) {
        return fileRepository.save(fileEntity);
    }

    @Override
    public String Uplaodfile(MultipartFile file) throws IOException {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Check if bucket exists, create if it doesn't
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Upload file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Generate a pre-signed URL for the uploaded file
            String fileUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET) // HTTP method (GET, PUT, etc.)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(60 * 60 * 24) // URL expiry time in seconds (e.g., 1 day)
                            .build()
            );

            // Save file details to the database
            FileEntity fileEntity = new FileEntity(fileUrl, fileName);
            fileRepository.save(fileEntity);

            return fileUrl; // Return the pre-signed URL
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("Error uploading file to MinIO: " + e.getMessage());
        }
    }

    @Override
    public Resource getFile(String fileName) {
        try {
            // Check if file exists in DB
            FileEntity fileEntity = fileRepository.findByFileName(fileName);
            if (fileEntity == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found in database: " + fileName);
            }

            // Get file from MinIO
            GetObjectResponse object = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build()
            );


            return new ByteArrayResource(object.readAllBytes());

        } catch (MinioException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
