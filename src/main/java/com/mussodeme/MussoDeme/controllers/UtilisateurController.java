package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.entities.AudioConseil;
import com.mussodeme.MussoDeme.entities.Tuto;
import com.mussodeme.MussoDeme.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/audios")
    public ResponseEntity<List<AudioConseil>> getAudios() {
        return ResponseEntity.ok(utilisateurService.getAllAudios());
    }

    @GetMapping("/tutos")
    public ResponseEntity<List<Tuto>> getTutos() {
        return ResponseEntity.ok(utilisateurService.getAllTutos());
    }

    @GetMapping("/audio/{id}")
    public ResponseEntity<AudioConseil> getAudio(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getAudioById(id));
    }

    @GetMapping("/tuto/{id}")
    public ResponseEntity<Tuto> getTuto(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getTutoById(id));
    }
}
