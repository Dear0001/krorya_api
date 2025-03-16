package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.entity.FileEntity;
import com.kshrd.kroya_api.payload.File.FileResponse;
import com.kshrd.kroya_api.service.File.FileService;
import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fileView")
@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "https://krorya-dashbaord.vercel.app"
        },
        allowedHeaders = "*", allowCredentials = "true"
)
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(
            summary = "📤 Upload Multiple Files",
            description = """
                    Uploads one or more files to the server.
                    - **Request Parameter**: **files** (List of `MultipartFile`): Files to be uploaded.
                    
                    **📩 Response Summary**:
                    - **201**: ✅ Files uploaded successfully, returns URLs of the uploaded files.
                    - **400**: 🚫 Invalid file format or missing files.
                    """
    )
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] files) throws MinioException, IOException{
        try {
            if (files.length == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("🚫 No files uploaded.");
            }

            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("🚫 One or more files are empty.");
                }

                // ✅ Fix: Ensure fileService method name is correct
                String fileName = fileService.Uplaodfile(file);
                String url = ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .replacePath("/api/v1/fileView/" + fileName)
                        .toUriString();

                FileEntity fileEntity = new FileEntity(url, fileName);
                fileService.InsertFile(fileEntity);
                fileUrls.add(url);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(new FileResponse<>(
                    "✅ Upload successful",
                    201,
                    fileUrls
            ));
        } catch (MinioException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error uploading files: " + e.getMessage());
        }
    }

    @Operation(
            summary = "📥 Download File by Name",
            description = """
                    Retrieves a file from the server based on its name.
                    - **Path Variable**: **fileName** (String): Name of the file to be downloaded.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ File retrieved successfully.
                    - **404**: 🚫 File not found.
                    """
    )
    @GetMapping("/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable String fileName) throws MinioException {
        try {
            Resource file = fileService.getFile(fileName);
            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("🚫 File not found: " + fileName);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);
        } catch (IOException | MinioException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error retrieving file: " + e.getMessage());
        }
    }
}
