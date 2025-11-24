@echo off
cd /d "c:\Users\dembe\Desktop\odkproject\APIMussoDeme"

REM Vérifier si Maven est installé
where mvn >nul 2>&1
if %errorlevel% == 0 (
    echo Maven trouvé. Démarrage avec Maven...
    mvn spring-boot:run
) else (
    echo Maven non trouvé. Vérification du wrapper Maven...
    if exist mvnw.cmd (
        echo Wrapper Maven trouvé. Démarrage avec le wrapper...
        mvnw.cmd spring-boot:run
    ) else (
        echo Aucune méthode de démarrage trouvée.
        pause
    )
)