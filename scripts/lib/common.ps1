$ErrorActionPreference = "Stop"

function Write-Banner { param([string]$Text)
    Write-Host ""; Write-Host "  ========================================" -ForegroundColor Cyan
    Write-Host "  $Text" -ForegroundColor Cyan; Write-Host "  ========================================" -ForegroundColor Cyan; Write-Host "" }
function Write-Step { param([int]$N,[int]$Total,[string]$Message) Write-Host "[$N/$Total] $Message" -ForegroundColor Cyan }
function Write-Ok { param([string]$Message) Write-Host "  OK: $Message" -ForegroundColor Green }
function Write-Fail { param([string]$Message) Write-Host "  FAILED: $Message" -ForegroundColor Red }
function Get-ProjectRoot { return (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path }

function Read-LocalConfig { param([string]$Root)
    $path = Join-Path $Root "config\local.properties"
    $example = Join-Path $Root "config\local.properties.example"
    if (-not (Test-Path $path)) { if (Test-Path $example) { Copy-Item $example $path -Force } }
    $cfg = @{}
    [IO.File]::ReadAllLines($path, [Text.Encoding]::UTF8) | ForEach-Object {
        $t = $_.Trim()
        if ($t -and -not $t.StartsWith("#") -and $t.Contains("=")) { $p = $t.Split("=",2); $cfg[$p[0].Trim()] = $p[1].Trim() }
    }
    if (-not $cfg["mysql.host"]) { $cfg["mysql.host"] = "localhost" }
    if (-not $cfg["mysql.port"]) { $cfg["mysql.port"] = "3306" }
    if (-not $cfg["mysql.user"]) { $cfg["mysql.user"] = "root" }
    if (-not $cfg["app.port"]) { $cfg["app.port"] = "8080" }
    return $cfg }

function Save-LocalConfig { param([string]$Root,[hashtable]$Cfg)
    $path = Join-Path $Root "config\local.properties"
    $text = "mysql.host=$($Cfg['mysql.host'])`r`nmysql.port=$($Cfg['mysql.port'])`r`nmysql.user=$($Cfg['mysql.user'])`r`nmysql.password=$($Cfg['mysql.password'])`r`napp.port=$($Cfg['app.port'])`r`n"
    [IO.File]::WriteAllText($path, $text, (New-Object System.Text.UTF8Encoding $false)) }

function Find-MySqlExe {
    $list = @("${env:ProgramFiles}\MySQL\MySQL Server 8.4\bin\mysql.exe","${env:ProgramFiles}\MySQL\MySQL Server 8.0\bin\mysql.exe","C:\xampp\mysql\bin\mysql.exe")
    foreach ($p in $list) { if (Test-Path $p) { return $p } }
    $cmd = Get-Command mysql -ErrorAction SilentlyContinue; if ($cmd) { return $cmd.Source }; return $null }

function Start-MySqlIfPossible {
    Get-Service -ErrorAction SilentlyContinue | Where-Object { $_.Name -match 'mysql' } | ForEach-Object {
        if ($_.Status -ne 'Running') { try { Write-Host "  Starting service $($_.Name)..." -ForegroundColor Yellow; Start-Service $_.Name -ErrorAction Stop } catch {} }
    }
    Start-Sleep -Seconds 2 }


function Invoke-External { param([scriptblock]$Command)
    $prev = $ErrorActionPreference
    $ErrorActionPreference = "Continue"
    try { & $Command 2>$null | Out-Null } finally { $ErrorActionPreference = $prev }
    return ($LASTEXITCODE -eq 0)
}
function Test-MySqlConnection { param($MysqlExe,$HostName,$Port,$User,$Password)
    $env:MYSQL_PWD = $Password
    $ok = Invoke-External { & $MysqlExe -h $HostName -P $Port -u $User -e "SELECT 1" }
    Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue
    return $ok }


function Test-MySqlPortOpen { param([string]$HostName,[string]$Port)
    try {
        $client = New-Object System.Net.Sockets.TcpClient
        $iar = $client.BeginConnect($HostName, [int]$Port, $null, $null)
        $ok = $iar.AsyncWaitHandle.WaitOne(2000, $false)
        if ($ok) { $client.EndConnect($iar); $client.Close(); return $true }
        $client.Close(); return $false
    } catch { return $false }
}
function Resolve-MySqlCredentials { param($Root,$MysqlExe,[hashtable]$Cfg)
    $hostName = $Cfg["mysql.host"]; $port = $Cfg["mysql.port"]; $user = $Cfg["mysql.user"]
    if (-not (Test-MySqlPortOpen -HostName $hostName -Port $port)) { return $false }
    $candidates = @(); if ($Cfg["mysql.password"]) { $candidates += $Cfg["mysql.password"] }
    $candidates += @("root","","mysql"); $candidates = $candidates | Select-Object -Unique
    foreach ($pass in $candidates) {
        if (Test-MySqlConnection -MysqlExe $MysqlExe -HostName $hostName -Port $port -User $user -Password $pass) {
            $Cfg["mysql.password"] = $pass; Save-LocalConfig -Root $Root -Cfg $Cfg; return $true } }
    Write-Host ""; Write-Host "  MySQL is running but password was not recognized." -ForegroundColor Yellow
    Write-Host "  Enter MySQL password once (saved automatically):" -ForegroundColor Yellow
    $secure = Read-Host "  Password for user '$user'"
    if (Test-MySqlConnection -MysqlExe $MysqlExe -HostName $hostName -Port $port -User $user -Password $secure) {
        $Cfg["mysql.password"] = $secure; Save-LocalConfig -Root $Root -Cfg $Cfg; return $true }
    return $false }

function Test-JavaInstalled {
    if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
        Write-Fail "Java not installed"; Write-Host "  Install JDK 17: https://adoptium.net/" -ForegroundColor Yellow; return $false }
    $prev = $ErrorActionPreference; $ErrorActionPreference = "Continue"
    $ver = (java -version 2>&1 | Select-Object -First 1).ToString()
    $ErrorActionPreference = $prev
    Write-Ok "Java - $ver"; return $true }

function Ensure-Maven { param([string]$Root)
    $cmd = Get-Command mvn -ErrorAction SilentlyContinue; if ($cmd) { return $cmd.Source }
    $mvn = Join-Path $Root ".tools\apache-maven-3.9.9\bin\mvn.cmd"
    if (Test-Path $mvn) { return $mvn }
    Write-Host "  Downloading Maven (first run only)..." -ForegroundColor Yellow
    $tools = Join-Path $Root ".tools"; New-Item -ItemType Directory -Force -Path $tools | Out-Null
    $zip = Join-Path $tools "maven.zip"
    Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip" -OutFile $zip -UseBasicParsing
    Expand-Archive $zip $tools -Force; Remove-Item $zip -Force
    if (-not (Test-Path $mvn)) { throw "Maven download failed" }
    Write-Ok "Maven ready"; return $mvn }

function Update-HibernateConfig { param([string]$Root,[hashtable]$Cfg)
    $file = Join-Path $Root "src\main\resources\hibernate.cfg.xml"
    $xml = [IO.File]::ReadAllText($file)
    $user = $Cfg["mysql.user"]; $pass = $Cfg["mysql.password"]
    $hostName = $Cfg["mysql.host"]; $port = $Cfg["mysql.port"]
    $xml = $xml -replace '(?<=hibernate\.connection\.username">)[^<]+', $user
    $xml = $xml -replace '(?<=hibernate\.connection\.password">)[^<]+', $pass
    $url = "jdbc:mysql://${hostName}:${port}/researchsphere_db?useSSL=false&amp;serverTimezone=UTC&amp;allowPublicKeyRetrieval=true"
    $xml = $xml -replace '<property name="hibernate\.connection\.url">[^<]+</property>', "<property name=`"hibernate.connection.url`">$url</property>"
    [IO.File]::WriteAllText($file, $xml, (New-Object System.Text.UTF8Encoding $false))
    Write-Ok "Config files updated" }

function Get-Utf8SqlFile { param([string]$SqlFile,[string]$Root)
    $bytes = [IO.File]::ReadAllBytes($SqlFile)
    $text = $null
    if ($bytes.Length -ge 2 -and $bytes[0] -eq 255 -and $bytes[1] -eq 254) { $text = [Text.Encoding]::Unicode.GetString($bytes) }
    elseif ($bytes.Length -ge 2 -and $bytes[1] -eq 0) { $text = [Text.Encoding]::Unicode.GetString($bytes) }
    else { $text = [Text.Encoding]::UTF8.GetString($bytes) }
    $out = Join-Path $Root ".tools\researchsphere_import.sql"
    $dir = Split-Path $out; if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    [IO.File]::WriteAllText($out, $text, (New-Object System.Text.UTF8Encoding $false))
    return ($out -replace '\\','/') }

function Test-DatabaseInitialized { param($MysqlExe,$HostName,$Port,$User,$Pass)
    $env:MYSQL_PWD = $Pass
    $prev = $ErrorActionPreference; $ErrorActionPreference = "Continue"
    $count = & $MysqlExe -h $HostName -P $Port -u $User -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='researchsphere_db' AND table_name='users';" 2>$null
    if ($LASTEXITCODE -ne 0 -or [int]$count -eq 0) {
        $ErrorActionPreference = $prev
        Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue
        return $false
    }
    $rows = & $MysqlExe -h $HostName -P $Port -u $User -N -e "SELECT COUNT(*) FROM researchsphere_db.users;" 2>$null
    $ErrorActionPreference = $prev
    Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue
    return ($LASTEXITCODE -eq 0 -and [int]$rows -gt 0)
}

function Repair-DemoPasswords { param($MysqlExe,$HostName,$Port,$User,$Pass)
    $correctHash = 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f'
    $wrongHash = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'
    $fixSql = "USE researchsphere_db; UPDATE users SET password_hash='$correctHash' WHERE password_hash='$wrongHash';"
    $env:MYSQL_PWD = $Pass
    $prev = $ErrorActionPreference; $ErrorActionPreference = "Continue"
    & $MysqlExe -h $HostName -P $Port -u $User -e $fixSql 2>&1 | Out-Null
    $ErrorActionPreference = $prev
    Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue
}

function Import-Database { param($Root,$MysqlExe,[hashtable]$Cfg)
    $sqlFile = Join-Path $Root "sql\researchsphere_db.sql"
    if (-not (Test-Path $sqlFile)) { throw "Missing sql\researchsphere_db.sql" }
    $hostName = $Cfg["mysql.host"]; $port = $Cfg["mysql.port"]; $user = $Cfg["mysql.user"]; $pass = $Cfg["mysql.password"]
    $env:MYSQL_PWD = $pass
    $prev = $ErrorActionPreference; $ErrorActionPreference = "Continue"
    & $MysqlExe -h $hostName -P $port -u $user -e "CREATE DATABASE IF NOT EXISTS researchsphere_db;" 2>&1 | Out-Null
    $ErrorActionPreference = $prev
    Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue

    if (Test-DatabaseInitialized -MysqlExe $MysqlExe -HostName $hostName -Port $port -User $user -Pass $pass) {
        Repair-DemoPasswords -MysqlExe $MysqlExe -HostName $hostName -Port $port -User $user -Pass $pass
        Write-Ok "Database ready (existing data kept - tasks/projects saved)"
        return
    }

    Write-Host "  First-time setup: importing demo database..." -ForegroundColor Yellow
    $sqlPath = Get-Utf8SqlFile -SqlFile $sqlFile -Root $Root
    $env:MYSQL_PWD = $pass
    $ErrorActionPreference = "Continue"
    & $MysqlExe -h $hostName -P $port -u $user --default-character-set=utf8mb4 -e "source $sqlPath" 2>&1 | Out-Null
    $ok = ($LASTEXITCODE -eq 0)
    $ErrorActionPreference = $prev
    Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue
    if (-not $ok) { throw "Database import failed. Check MySQL is running and user has permissions." }
    Repair-DemoPasswords -MysqlExe $MysqlExe -HostName $hostName -Port $port -User $user -Pass $pass
    Write-Ok "Database researchsphere_db initialized with demo data" }


function Repair-ProjectEncoding { param([string]$Root)
    $utf8 = New-Object System.Text.UTF8Encoding $false
    Get-ChildItem $Root -Recurse -File | Where-Object {
        $_.FullName -notmatch '\\\.tools\\|\\target\\' -and $_.Extension -match '\.(xml|java|jsp|properties|css|js|sql|cfg|bat|md|txt)$'
    } | ForEach-Object {
        $b = [IO.File]::ReadAllBytes($_.FullName)
        if ($b.Length -ge 2 -and (($b[0] -eq 255 -and $b[1] -eq 254) -or ($b[1] -eq 0))) {
            [IO.File]::WriteAllText($_.FullName, [Text.Encoding]::Unicode.GetString($b), $utf8)
        }
    } }
function Show-FatalError { param([string]$Title,[string[]]$Lines)
    Write-Host ""; Write-Host "  X $Title" -ForegroundColor Red
    foreach ($line in $Lines) { Write-Host "    $line" -ForegroundColor Yellow }; Write-Host "" }