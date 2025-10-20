package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.entities.Contenu;
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
    public ResponseEntity<List<Contenu>> getAudios() {
        return ResponseEntity.ok(utilisateurService.getAllAudios());
    }


    @GetMapping("/audio/{id}")
    public ResponseEntity<Contenu> getAudio(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getAudioById(id));
    }

}
