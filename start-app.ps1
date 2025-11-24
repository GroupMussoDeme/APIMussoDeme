# Script pour démarrer l'application MussoDeme
Write-Host "Démarrage de l'application MussoDeme API..."
Write-Host "Vérification de l'environnement..."

# Charger les propriétés de configuration
$config = @{}
if (Test-Path "startup.properties") {
    Get-Content "startup.properties" | ForEach-Object {
        if ($_ -match "^([^#].+?)=(.*)") {
            $config[$matches[1]] = $matches[2]
        }
    }
}

# Afficher la configuration
Write-Host "Configuration chargée:"
$config.GetEnumerator() | ForEach-Object { Write-Host "  $($_.Key) = $($_.Value)" }

# Vérifier si le fichier mvnw existe
if (Test-Path ".\mvnw") {
    Write-Host "Fichier mvnw trouvé. Démarrage de l'application..."
    try {
        # Définir les variables d'environnement
        $env:DB_URL = "jdbc:mysql://$($config['DB_HOST']):$($config['DB_PORT'])/$($config['DB_NAME'])?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
        $env:DB_USERNAME = $config['DB_USER']
        $env:DB_PASSWORD = $config['DB_PASSWORD']
        $env:SERVER_PORT = $config['SERVER_PORT']
        $env:JWT_SECRET = $config['JWT_SECRET']
        $env:JWT_ACCESS_TOKEN_EXPIRATION = $config['JWT_ACCESS_TOKEN_EXPIRATION']
        $env:JWT_REFRESH_TOKEN_EXPIRATION = $config['JWT_REFRESH_TOKEN_EXPIRATION']
        
        # Démarrer l'application
        .\mvnw spring-boot:run
    } catch {
        Write-Host "Erreur lors du démarrage de l'application: $_"
    }
} else {
    Write-Host "Fichier mvnw non trouvé. Vérifiez l'installation du projet."
}