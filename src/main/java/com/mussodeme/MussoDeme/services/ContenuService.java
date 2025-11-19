package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.CategorieDTO;
import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Contenu;
import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.enums.TypeInfo;
import com.mussodeme.MussoDeme.enums.TypeCategorie;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.ContenuRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContenuService {

    private final ContenuRepository contenuRepo;
    private final AdminRepository userRepo;
    private final CategorieRepository categorieRepo;

    private final Path contenuStorage = Paths.get("uploads/contenus");

    // Constructor for dependency injection
    public ContenuService(ContenuRepository contenuRepo, AdminRepository userRepo, CategorieRepository categorieRepo) {
        this.contenuRepo = contenuRepo;
        this.userRepo = userRepo;
        this.categorieRepo = categorieRepo;
    }

    public ContenuDTO televerserContenu(MultipartFile file, String titre, String description, String duree, String typeInfo, String categorie, Long adminId) throws IOException {
        // Vérification admin
        Admin admin = userRepo.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        // Conversion de la chaîne en énumération TypeCategorie
        TypeCategorie typeCategorie = TypeCategorie.valueOf(categorie);

        //  Sauvegarde du fichier dans /uploads/
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        //  Création du contenu
        Contenu contenu = new Contenu();
        contenu.setTitre(titre);
        contenu.setDescription(description);
        contenu.setDuree(duree);
        contenu.setTypeInfo(TypeInfo.valueOf(typeInfo.trim().toUpperCase()));
        contenu.setUrlContenu(filePath.toString());
        contenu.setCategorie(typeCategorie);
        contenu.setAdmin(admin);

        Contenu saved = contenuRepo.save(contenu);

        // Retour DTO
        ContenuDTO dto = new ContenuDTO();
        dto.setId(saved.getId());
        dto.setTitre(saved.getTitre());
        dto.setDescription(saved.getDescription());
        dto.setDuree(saved.getDuree());
        dto.setUrlContenu(saved.getUrlContenu());
        dto.setTypeInfo(saved.getTypeInfo());
        dto.setTypeCategorie(categorie);
        dto.setAdminId(adminId);

        return dto;
    }

    // ------------------ LIST ------------------
    public List<ContenuDTO> listContenus() {
        return contenuRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ------------------ GET BY ID ------------------
    public ContenuDTO getContenu(Long id) {
        Contenu contenu = contenuRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Contenu introuvable"));
        return toDTO(contenu);
    }

    // ------------------ DOWNLOAD ------------------
    public Resource downloadContenu(Long id) throws MalformedURLException {
        Contenu contenu = contenuRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Contenu introuvable"));
        return new UrlResource(Paths.get(contenu.getUrlContenu()).toUri());
    }

    // ------------------ UPDATE ------------------
    public ContenuDTO updateContenu(Long id, ContenuDTO dto) {
        Contenu contenu = contenuRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Contenu introuvable"));

        contenu.setTitre(dto.getTitre());
        contenu.setDescription(dto.getDescription());
        contenu.setDuree(dto.getDuree());
        contenu.setTypeInfo(dto.getTypeInfo());

        // Optionnel : changer la catégorie
        if (dto.getTypeCategorie() != null) {
            TypeCategorie typeCategorie = TypeCategorie.valueOf(dto.getTypeCategorie());
            contenu.setCategorie(typeCategorie);
        }

        Contenu updated = contenuRepo.save(contenu);
        return toDTO(updated);
    }

    // ------------------ DELETE ------------------
    public void deleteContenu(Long id) throws IOException {
        Contenu contenu = contenuRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Contenu introuvable"));
        Path path = Paths.get(contenu.getUrlContenu());
        if (Files.exists(path)) Files.delete(path);
        contenuRepo.delete(contenu);
    }

    // ------------------ Mapper ------------------
    private ContenuDTO toDTO(Contenu contenu) {
        ContenuDTO dto = new ContenuDTO();
        dto.setId(contenu.getId());
        dto.setTitre(contenu.getTitre());
        dto.setDescription(contenu.getDescription());
        dto.setDuree(contenu.getDuree());
        dto.setUrlContenu(contenu.getUrlContenu());
        dto.setTypeInfo(contenu.getTypeInfo());
        dto.setAdminId(contenu.getAdmin().getId());
        dto.setTypeCategorie(contenu.getCategorie().name());
        
        return dto;
    }
}