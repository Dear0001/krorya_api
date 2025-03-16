package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.File.FileResponse;
import com.kshrd.kroya_api.service.File.FileService;
import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
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
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "📤 Upload Multiple Files")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException, MinioException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = fileService.Uplaodfile(file);
            String url = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .replacePath("/api/v1/fileView/" + fileName)
                    .toUriString();
            fileUrls.add(url);
        }

        return ResponseEntity.ok(new FileResponse<>("Upload files successfully", 201, fileUrls));
    }

    @Operation(summary = "📥 Download File by Name")
    @GetMapping("/{fileName}")
    public ResponseEntity<String> getFile(@PathVariable String fileName) throws IOException, MinioException {
        String file = fileService.getFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
