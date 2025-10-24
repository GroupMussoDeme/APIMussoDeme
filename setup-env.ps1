# ============================================
# MUSSODEME API - Script de Configuration
# ============================================
# Ce script aide à configurer les variables d'environnement pour le développement
# Exécution: .\setup-env.ps1

Write-Host " Configuration de l'environnement MussoDeme API" -ForegroundColor Cyan
Write-Host "================================================`n" -ForegroundColor Cyan

# Vérifier si le fichier .env existe
if (-Not (Test-Path ".env")) {
    Write-Host "  Fichier .env non trouvé!" -ForegroundColor Yellow
    
    if (Test-Path ".env.example") {
        $response = Read-Host "Voulez-vous créer .env depuis .env.example? (O/N)"
        if ($response -eq "O" -or $response -eq "o") {
            Copy-Item ".env.example" ".env"
            Write-Host " Fichier .env créé avec succès!" -ForegroundColor Green
            Write-Host "  IMPORTANT: Éditez le fichier .env avec vos vraies valeurs!`n" -ForegroundColor Yellow
        }
    } else {
        Write-Host " Fichier .env.example non trouvé!" -ForegroundColor Red
        exit 1
    }
}

# Fonction pour lire et charger les variables du fichier .env
function Load-EnvFile {
    param (
        [string]$FilePath = ".env"
    )
    
    if (Test-Path $FilePath) {
        Write-Host " Chargement des variables depuis $FilePath..." -ForegroundColor Cyan
        
        $envVars = @{}
        Get-Content $FilePath | ForEach-Object {
            $line = $_.Trim()
            
            # Ignorer les lignes vides et les commentaires
            if ($line -and -not $line.StartsWith("#")) {
                if ($line -match '^\s*([^=]+)=(.*)$') {
                    $key = $matches[1].Trim()
                    $value = $matches[2].Trim()
                    
                    # Enlever les guillemets si présents
                    $value = $value -replace '^["'']|["'']$', ''
                    
                    $envVars[$key] = $value
                    [System.Environment]::SetEnvironmentVariable($key, $value, 'Process')
                }
            }
        }
        
        return $envVars
    } else {
        Write-Host " Fichier $FilePath non trouvé!" -ForegroundColor Red
        return $null
    }
}

# Charger les variables
$envVars = Load-EnvFile

if ($envVars) {
    Write-Host "`n Variables d'environnement chargées avec succès!`n" -ForegroundColor Green
    
    # Afficher les variables chargées (masquer les valeurs sensibles)
    Write-Host " Variables configurées:" -ForegroundColor Cyan
    $envVars.GetEnumerator() | Sort-Object Name | ForEach-Object {
        $key = $_.Key
        $value = $_.Value
        
        # Masquer les valeurs sensibles
        $displayValue = $value
        if ($key -match "PASSWORD|SECRET|KEY") {
            $displayValue = "***" + $value.Substring([Math]::Max(0, $value.Length - 4))
        }
        
        Write-Host "   $key = $displayValue" -ForegroundColor Gray
    }
    
    Write-Host "`n" -ForegroundColor Cyan
    Write-Host " Prochaines étapes:" -ForegroundColor Yellow
    Write-Host "   1. Vérifiez que toutes les variables sont correctement définies" -ForegroundColor White
    Write-Host "   2. Lancez l'application avec: mvn spring-boot:run" -ForegroundColor White
    Write-Host "   3. L'API sera disponible sur http://localhost:$($envVars['SERVER_PORT'])`n" -ForegroundColor White
    
    # Proposer de générer un nouveau JWT secret
    Write-Host " Sécurité:" -ForegroundColor Cyan
    $generateSecret = Read-Host "Voulez-vous générer un nouveau JWT_SECRET sécurisé? (O/N)"
    
    if ($generateSecret -eq "O" -or $generateSecret -eq "o") {
        # Générer un secret aléatoire
        $bytes = New-Object Byte[] 64
        $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
        $rng.GetBytes($bytes)
        $newSecret = [Convert]::ToBase64String($bytes)
        
        Write-Host "`n Nouveau JWT_SECRET généré:" -ForegroundColor Green
        Write-Host "   $newSecret`n" -ForegroundColor Yellow
        Write-Host "  Copiez cette valeur dans votre fichier .env pour la variable JWT_SECRET" -ForegroundColor Yellow
        
        # Proposer de mettre à jour automatiquement
        $updateEnv = Read-Host "`nVoulez-vous mettre à jour .env automatiquement? (O/N)"
        if ($updateEnv -eq "O" -or $updateEnv -eq "o") {
            (Get-Content .env) | ForEach-Object {
                if ($_ -match '^JWT_SECRET=') {
                    "JWT_SECRET=$newSecret"
                } else {
                    $_
                }
            } | Set-Content .env
            
            Write-Host " Fichier .env mis à jour avec le nouveau secret!" -ForegroundColor Green
            [System.Environment]::SetEnvironmentVariable('JWT_SECRET', $newSecret, 'Process')
        }
    }
    
    Write-Host "`n Configuration terminée! Vous pouvez maintenant lancer l'application.`n" -ForegroundColor Green
    
} else {
    Write-Host " Impossible de charger les variables d'environnement!" -ForegroundColor Red
    exit 1
}

# Fonction helper pour afficher les commandes utiles
Write-Host " Commandes utiles:" -ForegroundColor Cyan
Write-Host "   - Lancer l'API:              mvn spring-boot:run" -ForegroundColor White
Write-Host "   - Compiler:                  mvn clean install" -ForegroundColor White
Write-Host "   - Compiler sans tests:       mvn clean install -DskipTests" -ForegroundColor White
Write-Host "   - Lancer les tests:          mvn test" -ForegroundColor White
Write-Host "   - Vérifier les problèmes:    mvn validate`n" -ForegroundColor White
