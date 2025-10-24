#  Système de Notifications et Emails

##  Configuration

### 1. Configuration SMTP dans `application.properties`

```properties
# EMAIL CONFIGURATION (SMTP)
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

# APPLICATION NAME
app.name=MussoDeme
```

### 2. Variables d'environnement

Vous pouvez définir ces variables d'environnement :
- `MAIL_USERNAME` : Votre adresse email
- `MAIL_PASSWORD` : Votre mot de passe d'application (pas le mot de passe normal)

##  Services Implémentés

### EmailService
Service responsable de l'envoi d'emails asynchrones pour :
- Nouvelles commandes
- Paiements reçus
- Nouveaux produits publiés
- Nouveaux membres dans coopératives
- Contenus partagés

### NotificationService
Service responsable de la gestion des notifications in-app et de l'envoi d'emails :
- Création de notifications
- Marquage comme lues
- Comptage des notifications non lues
- Suppression de notifications

##  Types de Notifications

1. **COMMANDE** - Nouvelle commande passée
2. **PAIEMENT** - Paiement reçu
3. **PUBLICATION** - Nouveau produit publié
4. **COOPERATIVE** - Nouveau membre dans coopérative
5. **CONTENU_PARTAGE** - Contenu partagé dans coopérative

##  Endpoints API

### Notifications
- `GET /api/notifications/utilisateur/{utilisateurId}` - Récupérer toutes les notifications
- `GET /api/notifications/utilisateur/{utilisateurId}/non-lues` - Récupérer les notifications non lues
- `GET /api/notifications/utilisateur/{utilisateurId}/count-non-lues` - Compter les notifications non lues
- `PUT /api/notifications/{notificationId}/marquer-lue` - Marquer une notification comme lue
- `PUT /api/notifications/utilisateur/{utilisateurId}/marquer-toutes-lues` - Marquer toutes les notifications comme lues
- `DELETE /api/notifications/{notificationId}` - Supprimer une notification

##  Intégration dans les Services

Les notifications sont automatiquement envoyées dans :
- `FemmeRuraleService.passerCommande()` - Notification au vendeur
- `FemmeRuraleService.payerCommande()` - Notification au vendeur
- `FemmeRuraleService.publierProduit()` - Notification aux membres intéressés
- `FemmeRuraleService.creerCooperative()` - Notification aux membres
- `FemmeRuraleService.partagerContenuDansCooperative()` - Notification aux membres de la coopérative

##  Configuration Gmail (Recommandé)

1. Activez la vérification en deux étapes sur votre compte Gmail
2. Générez un "mot de passe d'application" dans les paramètres de sécurité
3. Utilisez ce mot de passe d'application comme `MAIL_PASSWORD`

##  Personnalisation

Vous pouvez facilement personnaliser :
- Les templates d'emails dans `EmailService`
- Les types de notifications dans `TypeNotif`
- Les règles d'envoi dans `NotificationService`

##  Asynchrone

Tous les envois d'emails sont effectués de manière asynchrone pour ne pas bloquer les requêtes principales.