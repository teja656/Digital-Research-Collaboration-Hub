@echo off
setlocal
title ResearchSphere
cd /d "%~dp0"
color 0B
cls
echo.
echo   ====================================================
echo        ResearchSphere - One-Click Startup
echo   ====================================================
echo.
echo   Before first use (one time only):
echo     - Install Java JDK 17  (https://adoptium.net/)
echo     - Install XAMPP or MySQL and START MySQL
echo.
echo   Everything else is automatic. Please wait...
echo.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run.ps1"
if errorlevel 1 (
    color 0C
    echo.
    echo   Startup stopped. Read the red/yellow messages above.
    echo.
    pause
    exit /b 1
)
pause