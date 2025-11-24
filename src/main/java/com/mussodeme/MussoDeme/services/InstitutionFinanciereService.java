package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.InstitutionFinanciereDTO;
import com.mussodeme.MussoDeme.entities.InstitutionFinanciere;
import com.mussodeme.MussoDeme.repository.InstitutionFinanciereRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InstitutionFinanciereService {

    private final InstitutionFinanciereRepository repo;
    private final FileStorageService fileStorageService;

    public InstitutionFinanciereService(InstitutionFinanciereRepository repo,
                                        FileStorageService fileStorageService) {
        this.repo = repo;
        this.fileStorageService = fileStorageService;
    }

    // ✅ CREATE + UPLOAD LOGO
    public InstitutionFinanciereDTO create(InstitutionFinanciereDTO dto, MultipartFile logo) {

        String logoUrl = null;

        if (logo != null && !logo.isEmpty()) {
            logoUrl = fileStorageService.saveFile(logo, "logo");
        }

        InstitutionFinanciere inst = new InstitutionFinanciere(
                null,
                dto.getNom(),
                dto.getNumeroTel(),
                dto.getDescription(),
                logoUrl,
                dto.getMontantMin(),
                dto.getMontantMax(),
                dto.getSecteurActivite(),
                dto.getTauxInteret()
        );

        return mapToDTO(repo.save(inst));
    }

    // ✅ UPDATE (SANS LOGO)
    public InstitutionFinanciereDTO update(Long id, InstitutionFinanciereDTO dto) {
        InstitutionFinanciere inst = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution non trouvée"));

        inst.setNom(dto.getNom());
        inst.setNumeroTel(dto.getNumeroTel());
        inst.setDescription(dto.getDescription());
        inst.setMontantMin(dto.getMontantMin());
        inst.setMontantMax(dto.getMontantMax());
        inst.setSecteurActivite(dto.getSecteurActivite());
        inst.setTauxInteret(dto.getTauxInteret());

        return mapToDTO(repo.save(inst));
    }

    // ✅ LISTE
    public List<InstitutionFinanciereDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET BY ID
    public InstitutionFinanciereDTO findById(Long id) {
        return repo.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Institution introuvable"));
    }

    // ✅ DELETE
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ MAPPING ENTITY → DTO
    private InstitutionFinanciereDTO mapToDTO(InstitutionFinanciere i) {
        return new InstitutionFinanciereDTO(
                i.getId(),
                i.getNom(),
                i.getNumeroTel(),
                i.getDescription(),
                i.getLogoUrl(),
                i.getMontantMin(),
                i.getMontantMax(),
                i.getSecteurActivite(),
                i.getTauxInteret()
        );
    }
}
