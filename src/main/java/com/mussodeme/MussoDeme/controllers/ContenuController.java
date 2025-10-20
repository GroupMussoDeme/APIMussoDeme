package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.ContenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/audios")
@RequiredArgsConstructor
public class ContenuController {

    private final ContenuService audioService;

    // ------------------ CREATE / UPLOAD ------------------
    @PostMapping("/upload")
    public ResponseEntity<Response> upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam Long utilisateurId,
                                           @RequestParam Long categorieId,
                                           @RequestParam String titre,
                                           @RequestParam String langue,
                                           @RequestParam String description,
                                           @RequestParam String duree) throws IOException {

        ContenuDTO dto = new ContenuDTO();
        dto.setAdminId(dto.getAdminId());
        dto.setCategorieId(categorieId);
        dto.setTitre(titre);
        dto.setLangue(langue);
        dto.setDescription(description);
        dto.setDuree(duree);

        audioService.uploadAudio(file, dto);

        return ResponseEntity.ok(Response.builder()
                .status(201)
                .message("Audio uploadé avec succès")
                .build());
    }


    // ------------------ LIST ------------------
    @GetMapping("/list")
    public ResponseEntity<List<ContenuDTO>> list() {
        return ResponseEntity.ok(audioService.listAudios());
    }

    // ------------------ GET BY ID ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ContenuDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(audioService.getAudio(id));
    }

    // ------------------ DOWNLOAD ------------------
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource file = audioService.downloadAudio(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContenuDTO> update(@PathVariable Long id,
                                             @RequestBody ContenuDTO dto) {
        return ResponseEntity.ok(audioService.updateAudio(id, dto));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable Long id) throws IOException {
        audioService.deleteAudio(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Audio supprimé avec succès")
                .build());
    }
}
