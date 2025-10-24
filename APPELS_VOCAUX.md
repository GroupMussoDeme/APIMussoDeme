# Appels Vocaux Asynchrones

##  Architecture

Les appels vocaux fonctionnent de manière asynchrone, adaptée à l'environnement rural :
- **Pas de connexion en temps réel requise**
- **Fonctionne avec réseau intermittent**
- **Compatible avec interface audio existante**

##  Fonctionnalités

### Appels Privés
- Initiation d'appel entre deux femmes rurales
- Enregistrement vocal de la conversation
- Notifications automatiques
- Messagerie vocale si non répondu

### Appels de Groupe
- Appels à tous les membres d'une coopérative
- Partage d'informations vocales
- Historique des appels de groupe

### Gestion des Appels
- Répondre à un appel
- Refuser un appel
- Laisser un message vocal
- Historique des appels reçus/émis

##  Entités

### AppelVocal
```java
@Entity
public class AppelVocal {
    private Long id;
    private FemmeRurale appelant;
    private FemmeRurale appele;
    private Coperative cooperative;
    private String audioUrl;
    private LocalDateTime dateAppel;
    private Long dureeSecondes;
    private StatutAppel statut;
    private LocalDateTime dateReponse;
    private String messageVocalUrl;
    private boolean estAppelGroupe;
}
```

### StatutAppel (Enum)
- `EN_COURS` - Appel en cours d'émission
- `TERMINE` - Appel terminé avec succès
- `MANQUE` - Appel manqué (non répondu)
- `REFUSE` - Appel refusé
- `ANNULE` - Appel annulé par l'appelant
- `MESSAGE_VOCALE` - Message vocal laissé

##  Endpoints API

### Initiation d'Appels
```
POST /api/appels/prive/{appeleId}          # Appel privé
POST /api/appels/groupe/{cooperativeId}    # Appel de groupe
```

### Réponse aux Appels
```
PUT /api/appels/{id}/repondre              # Répondre
PUT /api/appels/{id}/refuser               # Refuser
```

### Messagerie Vocale
```
POST /api/appels/{appeleId}/message-vocal  # Laisser message
```

### Historique des Appels
```
GET /api/appels/recus                      # Appels reçus
GET /api/appels/emis                       # Appels émis
GET /api/appels/cooperative/{id}           # Appels coopérative
GET /api/appels/manques/count              # Compter manqués
```

##  Notifications

Les appels génèrent des notifications :
- **Appel entrant** - Notification in-app + email (optionnel)
- **Message vocal** - Notification in-app
- **Appel manqué** - Notification in-app + email

##  Intégration

### Avec le Système Existant
- Utilise les mêmes entités FemmeRurale/Coperative
- Intègre avec le système de notifications
- Compatible avec l'authentification JWT

### Stockage Audio
- URLs stockées dans la base de données
- Fichiers audio gérés par FileStorageService
- Supporte les formats audio compatibles mobile

##  Avantages pour Environnement Rural

### Fiabilité
-  Fonctionne sans connexion continue
-  Pas de perte de données réseau
-  Retry automatique possible

### Accessibilité
- Interface audio simple
- Pas de configuration complexe
- Compatible avec tous les appareils

### Coût
- Pas de serveur WebSocket coûteux
- Infrastructure existante réutilisée
- Faible consommation de bande passante

##  Exemple d'Utilisation

1. **Marie** veut appeler **Aminata**
2. Marie enregistre un message vocal d'intro
3. Système envoie notification à Aminata
4. Aminata reçoit l'appel et peut répondre
5. Conversation enregistrée et stockée
6. Historique disponible pour les deux

##  Sécurité

- Authentification JWT requise
- Vérification des permissions
- Accès uniquement aux membres de coopérative
- Protection contre spam d'appels

##  Évolutivité

- Supporte des milliers d'appels
- Indexation optimisée pour recherche
- Archivage possible des anciens appels
- Export des historiques