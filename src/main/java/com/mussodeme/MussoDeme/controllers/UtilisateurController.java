package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.services.UtilisateurService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // Upload audio
    @PostMapping("/{id}/upload-audio")
    public String uploadAudio(@PathVariable Long id, @RequestParam("audio") MultipartFile file) {
        return utilisateurService.telechargerAudio(id, file);
    }

    // Upload vidéo
    @PostMapping("/{id}/upload-video")
    public String uploadVideo(@PathVariable Long id, @RequestParam("video") MultipartFile file) {
        return utilisateurService.telechargerVideo(id, file);
    }

    // Liste des audios
    @GetMapping("/audios")
    public List<String> getAudios() {
        return utilisateurService.listerAudios();
    }

    // Liste des vidéos
    @GetMapping("/videos")
    public List<String> getVideos() {
        return utilisateurService.listerVideos();
    }

    // Pictogrammes
    @GetMapping("/pictogrammes")
    public Map<String, String> getPictogrammes() {
        return utilisateurService.getPictogrammes();
    }

    // Navigation pictogramme
    @GetMapping("/{id}/naviguer")
    public String naviguer(@PathVariable Long id, @RequestParam String action) {
        return utilisateurService.naviguerParPictogramme(id, action);
    }
}
