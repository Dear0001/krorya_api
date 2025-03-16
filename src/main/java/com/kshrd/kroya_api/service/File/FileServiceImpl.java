package com.kshrd.kroya_api.service.File;

import com.kshrd.kroya_api.entity.FileEntity;
import com.kshrd.kroya_api.repository.File.FileRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private final FileRepository fileRepository;
    @Value("${minio.bucketName}")
    private String bucketName;
    private final MinioClient minioClient;

    public FileServiceImpl(FileRepository fileRepository, MinioClient minioClient) {
        this.fileRepository = fileRepository;
        this.minioClient = minioClient;
    }

    @Override
    public FileEntity InsertFile(FileEntity fileEntity) {
        return fileRepository.save(fileEntity);
    }

 @Override
 public String Uplaodfile(MultipartFile file) throws IOException, MinioException {
     String fileName = file.getOriginalFilename();
     try (InputStream stream = file.getInputStream()) {
         fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);

         if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())){
             minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
         }

         //1. upload from any source and provide data as an InputStream
         minioClient.putObject(PutObjectArgs.builder()
                 .bucket(bucketName)
                 .object(fileName)
                 .stream(stream, stream.available(), -1)
                 .build());

         stream.close();
         return fileName;
     } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
         throw new MinioException("Failed to upload file");
     }
 }

    @Override
    public String getFile(String fileName) {
        try {
            // Find the file entity from the repository
            FileEntity files = fileRepository.findByFileName(fileName);
            if (files == null) {
                throw new FileNotFoundException("File not found with name: " + fileName);
            }

            String savePath = "src/main/resources/Datauplaod/" + fileName;
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .filename(savePath)
                            .build());
            System.out.println("File downloaded successfully to: " + savePath);
            return "success";
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}