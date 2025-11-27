package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    // Generic
    private int status;
    private String message;
    private Object data;

    // For login/auth (si tu gères l’authentification)
    private String token;
    private String role;
    private String expirationTime;

    // For pagination
    private Integer totalPages;
    private Long totalElements;

    // Data output optional
    private UtilisateurDTO utilisateur;
    private List<UtilisateurDTO> utilisateurs;

    private FemmeRuraleDTO femmeRurale;
    private List<FemmeRuraleDTO> femmesRurales;

    private CoperativeDTO coperative;
    private List<CoperativeDTO> coperatives;

    private ContenuDTO audioConseil;
    private List<ContenuDTO> audioConseils;

    private ProduitDTO produit;
    private List<ProduitDTO> produits;

    private CommandeDTO commande;
    private List<CommandeDTO> commandes;

    private PaiementDTO paiement;
    private List<PaiementDTO> paiements;

    private NotificationDTO notification;
    private List<NotificationDTO> notifications;

    private HistoriqueDTO historique;
    private List<HistoriqueDTO> historiques;

    private UtilisateurAudioDTO utilisateurAudio;
    private List<UtilisateurAudioDTO> utilisateurAudios;

    private RechercheParLocalisationDTO rechercheParLocalisation;
    private List<RechercheParLocalisationDTO> recherchesParLocalisation;

    private final LocalDateTime timestamp = LocalDateTime.now();

    // Default constructor
    public Response() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    // Private constructor for builder pattern
    private Response(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.token = builder.token;
        this.role = builder.role;
        this.expirationTime = builder.expirationTime;
        this.totalPages = builder.totalPages;
        this.totalElements = builder.totalElements;
        this.utilisateur = builder.utilisateur;
        this.utilisateurs = builder.utilisateurs;
        this.femmeRurale = builder.femmeRurale;
        this.femmesRurales = builder.femmesRurales;
        this.coperative = builder.coperative;
        this.coperatives = builder.coperatives;
        this.audioConseil = builder.audioConseil;
        this.audioConseils = builder.audioConseils;
        this.produit = builder.produit;
        this.produits = builder.produits;
        this.commande = builder.commande;
        this.commandes = builder.commandes;
        this.paiement = builder.paiement;
        this.paiements = builder.paiements;
        this.notification = builder.notification;
        this.notifications = builder.notifications;
        this.historique = builder.historique;
        this.historiques = builder.historiques;
        this.utilisateurAudio = builder.utilisateurAudio;
        this.utilisateurAudios = builder.utilisateurAudios;
        this.rechercheParLocalisation = builder.rechercheParLocalisation;
        this.recherchesParLocalisation = builder.recherchesParLocalisation;
        this.data = builder.data;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<UtilisateurDTO> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<UtilisateurDTO> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public FemmeRuraleDTO getFemmeRurale() {
        return femmeRurale;
    }

    public void setFemmeRurale(FemmeRuraleDTO femmeRurale) {
        this.femmeRurale = femmeRurale;
    }

    public List<FemmeRuraleDTO> getFemmesRurales() {
        return femmesRurales;
    }

    public void setFemmesRurales(List<FemmeRuraleDTO> femmesRurales) {
        this.femmesRurales = femmesRurales;
    }

    public CoperativeDTO getCoperative() {
        return coperative;
    }

    public void setCoperative(CoperativeDTO coperative) {
        this.coperative = coperative;
    }

    public List<CoperativeDTO> getCoperatives() {
        return coperatives;
    }

    public void setCoperatives(List<CoperativeDTO> coperatives) {
        this.coperatives = coperatives;
    }

    public ContenuDTO getAudioConseil() {
        return audioConseil;
    }

    public void setAudioConseil(ContenuDTO audioConseil) {
        this.audioConseil = audioConseil;
    }

    public List<ContenuDTO> getAudioConseils() {
        return audioConseils;
    }

    public void setAudioConseils(List<ContenuDTO> audioConseils) {
        this.audioConseils = audioConseils;
    }

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    public List<ProduitDTO> getProduits() {
        return produits;
    }

    public void setProduits(List<ProduitDTO> produits) {
        this.produits = produits;
    }

    public CommandeDTO getCommande() {
        return commande;
    }

    public void setCommande(CommandeDTO commande) {
        this.commande = commande;
    }

    public List<CommandeDTO> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<CommandeDTO> commandes) {
        this.commandes = commandes;
    }

    public PaiementDTO getPaiement() {
        return paiement;
    }

    public void setPaiement(PaiementDTO paiement) {
        this.paiement = paiement;
    }

    public List<PaiementDTO> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<PaiementDTO> paiements) {
        this.paiements = paiements;
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public HistoriqueDTO getHistorique() {
        return historique;
    }

    public void setHistorique(HistoriqueDTO historique) {
        this.historique = historique;
    }

    public List<HistoriqueDTO> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<HistoriqueDTO> historiques) {
        this.historiques = historiques;
    }

    public UtilisateurAudioDTO getUtilisateurAudio() {
        return utilisateurAudio;
    }

    public void setUtilisateurAudio(UtilisateurAudioDTO utilisateurAudio) {
        this.utilisateurAudio = utilisateurAudio;
    }

    public List<UtilisateurAudioDTO> getUtilisateurAudios() {
        return utilisateurAudios;
    }

    public void setUtilisateurAudios(List<UtilisateurAudioDTO> utilisateurAudios) {
        this.utilisateurAudios = utilisateurAudios;
    }

    public RechercheParLocalisationDTO getRechercheParLocalisation() {
        return rechercheParLocalisation;
    }

    public void setRechercheParLocalisation(RechercheParLocalisationDTO rechercheParLocalisation) {
        this.rechercheParLocalisation = rechercheParLocalisation;
    }

    public List<RechercheParLocalisationDTO> getRecherchesParLocalisation() {
        return recherchesParLocalisation;
    }

    public void setRecherchesParLocalisation(List<RechercheParLocalisationDTO> recherchesParLocalisation) {
        this.recherchesParLocalisation = recherchesParLocalisation;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Builder pattern
    public static class Builder {
        private int status;
        private String message;
        private String token;
        private String role;
        private String expirationTime;
        private Integer totalPages;
        private Long totalElements;
        private UtilisateurDTO utilisateur;
        private List<UtilisateurDTO> utilisateurs;
        private FemmeRuraleDTO femmeRurale;
        private List<FemmeRuraleDTO> femmesRurales;
        private CoperativeDTO coperative;
        private List<CoperativeDTO> coperatives;
        private ContenuDTO audioConseil;
        private List<ContenuDTO> audioConseils;
        private ProduitDTO produit;
        private List<ProduitDTO> produits;
        private CommandeDTO commande;
        private List<CommandeDTO> commandes;
        private PaiementDTO paiement;
        private List<PaiementDTO> paiements;
        private NotificationDTO notification;
        private List<NotificationDTO> notifications;
        private HistoriqueDTO historique;
        private List<HistoriqueDTO> historiques;
        private UtilisateurAudioDTO utilisateurAudio;
        private List<UtilisateurAudioDTO> utilisateurAudios;
        private RechercheParLocalisationDTO rechercheParLocalisation;
        private List<RechercheParLocalisationDTO> recherchesParLocalisation;
        private Object data;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        // Adding responseCode as an alias for status
        public Builder responseCode(String responseCode) {
            try {
                this.status = Integer.parseInt(responseCode);
            } catch (NumberFormatException e) {
                this.status = 0;
            }
            return this;
        }

        // Adding statusCode as an alias for status
        public Builder statusCode(int statusCode) {
            this.status = statusCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        // Adding responseMessage as an alias for message
        public Builder responseMessage(String responseMessage) {
            this.message = responseMessage;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder expirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public Builder totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder totalElements(Long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder utilisateur(UtilisateurDTO utilisateur) {
            this.utilisateur = utilisateur;
            return this;
        }

        public Builder utilisateurs(List<UtilisateurDTO> utilisateurs) {
            this.utilisateurs = utilisateurs;
            return this;
        }

        public Builder femmeRurale(FemmeRuraleDTO femmeRurale) {
            this.femmeRurale = femmeRurale;
            return this;
        }

        public Builder femmesRurales(List<FemmeRuraleDTO> femmesRurales) {
            this.femmesRurales = femmesRurales;
            return this;
        }

        public Builder coperative(CoperativeDTO coperative) {
            this.coperative = coperative;
            return this;
        }

        public Builder coperatives(List<CoperativeDTO> coperatives) {
            this.coperatives = coperatives;
            return this;
        }

        public Builder audioConseil(ContenuDTO audioConseil) {
            this.audioConseil = audioConseil;
            return this;
        }

        public Builder audioConseils(List<ContenuDTO> audioConseils) {
            this.audioConseils = audioConseils;
            return this;
        }

        public Builder produit(ProduitDTO produit) {
            this.produit = produit;
            return this;
        }

        public Builder produits(List<ProduitDTO> produits) {
            this.produits = produits;
            return this;
        }

        public Builder commande(CommandeDTO commande) {
            this.commande = commande;
            return this;
        }

        public Builder commandes(List<CommandeDTO> commandes) {
            this.commandes = commandes;
            return this;
        }

        public Builder paiement(PaiementDTO paiement) {
            this.paiement = paiement;
            return this;
        }

        public Builder paiements(List<PaiementDTO> paiements) {
            this.paiements = paiements;
            return this;
        }

        public Builder notification(NotificationDTO notification) {
            this.notification = notification;
            return this;
        }

        public Builder notifications(List<NotificationDTO> notifications) {
            this.notifications = notifications;
            return this;
        }

        public Builder historique(HistoriqueDTO historique) {
            this.historique = historique;
            return this;
        }

        public Builder historiques(List<HistoriqueDTO> historiques) {
            this.historiques = historiques;
            return this;
        }

        public Builder utilisateurAudio(UtilisateurAudioDTO utilisateurAudio) {
            this.utilisateurAudio = utilisateurAudio;
            return this;
        }

        public Builder utilisateurAudios(List<UtilisateurAudioDTO> utilisateurAudios) {
            this.utilisateurAudios = utilisateurAudios;
            return this;
        }

        public Builder rechercheParLocalisation(RechercheParLocalisationDTO rechercheParLocalisation) {
            this.rechercheParLocalisation = rechercheParLocalisation;
            return this;
        }

        public Builder recherchesParLocalisation(List<RechercheParLocalisationDTO> recherchesParLocalisation) {
            this.recherchesParLocalisation = recherchesParLocalisation;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder data(Object data) {
            // ✅ On stocke toujours l'objet brut ici, quel que soit son type
            this.data = data;

            // Puis on garde ton comportement existant pour typer automatiquement
            if (data instanceof UtilisateurDTO) {
                this.utilisateur = (UtilisateurDTO) data;
            } else if (data instanceof List) {
                List<?> dataList = (List<?>) data;
                if (!dataList.isEmpty()) {
                    Object firstItem = dataList.get(0);
                    if (firstItem instanceof UtilisateurDTO) {
                        this.utilisateurs = (List<UtilisateurDTO>) dataList;
                    } else if (firstItem instanceof FemmeRuraleDTO) {
                        this.femmesRurales = (List<FemmeRuraleDTO>) dataList;
                    } else if (firstItem instanceof CoperativeDTO) {
                        this.coperatives = (List<CoperativeDTO>) dataList;
                    } else if (firstItem instanceof ContenuDTO) {
                        this.audioConseils = (List<ContenuDTO>) dataList;
                    } else if (firstItem instanceof ProduitDTO) {
                        this.produits = (List<ProduitDTO>) dataList;
                    } else if (firstItem instanceof CommandeDTO) {
                        this.commandes = (List<CommandeDTO>) dataList;
                    } else if (firstItem instanceof PaiementDTO) {
                        this.paiements = (List<PaiementDTO>) dataList;
                    } else if (firstItem instanceof NotificationDTO) {
                        this.notifications = (List<NotificationDTO>) dataList;
                    } else if (firstItem instanceof HistoriqueDTO) {
                        this.historiques = (List<HistoriqueDTO>) dataList;
                    } else if (firstItem instanceof UtilisateurAudioDTO) {
                        this.utilisateurAudios = (List<UtilisateurAudioDTO>) dataList;
                    } else if (firstItem instanceof RechercheParLocalisationDTO) {
                        this.recherchesParLocalisation = (List<RechercheParLocalisationDTO>) dataList;
                    }
                }
            } else if (data instanceof FemmeRuraleDTO) {
                this.femmeRurale = (FemmeRuraleDTO) data;
            } else if (data instanceof CoperativeDTO) {
                this.coperative = (CoperativeDTO) data;
            } else if (data instanceof ContenuDTO) {
                this.audioConseil = (ContenuDTO) data;
            } else if (data instanceof ProduitDTO) {
                this.produit = (ProduitDTO) data;
            } else if (data instanceof CommandeDTO) {
                this.commande = (CommandeDTO) data;
            } else if (data instanceof PaiementDTO) {
                this.paiement = (PaiementDTO) data;
            } else if (data instanceof NotificationDTO) {
                this.notification = (NotificationDTO) data;
            } else if (data instanceof HistoriqueDTO) {
                this.historique = (HistoriqueDTO) data;
            } else if (data instanceof UtilisateurAudioDTO) {
                this.utilisateurAudio = (UtilisateurAudioDTO) data;
            } else if (data instanceof RechercheParLocalisationDTO) {
                this.rechercheParLocalisation = (RechercheParLocalisationDTO) data;
            } else if (data instanceof Long) {
                this.totalElements = (Long) data;
            }
            // ⚠️ Si c’est un Map<String, Object> (cas de l’upload image),
            // on ne fait rien de plus : il reste dans this.data et sera
            // sérialisé tel quel sous la propriété "data".
            return this;
        }


        public Response build() {
            return new Response(this);
        }
    }

    // Static builder method
    public static Builder builder() {
        return new Builder();
    }
}