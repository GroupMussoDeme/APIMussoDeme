#!/bin/bash

# ============================================
# MUSSODEME API - Script de Configuration
# ============================================
# Ce script aide à configurer les variables d'environnement pour le développement
# Exécution: chmod +x setup-env.sh && ./setup-env.sh

set -e

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

echo -e "${CYAN} Configuration de l'environnement MussoDeme API${NC}"
echo -e "${CYAN}================================================${NC}\n"

# Vérifier si le fichier .env existe
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}  Fichier .env non trouvé!${NC}"
    
    if [ -f ".env.example" ]; then
        read -p "Voulez-vous créer .env depuis .env.example? (o/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Oo]$ ]]; then
            cp .env.example .env
            echo -e "${GREEN} Fichier .env créé avec succès!${NC}"
            echo -e "${YELLOW}  IMPORTANT: Éditez le fichier .env avec vos vraies valeurs!${NC}\n"
        fi
    else
        echo -e "${RED} Fichier .env.example non trouvé!${NC}"
        exit 1
    fi
fi

# Fonction pour charger les variables du fichier .env
load_env_file() {
    local env_file="${1:-.env}"
    
    if [ -f "$env_file" ]; then
        echo -e "${CYAN} Chargement des variables depuis $env_file...${NC}"
        
        # Charger les variables (ignorer les commentaires et lignes vides)
        while IFS='=' read -r key value; do
            # Nettoyer la clé et la valeur
            key=$(echo "$key" | xargs)
            value=$(echo "$value" | xargs)
            
            # Ignorer les lignes vides et les commentaires
            if [ -n "$key" ] && [[ ! "$key" =~ ^#.* ]]; then
                # Enlever les guillemets si présents
                value="${value%\"}"
                value="${value#\"}"
                value="${value%\'}"
                value="${value#\'}"
                
                # Exporter la variable
                export "$key=$value"
                
                # Masquer les valeurs sensibles pour l'affichage
                display_value="$value"
                if [[ "$key" =~ PASSWORD|SECRET|KEY ]]; then
                    display_value="***${value: -4}"
                fi
                
                echo -e "   ${WHITE}$key${NC} = ${display_value}"
            fi
        done < "$env_file"
        
        return 0
    else
        echo -e "${RED} Fichier $env_file non trouvé!${NC}"
        return 1
    fi
}

# Charger les variables
if load_env_file; then
    echo -e "\n${GREEN} Variables d'environnement chargées avec succès!${NC}\n"
    
    echo -e "${CYAN} Prochaines étapes:${NC}"
    echo -e "   ${WHITE}1. Vérifiez que toutes les variables sont correctement définies${NC}"
    echo -e "   ${WHITE}2. Lancez l'application avec: mvn spring-boot:run${NC}"
    echo -e "   ${WHITE}3. L'API sera disponible sur http://localhost:${SERVER_PORT:-5500}${NC}\n"
    
    # Proposer de générer un nouveau JWT secret
    echo -e "${CYAN} Sécurité:${NC}"
    read -p "Voulez-vous générer un nouveau JWT_SECRET sécurisé? (o/n) " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Oo]$ ]]; then
        # Générer un secret aléatoire avec openssl
        if command -v openssl &> /dev/null; then
            new_secret=$(openssl rand -base64 64 | tr -d '\n')
            echo -e "\n${GREEN} Nouveau JWT_SECRET généré:${NC}"
            echo -e "   ${YELLOW}$new_secret${NC}\n"
            echo -e "${YELLOW}  Copiez cette valeur dans votre fichier .env pour la variable JWT_SECRET${NC}"
            
            # Proposer de mettre à jour automatiquement
            read -p "Voulez-vous mettre à jour .env automatiquement? (o/n) " -n 1 -r
            echo
            
            if [[ $REPLY =~ ^[Oo]$ ]]; then
                # Créer une backup
                cp .env .env.backup
                
                # Mettre à jour le fichier
                if [[ "$OSTYPE" == "darwin"* ]]; then
                    # macOS
                    sed -i '' "s|^JWT_SECRET=.*|JWT_SECRET=$new_secret|" .env
                else
                    # Linux
                    sed -i "s|^JWT_SECRET=.*|JWT_SECRET=$new_secret|" .env
                fi
                
                echo -e "${GREEN} Fichier .env mis à jour avec le nouveau secret!${NC}"
                echo -e "${CYAN}  Une sauvegarde a été créée: .env.backup${NC}"
                
                # Recharger la variable
                export JWT_SECRET="$new_secret"
            fi
        else
            echo -e "${RED} openssl n'est pas installé. Impossible de générer un secret.${NC}"
        fi
    fi
    
    echo -e "\n${GREEN} Configuration terminée! Vous pouvez maintenant lancer l'application.${NC}\n"
    
    # Afficher les commandes utiles
    echo -e "${CYAN} Commandes utiles:${NC}"
    echo -e "   ${WHITE}- Lancer l'API:              mvn spring-boot:run${NC}"
    echo -e "   ${WHITE}- Compiler:                  mvn clean install${NC}"
    echo -e "   ${WHITE}- Compiler sans tests:       mvn clean install -DskipTests${NC}"
    echo -e "   ${WHITE}- Lancer les tests:          mvn test${NC}"
    echo -e "   ${WHITE}- Vérifier les problèmes:    mvn validate${NC}\n"
    
    # Créer un script pour sourcer les variables facilement
    cat > load-env.sh << 'EOF'
#!/bin/bash
# Chargement automatique des variables d'environnement
if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
    echo " Variables d'environnement chargées depuis .env"
else
    echo " Fichier .env non trouvé!"
fi
EOF
    chmod +x load-env.sh
    
    echo -e "${CYAN} Astuce: Utilisez ${WHITE}source ./load-env.sh${CYAN} pour charger rapidement les variables${NC}\n"
    
else
    echo -e "${RED} Impossible de charger les variables d'environnement!${NC}"
    exit 1
fi
