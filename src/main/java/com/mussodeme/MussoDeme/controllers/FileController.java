package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    // Constructor for dependency injection
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // -------------------- Upload d’un audio --------------------
    @PostMapping(value = "/upload/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.saveFile(file, "audio");
        return ResponseEntity.ok("Audio enregistré avec succès : " + fileUrl);
    }

    // -------------------- Upload d’une vidéo --------------------
    @PostMapping(value = "/upload/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.saveFile(file, "video");
        return ResponseEntity.ok("Vidéo enregistrée avec succès : " + fileUrl);
    }

    // -------------------- Upload d’un logo --------------------
    @PostMapping(value = "/upload/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadLogo(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.saveFile(file, "logo");
        return ResponseEntity.ok("Logo enregistré avec succès : " + fileUrl);
    }

    // -------------------- Téléchargement --------------------
    @GetMapping("/{type}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String type, @PathVariable String filename) {
        Resource file = fileStorageService.loadFile(filename, type);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}