#  Système SMS pour Femmes Rurales

##  Architecture

Le système utilise une approche hybride :
- **SMS** pour les femmes rurales (pas d'email)
- **Email** uniquement pour l'admin (rapports système)

##  Fonctionnalités SMS

### Messages Automatiques
1. **Inscription** - SMS de bienvenue
2. **Nouvelles Commandes** - Notification vendeur
3. **Paiements Reçus** - Confirmation vendeur
4. **Nouveaux Produits** - Notification membres
5. **Nouveaux Membres** - Notification coopérative
6. **Contenus Partagés** - Notification membres
7. **Activation/Désactivation Utilisateur** - Notification utilisateur
8. **Nouveaux Contenus** - Notification femmes rurales
9. **Nouvelles Institutions** - Notification femmes rurales

### Format des Messages
- **Concise et claire** - Adapté à l'illettrisme
- **En français** - Langue locale
- **Maximum 160 caractères** - SMS standard
- **Pas de caractères spéciaux** - Compatibilité universelle

##  Configuration

### Variables d'Environnement
```properties
# SMS CONFIGURATION - Africa's Talking pour développement
sms.api.url=https://api.africastalking.com/version1/messaging
sms.api.key=votre-api-key-africastalking
sms.sender.id=MussoDeme
```

### Fournisseurs SMS Recommandés (Mali)
1. **Africa's Talking** - Parfait pour développement en Afrique (gratuit)
2. **Orange Mali SMS API** - Meilleure couverture en production
3. **MTN Mali SMS API** - Coût compétitif en production

##  Intégration Technique

### Service SMSService
```java
@Service
public class SMSService {
    // Envoi de SMS avec formatage numéro Mali
    public void envoyerSMS(Utilisateur utilisateur, String message)
    
    // Messages prédéfinis pour chaque événement
    public void envoyerSMSNouvelleCommande(Commande commande)
    // ... autres méthodes
}
```

### Détection Automatique du Canal
```java
// Dans NotificationService
if (utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty()) {
    // Utilisateur avec email -> Email
    emailService.envoyerEmailNouvelleCommande(commande, utilisateur);
} else {
    // Femme rurale -> SMS
    smsService.envoyerSMSNouvelleCommande(commande);
}
```

##  Avantages pour Population Rurale

### Accessibilité
-  Tous ont des téléphones mobiles
-  Fonctionne sans internet
-  Pas besoin de compte email
-  Plus direct et immédiat

### Adaptation Culturelle
-  Messages en français local
-  Format court et simple
-  Pas de jargon technique
-  Adapté à l'illettrisme

### Fiabilité
-  Moins dépendant du réseau
-  Fonctionne en zone couverture limitée
-  Moins de points de défaillance
-  Coût prévisible

##  Exemple de Messages

### Bienvenue
```
Bienvenue dans MussoDeme Aminata ! Votre inscription est confirmée. Commencez à vendre et acheter.
```

### Nouvelle Commande
```
MussoDeme: Nouvelle commande de Fatou pour 'Savon traditionnel' (Qté: 2, Total: 3000 FCFA). Merci !
```

### Paiement Reçu
```
MussoDeme: Paiement de 3000 FCFA reçu via ORANGEMONEY pour 'Savon traditionnel'. Votre compte a été crédité.
```

### Activation/Désactivation Utilisateur
```
MussoDeme: Votre compte a été activé par l'administrateur AdminName. Contactez le support si nécessaire.
```

### Nouveau Contenu
```
MussoDeme: Nouveau contenu audio 'Santé: Nutrition' ajouté par AdminName. Écoutez/regardez-le !
```

### Nouvelle Institution
```
MussoDeme: Nouvelle institution 'Banque Agricole' ajoutée par AdminName. Découvrez ses services !
```

##  Gestion des Erreurs

### Envoi Échoué
- Retry automatique (3 tentatives)
- Log détaillé pour débogage
- Notification admin en cas d'échec critique

### Numéro Invalide
- Validation automatique format Mali
- Formatage automatique (+223)
- Message d'erreur clair

##  Coûts et Budget

### Estimation Mensuelle
- **1000 femmes rurales**
- **5 SMS/mois par femme**
- **Africa's Talking**: Gratuit pour développement
- **Orange Mali**: 10 FCFA/SMS → ~50,000 FCFA/mois (en production)

### Fournisseurs Recommandés
1. **Africa's Talking** - Développement (gratuit en Afrique)
2. **Orange Mali** - Production (meilleure couverture)
3. **MTN Mali** - Production (coût compétitif)

##  Évolutivité

### Capacité
- Supporte des milliers de SMS/jour
- File d'attente pour envoi différé
- Priorisation des messages critiques
- Archivage des logs d'envoi

### Monitoring
- Statistiques d'envoi en temps réel
- Taux de réussite des envois
- Alertes en cas de problèmes
- Rapports mensuels pour admin