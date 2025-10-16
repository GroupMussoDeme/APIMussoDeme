package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.dto.TutoDTO;
import com.mussodeme.MussoDeme.services.TutoService;
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
@RequestMapping("/api/tutos")
@RequiredArgsConstructor
public class TutoController {

    private final TutoService tutoService;

    // ------------------ CREATE / UPLOAD ------------------
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam Long adminId,
                                           @RequestParam String titre,
                                           @RequestParam String langue,
                                           @RequestParam String description,
                                           @RequestParam String duree) throws IOException {

        TutoDTO dto = new TutoDTO();
        dto.setAdminId(adminId);
        dto.setTitre(titre);
        dto.setLangue(langue);
        dto.setDescription(description);
        dto.setDuree(duree);

        tutoService.uploadTuto(file, dto);

        return ResponseEntity.ok(Response.builder()
                .status(201)
                .message("Tuto uploadé avec succès")
                .build());
    }

    // ------------------ LIST ------------------
    @GetMapping("/list")
    public ResponseEntity<List<TutoDTO>> list() {
        return ResponseEntity.ok(tutoService.listTutos());
    }

    // ------------------ GET BY ID ------------------
    @GetMapping("/{id}")
    public ResponseEntity<TutoDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(tutoService.getTuto(id));
    }

    // ------------------ DOWNLOAD ------------------
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource file = tutoService.downloadTuto(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TutoDTO> update(@PathVariable Long id,
                                          @RequestBody TutoDTO dto) {
        return ResponseEntity.ok(tutoService.updateTuto(id, dto));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable Long id) throws IOException {
        tutoService.deleteTuto(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Tuto supprimé avec succès")
                .build());
    }
}
