package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.dto.InstitutionFinanciereDTO;
import com.mussodeme.MussoDeme.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //Ajouter un contenu
    @PostMapping("/contenus")
    public ResponseEntity<ContenuDTO> ajouterContenu(@RequestBody ContenuDTO dto) {
        return ResponseEntity.ok(adminService.ajouterContenu(dto));
    }

    //Supprimer un contenu
    @DeleteMapping("/contenus/{id}")
    public ResponseEntity<String> supprimerContenu(@PathVariable Long id) {
        adminService.supprimerContenu(id);
        return ResponseEntity.ok("Contenu supprimé avec succès");
    }

    //Lister tous les contenus
    @GetMapping("/contenus")
    public ResponseEntity<List<ContenuDTO>> listerContenus() {
        return ResponseEntity.ok(adminService.listerContenus());
    }

    //Ajouter une institution
    @PostMapping("/institutions")
    public ResponseEntity<InstitutionFinanciereDTO> ajouterInstitution(@RequestBody InstitutionFinanciereDTO dto) {
        return ResponseEntity.ok(adminService.ajouterInstitution(dto));
    }

    //Supprimer une institution
    @DeleteMapping("/institutions/{id}")
    public ResponseEntity<String> supprimerInstitution(@PathVariable Long id) {
        adminService.supprimerInstitution(id);
        return ResponseEntity.ok("Institution supprimée avec succès");
    }

    //Lister toutes les institutions
    @GetMapping("/institutions")
    public ResponseEntity<List<InstitutionFinanciereDTO>> listerInstitutions() {
        return ResponseEntity.ok(adminService.listerInstitutions());
    }
}
