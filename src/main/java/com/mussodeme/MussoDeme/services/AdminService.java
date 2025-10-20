package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.dto.InstitutionFinanciereDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.entities.Contenu;
import com.mussodeme.MussoDeme.entities.InstitutionFinanciere;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import com.mussodeme.MussoDeme.repository.ContenuRepository;
import com.mussodeme.MussoDeme.repository.InstitutionFinanciereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ContenuRepository contenuRepository;
    private final CategorieRepository categorieRepository;
    private final InstitutionFinanciereRepository institutionRepository;

    //Ajouter un contenu
    public ContenuDTO ajouterContenu(ContenuDTO dto) {
        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        Contenu contenu = new Contenu();
        contenu.setTitre(dto.getTitre());
        contenu.setLangue(dto.getLangue());
        contenu.setDescription(dto.getDescription());
        contenu.setUrlContenu(dto.getUrlContenu());
        contenu.setDuree(dto.getDuree());
        contenu.setCategorie(categorie);
        contenu.setAdmin(admin);

        Contenu saved = contenuRepository.save(contenu);
        dto.setId(saved.getId());
        return dto;
    }

    //Supprimer un contenu
    public void supprimerContenu(Long id) {
        if (!contenuRepository.existsById(id)) {
            throw new RuntimeException("Contenu introuvable");
        }
        contenuRepository.deleteById(id);
    }

    //Lister tous les contenus
    public List<ContenuDTO> listerContenus() {
        return contenuRepository.findAll().stream()
                .map(c -> new ContenuDTO(
                        c.getId(),
                        c.getTitre(),
                        c.getLangue(),
                        c.getDescription(),
                        c.getUrlContenu(),
                        c.getDuree(),
                        c.getAdmin().getId(),
                        c.getCategorie().getId()
                ))
                .collect(Collectors.toList());
    }

    //Ajouter une institution financière
    public InstitutionFinanciereDTO ajouterInstitution(InstitutionFinanciereDTO dto) {
        InstitutionFinanciere institution = new InstitutionFinanciere();
        institution.setNom(dto.getNom());
        institution.setNumeroTel(dto.getNumeroTel());
        institution.setDescription(dto.getDescription());
        institution.setLogoUrl(dto.getLogoUrl());

        InstitutionFinanciere saved = institutionRepository.save(institution);
        dto.setId(saved.getId());
        return dto;
    }

    //Supprimer une institution financière
    public void supprimerInstitution(Long id) {
        if (!institutionRepository.existsById(id)) {
            throw new RuntimeException("Institution introuvable");
        }
        institutionRepository.deleteById(id);
    }

    //Lister les institutions
    public List<InstitutionFinanciereDTO> listerInstitutions() {
        return institutionRepository.findAll().stream()
                .map(i -> new InstitutionFinanciereDTO(
                        i.getId(),
                        i.getNom(),
                        i.getNumeroTel(),
                        i.getDescription(),
                        i.getLogoUrl()
                ))
                .collect(Collectors.toList());
    }
}
