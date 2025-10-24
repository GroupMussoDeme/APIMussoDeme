package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.AppelVocalDTO;
import com.mussodeme.MussoDeme.entities.AppelVocal;
import com.mussodeme.MussoDeme.entities.Coperative;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.enums.StatutAppel;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AppelVocalRepository;
import com.mussodeme.MussoDeme.repository.CoperativeRepository;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AppelVocalService {
    
    private static final Logger logger = Logger.getLogger(AppelVocalService.class.getName());
    
    private final AppelVocalRepository appelVocalRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;
    private final CoperativeRepository cooperativeRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    
    // Constructor for dependency injection
    public AppelVocalService(AppelVocalRepository appelVocalRepository, 
                           FemmeRuraleRepository femmeRuraleRepository,
                           CoperativeRepository cooperativeRepository,
                           NotificationService notificationService,
                           ModelMapper modelMapper) {
        this.appelVocalRepository = appelVocalRepository;
        this.femmeRuraleRepository = femmeRuraleRepository;
        this.cooperativeRepository = cooperativeRepository;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }
    
    /**
     * Initier un appel vocal privé
     */
    @Transactional
    public AppelVocalDTO initierAppelPrive(Long appelantId, Long appeleId, String audioUrl) {
        logger.info("Initiation d'un appel vocal privé de " + appelantId + " vers " + appeleId);
        
        FemmeRurale appelant = femmeRuraleRepository.findById(appelantId)
                .orElseThrow(() -> new NotFoundException("Appelant non trouvé avec l'ID: " + appelantId));
        
        FemmeRurale appele = femmeRuraleRepository.findById(appeleId)
                .orElseThrow(() -> new NotFoundException("Appelé non trouvé avec l'ID: " + appeleId));
        
        AppelVocal appel = new AppelVocal();
        appel.setAppelant(appelant);
        appel.setAppele(appele);
        appel.setAudioUrl(audioUrl);
        appel.setDateAppel(LocalDateTime.now());
        appel.setStatut(StatutAppel.EN_COURS);
        appel.setEstAppelGroupe(false);
        
        AppelVocal saved = appelVocalRepository.save(appel);
        
        // Envoyer notification à l'appelé
        notificationService.creerNotification(appele, TypeNotif.INFO, 
            String.format("Appel vocal de %s", appelant.getNom()));
        
        logger.info("Appel vocal privé initié avec succès: ID " + saved.getId());
        
        return mapToDTO(saved);
    }
    
    /**
     * Initier un appel vocal de groupe dans une coopérative
     */
    @Transactional
    public AppelVocalDTO initierAppelGroupe(Long appelantId, Long cooperativeId, String audioUrl) {
        logger.info("Initiation d'un appel vocal de groupe dans la coopérative " + cooperativeId);
        
        FemmeRurale appelant = femmeRuraleRepository.findById(appelantId)
                .orElseThrow(() -> new NotFoundException("Appelant non trouvé avec l'ID: " + appelantId));
        
        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));
        
        AppelVocal appel = new AppelVocal();
        appel.setAppelant(appelant);
        appel.setCooperative(cooperative);
        appel.setAudioUrl(audioUrl);
        appel.setDateAppel(LocalDateTime.now());
        appel.setStatut(StatutAppel.EN_COURS);
        appel.setEstAppelGroupe(true);
        
        AppelVocal saved = appelVocalRepository.save(appel);
        
        // Envoyer notifications à tous les membres de la coopérative
        // TODO: Implémenter la notification aux membres de la coopérative
        
        logger.info("Appel vocal de groupe initié avec succès: ID " + saved.getId());
        
        return mapToDTO(saved);
    }
    
    /**
     * Répondre à un appel vocal
     */
    @Transactional
    public AppelVocalDTO repondreAppel(Long appelId, Long appeleId, String reponseAudioUrl) {
        logger.info("Réponse à l'appel vocal " + appelId);
        
        AppelVocal appel = appelVocalRepository.findById(appelId)
                .orElseThrow(() -> new NotFoundException("Appel non trouvé avec l'ID: " + appelId));
        
        // Vérifier que c'est bien l'appelé qui répond
        if (!appel.getAppele().getId().equals(appeleId)) {
            throw new IllegalArgumentException("Vous n'êtes pas l'appelé de cet appel");
        }
        
        appel.setStatut(StatutAppel.TERMINE);
        appel.setDateReponse(LocalDateTime.now());
        appel.setAudioUrl(reponseAudioUrl); // Mettre à jour avec la réponse
        
        AppelVocal saved = appelVocalRepository.save(appel);
        
        logger.info("Appel vocal " + appelId + " répondu avec succès");
        
        return mapToDTO(saved);
    }
    
    /**
     * Refuser un appel vocal
     */
    @Transactional
    public void refuserAppel(Long appelId, Long appeleId) {
        logger.info("Refus de l'appel vocal " + appelId);
        
        AppelVocal appel = appelVocalRepository.findById(appelId)
                .orElseThrow(() -> new NotFoundException("Appel non trouvé avec l'ID: " + appelId));
        
        // Vérifier que c'est bien l'appelé qui refuse
        if (!appel.getAppele().getId().equals(appeleId)) {
            throw new IllegalArgumentException("Vous n'êtes pas l'appelé de cet appel");
        }
        
        appel.setStatut(StatutAppel.REFUSE);
        appel.setDateReponse(LocalDateTime.now());
        
        appelVocalRepository.save(appel);
        
        logger.info("Appel vocal " + appelId + " refusé");
    }
    
    /**
     * Laisser un message vocal (messagerie vocale)
     */
    @Transactional
    public AppelVocalDTO laisserMessageVocal(Long appelantId, Long appeleId, String messageAudioUrl) {
        logger.info("Laisser un message vocal pour " + appeleId);
        
        FemmeRurale appelant = femmeRuraleRepository.findById(appelantId)
                .orElseThrow(() -> new NotFoundException("Appelant non trouvé avec l'ID: " + appelantId));
        
        FemmeRurale appele = femmeRuraleRepository.findById(appeleId)
                .orElseThrow(() -> new NotFoundException("Appelé non trouvé avec l'ID: " + appeleId));
        
        AppelVocal appel = new AppelVocal();
        appel.setAppelant(appelant);
        appel.setAppele(appele);
        appel.setMessageVocalUrl(messageAudioUrl);
        appel.setDateAppel(LocalDateTime.now());
        appel.setStatut(StatutAppel.MESSAGE_VOCALE);
        appel.setEstAppelGroupe(false);
        
        AppelVocal saved = appelVocalRepository.save(appel);
        
        // Envoyer notification à l'appelé
        notificationService.creerNotification(appele, TypeNotif.INFO, 
            String.format("Message vocal de %s", appelant.getNom()));
        
        logger.info("Message vocal laissé avec succès: ID " + saved.getId());
        
        return mapToDTO(saved);
    }
    
    /**
     * Récupérer les appels reçus par une femme
     */
    @Transactional(readOnly = true)
    public List<AppelVocalDTO> getAppelsRecus(Long femmeId) {
        logger.info("Récupération des appels reçus pour la femme " + femmeId);
        
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme non trouvée avec l'ID: " + femmeId));
        
        List<AppelVocal> appels = appelVocalRepository.findByAppeleAndStatutIn(
            femme, 
            List.of(StatutAppel.TERMINE, StatutAppel.MANQUE, StatutAppel.REFUSE, StatutAppel.MESSAGE_VOCALE)
        );
        
        return appels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les appels émis par une femme
     */
    @Transactional(readOnly = true)
    public List<AppelVocalDTO> getAppelsEmis(Long femmeId) {
        logger.info("Récupération des appels émis par la femme " + femmeId);
        
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme non trouvée avec l'ID: " + femmeId));
        
        List<AppelVocal> appels = appelVocalRepository.findByAppelant(femme);
        
        return appels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les appels d'une coopérative
     */
    @Transactional(readOnly = true)
    public List<AppelVocalDTO> getAppelsCooperative(Long cooperativeId) {
        logger.info("Récupération des appels de la coopérative " + cooperativeId);
        
        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));
        
        List<AppelVocal> appels = appelVocalRepository.findByCooperative(cooperative);
        
        return appels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Compter les appels manqués
     */
    @Transactional(readOnly = true)
    public Long compterAppelsManques(Long femmeId) {
        logger.info("Comptage des appels manqués pour la femme " + femmeId);
        
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme non trouvée avec l'ID: " + femmeId));
        
        return appelVocalRepository.countAppelsManques(femme);
    }
    
    // ==================== MAPPING ====================
    
    private AppelVocalDTO mapToDTO(AppelVocal appel) {
        AppelVocalDTO dto = modelMapper.map(appel, AppelVocalDTO.class);
        
        if (appel.getAppelant() != null) {
            dto.setAppelantId(appel.getAppelant().getId());
            dto.setAppelantNom(appel.getAppelant().getNom());
        }
        
        if (appel.getAppele() != null) {
            dto.setAppeleId(appel.getAppele().getId());
            dto.setAppeleNom(appel.getAppele().getNom());
        }
        
        if (appel.getCooperative() != null) {
            dto.setCooperativeId(appel.getCooperative().getId());
            dto.setCooperativeNom(appel.getCooperative().getNom());
        }
        
        return dto;
    }
}