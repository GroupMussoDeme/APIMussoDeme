package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.entities.Contenu;
import com.mussodeme.MussoDeme.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // -------------------- Ajouter un audio --------------------
    @PostMapping("/{adminId}/audios")
    public ResponseEntity<Contenu> addAudio(
            @PathVariable Long adminId,
            @RequestPart("data") ContenuDTO dto,
            @RequestPart("audioFile") MultipartFile audioFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        Contenu audio = adminService.addAudio(adminId, dto, audioFile, imageFile);
        return ResponseEntity.ok(audio);
    }


    // -------------------- Supprimer un audio --------------------
    @DeleteMapping("/audios/{id}")
    public ResponseEntity<Void> deleteAudio(@PathVariable Long id) {
        adminService.deleteAudio(id);
        return ResponseEntity.noContent().build();
    }

}
