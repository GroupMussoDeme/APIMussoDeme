package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.AudioConseilDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.AudioConseil;
import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.AudioConseilRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudioConseilService {

    private final AudioConseilRepository audioRepo;
    private final AdminRepository userRepo;
    private final CategorieRepository categorieRepo;

    private final Path audioStorage = Paths.get("uploads/audios");

    public AudioConseilDTO uploadAudio(MultipartFile file, AudioConseilDTO dto) throws IOException {
        try {
            if (!Files.exists(audioStorage)) Files.createDirectories(audioStorage);

            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetPath = audioStorage.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Utilisateur utilisateur = userRepo.findById(dto.getUtilisateurId())
                    .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

            Categorie categorie = categorieRepo.findById(dto.getCategorieId())
                    .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

            AudioConseil audio = AudioConseil.builder()
                    .titre(dto.getTitre())
                    .langue(dto.getLangue())
                    .description(dto.getDescription())
                    .duree(dto.getDuree())
                    .urlAudio(targetPath.toString())
                    .utilisateur(utilisateur)
                    .categorie(categorie)
                    .build();

            AudioConseil saved = audioRepo.save(audio);

            dto.setId(saved.getId());
            dto.setUrlAudio(saved.getUrlAudio());
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l’upload : " + e.getMessage(), e);
        }
    }



    // ------------------ LIST ------------------
    public List<AudioConseilDTO> listAudios() {
        return audioRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ------------------ GET BY ID ------------------
    public AudioConseilDTO getAudio(Long id) {
        AudioConseil audio = audioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio introuvable"));
        return toDTO(audio);
    }

    // ------------------ DOWNLOAD ------------------
    public Resource downloadAudio(Long id) throws MalformedURLException {
        AudioConseil audio = audioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio introuvable"));
        return new UrlResource(Paths.get(audio.getUrlAudio()).toUri());
    }

    // ------------------ UPDATE ------------------
    public AudioConseilDTO updateAudio(Long id, AudioConseilDTO dto) {
        AudioConseil audio = audioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio introuvable"));

        audio.setTitre(dto.getTitre());
        audio.setLangue(dto.getLangue());
        audio.setDescription(dto.getDescription());
        audio.setDuree(dto.getDuree());

        // Optionnel : changer la catégorie
        if (dto.getCategorieId() != null) {
            Categorie cat = categorieRepo.findById(dto.getCategorieId())
                    .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));
            audio.setCategorie(cat);
        }

        AudioConseil updated = audioRepo.save(audio);
        return toDTO(updated);
    }

    // ------------------ DELETE ------------------
    public void deleteAudio(Long id) throws IOException {
        AudioConseil audio = audioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio introuvable"));
        Path path = Paths.get(audio.getUrlAudio());
        if (Files.exists(path)) Files.delete(path);
        audioRepo.delete(audio);
    }

    // ------------------ Mapper ------------------
    private AudioConseilDTO toDTO(AudioConseil audio) {
        AudioConseilDTO dto = new AudioConseilDTO();
        dto.setId(audio.getId());
        dto.setTitre(audio.getTitre());
        dto.setLangue(audio.getLangue());
        dto.setDescription(audio.getDescription());
        dto.setDuree(audio.getDuree());
        dto.setUrlAudio(audio.getUrlAudio());
        dto.setUtilisateurId(audio.getUtilisateur().getId());
        dto.setCategorieId(audio.getCategorie().getId());
        return dto;
    }
}
