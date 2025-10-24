#  Système de Notifications et SMS - Configuration

##  Configuration

### 1. Compte Admin (Email)
- Email : `group2apicollabdev@gmail.com`
- Vérification en deux étapes activée
- Mot de passe d'application généré

### 2. Configuration dans `application.properties`

```properties
# EMAIL CONFIGURATION (Admin seulement)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=group2apicollabdev@gmail.com
spring.mail.password=votre-mot-de-passe-application
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

# SMS CONFIGURATION (Femmes rurales)
sms.api.url=${SMS_API_URL:https://api.sms-service.com/send}
sms.api.key=${SMS_API_KEY:your-api-key}
sms.sender.id=${SMS_SENDER_ID:MussoDeme}

# APPLICATION NAME
app.name=MussoDeme
```

##  Fonctionnalités Implémentées

### Notifications In-App
- Création automatique lors d'événements
- Marquage comme lu
- Suppression
- Filtrage par type
- **Appels vocaux** (nouveaux appels, messages vocaux)

### Notifications Externes
- **SMS** pour femmes rurales (pas d'email)
- **Email** pour admin uniquement (rapports système)

### SMS Automatiques
- Inscription (bienvenue)
- Nouvelles commandes
- Paiements reçus
- Nouveaux produits
- Nouveaux membres coopératives
- Contenus partagés
- **Appels vocaux manqués** (notification)

##  Types de Notifications

1. **INFO** - Informations générales
2. **ACHAT** - Achats effectués
3. **VENTE** - Ventes réalisées
4. **PAIEMENT** - Paiements reçus
5. **COMMANDE** - Nouvelles commandes
6. **COOPERATIVE** - Activité coopérative
7. **CONTENU_PARTAGE** - Contenus partagés
8. **PUBLICATION** - Nouveaux produits

##  Endpoints API

### Notifications Utilisateur
- `GET /api/notifications/utilisateur/{id}` - Toutes les notifications
- `GET /api/notifications/utilisateur/{id}/non-lues` - Non lues seulement
- `GET /api/notifications/utilisateur/{id}/type/{type}` - Par type spécifique
- `GET /api/notifications/utilisateur/{id}/count-non-lues` - Compter non lues

### Appels Vocaux
- `POST /api/appels/prive/{appeleId}` - Initier appel privé
- `POST /api/appels/groupe/{cooperativeId}` - Initier appel de groupe
- `PUT /api/appels/{id}/repondre` - Répondre à un appel
- `PUT /api/appels/{id}/refuser` - Refuser un appel
- `POST /api/appels/{appeleId}/message-vocal` - Laisser message vocal
- `GET /api/appels/recus` - Appels reçus
- `GET /api/appels/emis` - Appels émis
- `GET /api/appels/cooperative/{id}` - Appels d'une coopérative
- `GET /api/appels/manques/count` - Compter appels manqués

### Gestion des Notifications
- `PUT /api/notifications/{id}/marquer-lue` - Marquer une notification comme lue
- `PUT /api/notifications/utilisateur/{id}/marquer-toutes-lues` - Tout marquer comme lu
- `DELETE /api/notifications/{id}` - Supprimer une notification

##  Événements qui Déclenchent des Notifications

1. **Inscription** - SMS bienvenue + notification in-app (femmes rurales)
2. **Nouvelle Commande** - SMS au vendeur + notification in-app
3. **Paiement Reçu** - SMS au vendeur + notification in-app
4. **Publication Produit** - SMS aux membres intéressés + notification in-app
5. **Nouveau Membre Coopérative** - SMS aux autres membres + notification in-app
6. **Contenu Partagé** - SMS aux membres de la coopérative + notification in-app
7. **Appel Vocal** - Notifications aux destinataires
8. **Alertes Système** - Email à l'admin

##  Configuration Gmail (Étapes)

1. **Activer la vérification en deux étapes**
   - Aller dans Paramètres Gmail > Sécurité
   - Activer "Vérification en deux étapes"

2. **Générer un mot de passe d'application**
   - Dans Paramètres Gmail > Sécurité
   - Sélectionner "Mots de passe d'application"
   - Générer un nouveau mot de passe
   - Utiliser ce mot de passe dans `MAIL_PASSWORD`

##  Personnalisation

### Templates d'Emails
- Localisés en français
- Responsive design
- Informations contextuelles

### Messages de Notification
- Concis et informatifs
- Liés aux actions utilisateur
- Horodatés

##  Asynchrone

Tous les envois d'emails sont effectués de manière asynchrone :
- Ne bloque pas les requêtes principales
- Meilleure expérience utilisateur
- Gestion d'erreurs robuste

##  Support

En cas de problème d'envoi d'emails :
1. Vérifier le mot de passe d'application
2. Confirmer la vérification en deux étapes
3. Tester la configuration SMTP
4. Consulter les logs d'erreur