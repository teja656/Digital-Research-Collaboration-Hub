. "$PSScriptRoot\lib\common.ps1"
$Root = Get-ProjectRoot
Set-Location $Root
try {
    Write-Banner "ResearchSphere - One-Click Startup"
    Write-Step 1 5 "Checking Java..."
    if (-not (Test-JavaInstalled)) { exit 1 }
    Write-Step 2 5 "Checking MySQL..."
    $mysql = Find-MySqlExe
    if (-not $mysql) {
        Show-FatalError "MySQL not installed" @(
            "Install XAMPP or MySQL Server once.",
            "Then double-click START.bat again.")
        exit 1 }
    Write-Ok "MySQL tools found"
    Start-MySqlIfPossible | Out-Null
    $Cfg = Read-LocalConfig -Root $Root
    if (-not (Test-MySqlPortOpen -HostName $Cfg["mysql.host"] -Port $Cfg["mysql.port"])) {
        Show-FatalError "MySQL is not running" @(
            "Open XAMPP Control Panel and click Start next to MySQL.",
            "OR start MySQL80 service in services.msc.",
            "Then double-click START.bat again.")
        exit 1 }
    if (-not (Resolve-MySqlCredentials -Root $Root -MysqlExe $mysql -Cfg $Cfg)) {
        Show-FatalError "MySQL login failed" @(
            "Enter the correct root password when prompted.",
            "It will be saved automatically for next time.")
        exit 1 }
    Write-Ok "MySQL connected"
    Write-Step 3 5 "Database and configuration..."
    Update-HibernateConfig -Root $Root -Cfg $Cfg
    Import-Database -Root $Root -MysqlExe $mysql -Cfg $Cfg
    Repair-ProjectEncoding -Root $Root
    $env:MAVEN_OPTS = '--enable-native-access=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED'
    Write-Step 4 5 "Building application (first run: 5-15 min)..."
    $mvn = Ensure-Maven -Root $Root
    $port = $Cfg["app.port"]
    $logDir = Join-Path $Root ".tools"; New-Item -ItemType Directory -Force -Path $logDir | Out-Null
    $log = Join-Path $logDir "last-build.log"
    $prev = $ErrorActionPreference; $ErrorActionPreference = "Continue"
    & $mvn clean package -DskipTests 2>&1 | Tee-Object -FilePath $log | Out-Null
    $ErrorActionPreference = $prev
    if ($LASTEXITCODE -ne 0) {
        Show-FatalError "Build failed" @("Open .tools\last-build.log for details.","First run needs internet to download libraries.")
        exit 1 }
    Write-Ok "Build complete"
    $loginUrl = "http://localhost:${port}/ResearchSphere/login"
    Write-Step 5 5 "Starting server (embedded Jetty)..."
    Write-Host ""
    Write-Host "  Opening browser: $loginUrl" -ForegroundColor Green
    Write-Host "  Email: admin@researchsphere.edu   Password: password123" -ForegroundColor Green
    Write-Host "  Keep this window open. Ctrl+C to stop." -ForegroundColor Yellow
    Write-Host ""
    Start-Sleep -Seconds 2
    Start-Process $loginUrl
    & $mvn "-Dapp.port=$port" "jetty:run"
} catch {
    Show-FatalError "Unexpected error" @($_.Exception.Message, "Double-click START.bat to retry.")
    exit 1
}