package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.AudioConseilDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.AudioConseilService;
import org.springframework.core.io.Resource; // ✅ Correct
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioConseilController {

    private final AudioConseilService audioService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam Long adminId,
                                           @RequestParam Long categorieId,
                                           @RequestParam String titre,
                                           @RequestParam String langue,
                                           @RequestParam String description,
                                           @RequestParam String imageUrl,
                                           @RequestParam String duree) throws IOException {

        AudioConseilDTO dto = new AudioConseilDTO();
        dto.setAdminId(adminId);
        dto.setCategorieId(categorieId);
        dto.setTitre(titre);
        dto.setLangue(langue);
        dto.setDescription(description);
        dto.setImageUrl(imageUrl);
        dto.setDuree(duree);

        audioService.uploadAudio(file, dto);

        return ResponseEntity.ok(Response.builder()
                .status(201)
                .message("Audio uploadé avec succès")
                .build());
    }

    @GetMapping("/list")
    public ResponseEntity<List<AudioConseilDTO>> list() {
        return ResponseEntity.ok(audioService.listAudios());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource file = (Resource) audioService.downloadAudio(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}

