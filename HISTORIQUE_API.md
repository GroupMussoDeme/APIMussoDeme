#  API Historique - Tra avecabilité Complète

##  Vue d'ensemble

Le système d'**historique** permet de **tracer toutes les actions importantes** dans l'application MussoDeme. Chaque vente, achat, téléchargement et paiement est automatiquement enregistré pour assurer une traçabilité complète.

---

##  Types d'Historiques

| Type | Description | Enregistré lors de |
|------|-------------|-------------------|
| **HISTORIQUE_DES_VENTES** | Ventes de produits | Paiement d'une commande (côté vendeur) |
| **HISTORIQUE_DES_ACHATS** | Achats/Commandes | Passage d'une commande (côté acheteur) |
| **HISTORIQUE_DES_TELECHARGEMENTS** | Téléchargements de contenus | Écoute d'un audio ou visionnage d'une vidéo |
| **HISTORIQUE_DES_PAIEMENTS** | Paiements effectués | Paiement par Mobile Money |

---

##  Enregistrement Automatique

### 1️⃣ Historique des Téléchargements

**Quand ?** Lorsqu'une femme rurale écoute un audio ou regarde une vidéo

**Enregistré :**
```json
{
  "typeHistoriques": "HISTORIQUE_DES_TELECHARGEMENTS",
  "utilisateur": "Aminata Traoré",
  "entiteType": "Contenu",
  "entiteId": 123,
  "description": "Téléchargement/Écoute : Nutrition pour bébés (NUTRITION)",
  "dateAction": "2025-10-22T14:30:00"
}
```

### 2️⃣ Historique des Achats

**Quand ?** Lorsqu'une femme passe une commande

**Enregistré :**
```json
{
  "typeHistoriques": "HISTORIQUE_DES_ACHATS",
  "utilisateur": "Fatoumata Koné",
  "entiteType": "Commande",
  "entiteId": 456,
  "description": "Achat de 5 x Savon artisanal - 12,500 FCFA",
  "montant": 12500,
  "dateAction": "2025-10-22T15:00:00"
}
```

### 3️⃣ Historique des Paiements

**Quand ?** Lorsqu'un paiement Mobile Money est effectué

**Enregistré :**
```json
{
  "typeHistoriques": "HISTORIQUE_DES_PAIEMENTS",
  "utilisateur": "Fatoumata Koné",
  "entiteType": "Paiement",
  "entiteId": 789,
  "description": "Paiement MOBILE_MONEY - 12,500 FCFA (Commande #456)",
  "montant": 12500,
  "dateAction": "2025-10-22T15:05:00"
}
```

### 4️⃣ Historique des Ventes

**Quand ?** Lorsqu'une vente est finalisée (paiement reçu)

**Enregistré :**
```json
{
  "typeHistoriques": "HISTORIQUE_DES_VENTES",
  "utilisateur": "Aminata Traoré (vendeuse)",
  "entiteType": "Produit",
  "entiteId": 123,
  "description": "Vente de 5 x Savon artisanal - 12,500 FCFA",
  "montant": 12500,
  "dateAction": "2025-10-22T15:05:00"
}
```

---

##  Endpoints API

###  Pour les Utilisateurs (Femmes Rurales)

#### 1. Mes Historiques (Tous types)

```http
GET /api/historiques/utilisateur/{utilisateurId}
```

**Description :** Récupère tous les historiques de l'utilisateur (ventes, achats, téléchargements, paiements)

**Exemple :**
```http
GET /api/historiques/utilisateur/123
```

**Réponse :**
```json
{
  "statusCode": 200,
  "message": "45 historique(s) trouvé(s)",
  "data": [
    {
      "id": 1,
      "typeHistoriques": "HISTORIQUE_DES_ACHATS",
      "dateAction": "2025-10-22T15:00:00",
      "utilisateurId": 123,
      "utilisateurNom": "Koné",
      "utilisateurPrenom": "Fatoumata",
      "entiteId": 456,
      "entiteType": "Commande",
      "description": "Achat de 5 x Savon artisanal - 12,500 FCFA",
      "montant": 12500
    },
    // ... autres historiques
  ]
}
```

---

#### 2. Mes Historiques par Type

```http
GET /api/historiques/utilisateur/{utilisateurId}/type/{typeHistorique}
```

**Types disponibles :**
- `HISTORIQUE_DES_VENTES`
- `HISTORIQUE_DES_ACHATS`
- `HISTORIQUE_DES_TELECHARGEMENTS`
- `HISTORIQUE_DES_PAIEMENTS`

**Exemples :**

**Voir mes ventes :**
```http
GET /api/historiques/utilisateur/123/type/HISTORIQUE_DES_VENTES
```

**Voir mes achats :**
```http
GET /api/historiques/utilisateur/123/type/HISTORIQUE_DES_ACHATS
```

**Voir mes téléchargements :**
```http
GET /api/historiques/utilisateur/123/type/HISTORIQUE_DES_TELECHARGEMENTS
```

---

#### 3. Mes Historiques sur une Période

```http
GET /api/historiques/utilisateur/{utilisateurId}/periode
  ?dateDebut=2025-01-01T00:00:00
  &dateFin=2025-12-31T23:59:59
```

**Description :** Récupère les historiques entre deux dates

**Exemples :**

**Historiques du mois :**
```http
GET /api/historiques/utilisateur/123/periode
  ?dateDebut=2025-10-01T00:00:00
  &dateFin=2025-10-31T23:59:59
```

**Historiques de la semaine :**
```http
GET /api/historiques/utilisateur/123/periode
  ?dateDebut=2025-10-15T00:00:00
  &dateFin=2025-10-22T23:59:59
```

---

#### 4. Statistiques par Type

```http
GET /api/historiques/utilisateur/{utilisateurId}/stats/{typeHistorique}
```

**Description :** Compte le nombre d'actions par type

**Exemples :**

**Nombre total de ventes :**
```http
GET /api/historiques/utilisateur/123/stats/HISTORIQUE_DES_VENTES
```

**Réponse :**
```json
{
  "statusCode": 200,
  "message": "Statistiques récupérées",
  "data": {
    "utilisateurId": 123,
    "typeHistorique": "HISTORIQUE_DES_VENTES",
    "total": 15
  }
}
```

**Nombre de téléchargements :**
```http
GET /api/historiques/utilisateur/123/stats/HISTORIQUE_DES_TELECHARGEMENTS
```

---

### 👨‍💼 Pour les Administrateurs

#### 5. Tous les Historiques (ADMIN)

```http
GET /api/historiques/tous
```

**Description :** Récupère tous les historiques de tous les utilisateurs

**Autorisation :** `ROLE_ADMIN`

---

#### 6. Historiques Globaux par Type (ADMIN)

```http
GET /api/historiques/tous/type/{typeHistorique}
```

**Description :** Récupère tous les historiques d'un type spécifique (tous utilisateurs)

**Exemples :**

**Toutes les ventes :**
```http
GET /api/historiques/tous/type/HISTORIQUE_DES_VENTES
```

**Tous les paiements :**
```http
GET /api/historiques/tous/type/HISTORIQUE_DES_PAIEMENTS
```

---

###  Endpoint de Référence

#### 7. Types d'Historiques Disponibles

```http
GET /api/historiques/types
```

**Description :** Liste tous les types d'historiques avec descriptions

**Réponse :**
```json
{
  "statusCode": 200,
  "message": "Types d'historiques disponibles",
  "data": {
    "HISTORIQUE_DES_VENTES": "Historique des ventes de produits",
    "HISTORIQUE_DES_ACHATS": "Historique des achats/commandes",
    "HISTORIQUE_DES_TELECHARGEMENTS": "Historique des téléchargements de contenus",
    "HISTORIQUE_DES_PAIEMENTS": "Historique des paiements"
  }
}
```

---

##  Scénarios Audio (Interface Femmes Rurales)

### Scénario 1 : Consulter Mes Ventes

```
1.  Pictogramme "Mon Compte"
2.  Pictogramme "Historique"
3.  Pictogramme "Ventes"
4.  Audio : "Vous avez 15 ventes"
5.  "Vente 1 : 5 savons - 12,500 FCFA - Aujourd'hui à 15h"
6.  "Vente 2 : 3 beurres de karité - 9,000 FCFA - Hier"
   ...
```

### Scénario 2 : Voir Mes Achats

```
1.  Pictogramme "Historique"
2.  Pictogramme "Achats"
3.  "Vous avez 8 achats"
4.  "Achat 1 : 2 pagnes - 15,000 FCFA - 20 octobre"
5.  "Achat 2 : 1 panier - 5,000 FCFA - 18 octobre"
   ...
```

### Scénario 3 : Mes Contenus Écoutés

```
1.  Pictogramme "Historique"
2.  Pictogramme "Audio/Vidéo"
3.  "Vous avez écouté 23 contenus"
4.  "Contenu 1 : Nutrition bébés - Aujourd'hui"
5.  "Contenu 2 : Santé maternelle - Hier"
   ...
```

### Scénario 4 : Statistiques Vocales

```
1.  Pictogramme "Statistiques"
2.  "Vos statistiques :"
3.  "15 ventes pour 125,000 FCFA"
4.  "8 achats pour 80,000 FCFA"
5.  "23 contenus écoutés"
6.  "12 paiements effectués"
```

---

##  Structure de Données

### Entité Historique

```java
@Entity
public class Historique {
    private Long id;
    private TypeHistoriques typeHistoriques;  // Type d'action
    private LocalDateTime dateAction;         // Quand
    private Utilisateur utilisateur;          // Qui
    private Long entiteId;                    // ID de l'entité concernée
    private String entiteType;                // Type (Produit, Commande, etc.)
    private String description;               // Description détaillée
    private Double montant;                   // Montant (si applicable)
}
```

### Relations

```
Historique
├── Utilisateur (qui a fait l'action)
├── Entité concernée
│   ├── Produit (pour ventes)
│   ├── Commande (pour achats)
│   ├── Contenu (pour téléchargements)
│   └── Paiement (pour paiements)
└── Métadonnées
    ├── Type d'historique
    ├── Description
    ├── Montant
    └── Date/Heure
```

---

##  Sécurité

### Autorisations

| Endpoint | Rôles autorisés |
|----------|----------------|
| `/utilisateur/{id}/*` | `FEMME_RURALE`, `ADMIN` |
| `/tous/*` | `ADMIN` uniquement |
| `/types` | Tous (public) |

### Vérifications

 L'utilisateur ne peut consulter que **ses propres historiques**  
 Les admins peuvent consulter **tous les historiques**  
 Authentification JWT requise

---

##  Cas d'Usage Réels

### 1. Suivi des Ventes pour une Vendeuse

```
Aminata veut savoir combien elle a vendu ce mois-ci :
→ Consulte "Historique des ventes"
→ Filtre sur le mois d'octobre
→ Voit : 15 ventes pour 125,000 FCFA
→ Détails de chaque vente disponibles
```

### 2. Vérification des Achats

```
Fatoumata veut vérifier une commande :
→ Consulte "Historique des achats"
→ Retrouve : "Achat de 5 savons - 12,500 FCFA - 22 oct"
→ Peut vérifier le statut du paiement associé
```

### 3. Audit des Téléchargements

```
L'admin veut savoir quels contenus sont les plus écoutés :
→ Consulte tous les historiques de téléchargements
→ Voit : "Nutrition bébés" écouté 150 fois
→ Peut améliorer les contenus populaires
```

### 4. Rapports Financiers

```
Admin génère un rapport des paiements :
→ Filtre par type "HISTORIQUE_DES_PAIEMENTS"
→ Filtre par période (mois d'octobre)
→ Total : 2,500,000 FCFA de paiements
→ Statistiques par mode de paiement
```

---

##  Avantages

### Pour les Femmes Rurales
 **Traçabilité** : Historique complet de toutes leurs actions  
 **Transparence** : Vérification des ventes et achats  
 **Statistiques** : Suivi de leur activité  
 **Preuve** : Documentation de toutes les transactions

### Pour les Administrateurs
 **Audit complet** : Traçabilité de toutes les actions  
 **Rapports** : Génération de statistiques  
 **Analyse** : Contenus les plus consultés, produits les plus vendus  
 **Conformité** : Respect des obligations légales

---

##  Évolutions Futures

- [ ] Export des historiques en PDF/Excel
- [ ] Graphiques et statistiques visuelles
- [ ] Notifications sur événements importants
- [ ] Historique des modifications de profil
- [ ] Historique des partages dans coopératives
- [ ] Rapports automatiques mensuels
- [ ] Analyse prédictive des ventes

---

##  Résumé

| Type d'Historique | Enregistré quand ? | Informations capturées |
|-------------------|-------------------|------------------------|
| **VENTES** | Paiement reçu | Produit, Quantité, Montant |
| **ACHATS** | Commande passée | Produit, Quantité, Montant |
| **TÉLÉCHARGEMENTS** | Contenu écouté/regardé | Contenu, Type (SANTE/NUTRITION/etc.) |
| **PAIEMENTS** | Paiement effectué | Montant, Mode, Commande |

---

 **Système de traçabilité complet et opérationnel !**

Toutes les actions importantes sont automatiquement enregistrées et consultables via l'API.
