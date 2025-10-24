#  Partage de Contenus dans les Coopératives

##  Vue d'ensemble

Les femmes rurales peuvent **partager des contenus éducatifs** (audio, vidéo, photos, documents) dans leurs coopératives. Cela permet de diffuser des informations utiles sur la santé, la nutrition, le droit, etc.

---

##  Fonctionnalités de Partage

### 1. Partager un Contenu

#### Endpoint
```http
POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/partager
```

#### Paramètres
- `contenuId` (required) : ID du contenu à partager
- `messageAudioUrl` (optional) : Message vocal accompagnant le partage

#### Exemple d'utilisation
```http
POST /api/femmes-rurales/123/cooperatives/456/partager
  ?contenuId=789
  &messageAudioUrl=https://example.com/audio/mon-message.mp3
```

#### Message vocal d'accompagnement
La femme peut enregistrer un message vocal pour expliquer pourquoi elle partage ce contenu :
```
 "Écoutez cet audio sur la nutrition, c'est très utile pour nos bébés"
 "Cette vidéo explique comment obtenir un crédit, regardez-la"
 "Important ! Audio sur nos droits en tant que femmes"
```

#### Réponse (JSON)
```json
{
  "statusCode": 201,
  "message": "Contenu partagé avec succès dans la coopérative",
  "data": {
    "id": 1,
    "contenuId": 789,
    "contenuTitre": "Nutrition pour bébés",
    "contenuDescription": "Conseils nutritionnels...",
    "contenuMediaUrl": "https://example.com/audio/nutrition-bebes.mp3",
    "contenuTypeInfo": "NUTRITION",
    "cooperativeId": 456,
    "cooperativeNom": "Coopérative Karité Bamako",
    "partageParId": 123,
    "partageParNom": "Traoré",
    "partageParPrenom": "Aminata",
    "datePartage": "2025-10-22T14:30:00",
    "messageAudioUrl": "https://example.com/audio/mon-message.mp3"
  }
}
```

---

### 2. Voir les Contenus Partagés dans une Coopérative

#### Endpoint
```http
GET /api/femmes-rurales/cooperatives/{cooperativeId}/partages
```

#### Description
Retourne **tous les contenus** (audio, vidéo, photos) partagés dans la coopérative, triés du plus récent au plus ancien.

#### Exemple
```http
GET /api/femmes-rurales/cooperatives/456/partages
```

#### Types de contenus partagés
-  **Audios** : Informations santé, nutrition, droit
-  **Vidéos** : Formations professionnelles
-  **Photos** : Produits, événements
-  **Documents** : Guides, fiches pratiques

---

### 3. Filtrer les Partages par Type

#### Endpoint
```http
GET /api/femmes-rurales/cooperatives/{cooperativeId}/partages/type/{typeInfo}
```

#### Types disponibles
- `SANTE` - Contenus sur la santé
- `NUTRITION` - Conseils nutritionnels
- `DROIT` - Informations juridiques
- `VIDEO_FORMATION` - Vidéos de formation

#### Exemple
```http
GET /api/femmes-rurales/cooperatives/456/partages/type/SANTE
```

**Cas d'usage :**
```
 Pictogramme "Santé" → Liste des audios santé partagés
 Pictogramme "Nutrition" → Liste des contenus nutrition
 Pictogramme "Formation" → Liste des vidéos de formation
```

---

### 4. Voir Mes Partages

#### Endpoint
```http
GET /api/femmes-rurales/{femmeId}/mes-partages
```

#### Description
Retourne tous les contenus que cette femme a partagés dans ses différentes coopératives.

#### Exemple
```http
GET /api/femmes-rurales/123/mes-partages
```

#### Utilité
- Voir l'historique de mes partages
- Retrouver un contenu que j'ai partagé
- Savoir dans quelles coopératives j'ai partagé quoi

---

### 5. Supprimer un Partage

#### Endpoint
```http
DELETE /api/femmes-rurales/{femmeId}/partages/{partageId}
```

#### Description
Supprime un partage. **Seule la femme qui a partagé** peut supprimer son partage.

#### Exemple
```http
DELETE /api/femmes-rurales/123/partages/789
```

#### Sécurité
-  Vérification que c'est bien la femme qui a partagé
-  Erreur si une autre femme tente de supprimer

---

##  Scénarios d'Utilisation Audio

### Scénario 1 : Partage d'un Audio Santé

```
1.  Pictogramme "Mes Coopératives"
2.  Audio : "Vous avez 3 coopératives"
3.  Sélection "Coopérative Karité Bamako"
4.  Pictogramme "Partager"
5.  Pictogramme "Santé"
6.  Audio liste les contenus santé disponibles
7.  Sélection du contenu
8.  Enregistrement message : "Écoutez cet audio, c'est important"
9.  Audio confirmation : "Contenu partagé dans votre coopérative"
```

### Scénario 2 : Consulter les Partages

```
1.  Pictogramme "Ma Coopérative"
2.  Pictogramme "Contenus Partagés"
3.  Audio : "5 contenus ont été partagés"
4.  Pictogramme "Nutrition"
5.  Audio : "2 contenus nutrition"
   - "Aminata a partagé 'Nutrition bébés' il y a 2 heures"
   -  Message d'Aminata : "Écoutez cet audio..."
   -  Contenu : Audio nutrition
```

### Scénario 3 : Partage d'une Vidéo de Formation

```
1.  Pictogramme "Formations"
2.  Liste des vidéos de formation
3.  Sélection "Comment faire du savon"
4.  Pictogramme "Partager dans groupe"
5.  Sélection de la coopérative
6.  Enregistrement : "Regardez cette vidéo, on peut faire ça ensemble"
7.  Vidéo partagée dans la coopérative
```

---

##  Architecture Technique

### Entité PartageCooperative

```java
@Entity
public class PartageCooperative {
    private Long id;
    private Contenu contenu;           // Audio/Vidéo/Photo partagé
    private Coperative coperative;     // Groupe destinataire
    private FemmeRurale partagePar;    // Qui a partagé
    private LocalDateTime datePartage; // Quand
    private String messageAudioUrl;    // Message vocal optionnel
}
```

### Relations

```
PartageCooperative
├── Contenu (audio, vidéo, photo, document)
│   ├── TypeInfo (SANTE, NUTRITION, DROIT, VIDEO_FORMATION)
│   └── URL du média
├── Coopérative (groupe WhatsApp-like)
│   └── Membres (FemmeRurale)
└── FemmeRurale (qui a partagé)
    └── Message audio d'accompagnement
```

---

##  Règles de Sécurité

### Pour Partager
 Doit être **membre de la coopérative**  
 Le contenu doit **exister**  
 Authentifiée avec rôle `FEMME_RURALE`

### Pour Supprimer
 Doit être **celle qui a partagé**  
 Impossible de supprimer le partage d'une autre femme

---

##  Cas d'Usage Réels

### 1. Partage d'Info Santé Urgente
```
Une femme apprend une info importante sur la vaccination.
→ Elle partage l'audio dans sa coopérative
→ Toutes les membres reçoivent la notification
→ Elles écoutent son message + l'audio officiel
```

### 2. Formation Collective
```
Une vidéo sur la fabrication de savon artisanal est disponible.
→ La présidente de la coopérative la partage
→ Message : "Regardons cette vidéo ensemble samedi"
→ Formation collective organisée
```

### 3. Information Juridique
```
Nouvelle loi sur les droits des femmes.
→ Audio explicatif partagé dans plusieurs coopératives
→ Les femmes s'informent
→ Discussions vocales dans les groupes
```

### 4. Partage de Recettes Nutritionnelles
```
Audio sur la préparation de bouillie nutritive pour bébés.
→ Partagé dans coopérative de jeunes mamans
→ Message : "J'ai testé cette recette, ça marche bien"
→ Échanges d'expériences
```

---

##  Bénéfices

### Pour les Femmes Rurales
 **Accès à l'information** sans savoir lire  
 **Partage de connaissances** entre pairs  
 **Apprentissage collectif** dans les coopératives  
 **Autonomisation** par l'éducation audio

### Pour les Coopératives
 **Base de connaissances** partagée  
 **Solidarité** entre membres  
 **Formation continue** accessible  
 **Empowerment** collectif

---

##  Prochaines Évolutions

- [ ] Notifications push audio lors d'un nouveau partage
- [ ] Statistiques : contenus les plus partagés
- [ ] Réactions vocales aux partages
- [ ] Épingler les contenus importants
- [ ] Téléchargement offline des contenus partagés
- [ ] Traduction automatique audio (langues locales)

---

##  Notes Importantes

1. **Message audio optionnel** : Permet d'expliquer pourquoi on partage
2. **Pas de duplication** : Un même contenu peut être partagé plusieurs fois
3. **Tri chronologique** : Du plus récent au plus ancien
4. **Filtrage par type** : Pour retrouver facilement les contenus
5. **Suppression limitée** : Seule la partageuse peut supprimer

---

##  Exemples de Contenus Partageables

### Santé
-  Vaccination des enfants
-  Hygiène menstruelle
-  Prévention du paludisme
-  Planification familiale

### Nutrition
-  Recettes pour bébés
-  Conservation des aliments
-  Alimentation pendant la grossesse
-  Jardins nutritifs

### Droit
-  Droit à la propriété
-  Mariage et héritage
-  Violence conjugale
-  Accès au crédit

### Formations
-  Fabrication de savon
-  Teinture de tissus
-  Transformation du karité
-  Maraîchage

---

 **Fonctionnalité complète et opérationnelle !**
