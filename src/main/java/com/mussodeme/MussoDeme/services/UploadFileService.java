package com.mussodeme.MussoDeme.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class UploadFileService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    public String uploadLogo(MultipartFile file) throws IOException {

        Files.createDirectories(Paths.get(uploadDir));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // URL relative que tu renverras au front
        return "/uploads/logos/" + fileName;
    }
}
