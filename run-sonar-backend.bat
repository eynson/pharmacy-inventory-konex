@echo off
echo ========================================
echo   Ejecutar Analisis SonarQube Backend
echo ========================================
echo.
set /p TOKEN="Ingresa el TOKEN de SonarQube: "
echo.
echo Ejecutando analisis...
echo.

cd /d "%~dp0backend\pharmacy-inventory"

mvn clean verify sonar:sonar ^
  -Dsonar.projectKey=pharmacy-inventory-backend ^
  -Dsonar.projectName="Pharmacy Inventory Backend" ^
  -Dsonar.host.url=http://localhost:9000 ^
  -Dsonar.login=%TOKEN%

echo.
echo ========================================
echo Analisis completado!
echo Ve a: http://localhost:9000
echo ========================================
pause
