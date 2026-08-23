# ============================================================
# 一键更新并重启后端(本地 JVM 方式)
# 流程: 编译 -> 停旧进程 -> 刷新 classpath -> 启动 -> 等待 8080
# 用法: powershell -ExecutionPolicy Bypass -File deploy-backend.ps1
# ============================================================
$ErrorActionPreference = 'Stop'
$mvn  = 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\plugins\maven\lib\maven3\bin\mvn.cmd'
$java = 'C:\Users\dl3213\.jdks\graalvm-jdk-21.0.7\bin\java.exe'
$env:JAVA_HOME = 'C:\Users\dl3213\.jdks\graalvm-jdk-21.0.7'
$root = 'C:\dl3213\4dev\workspace\java-learning'   # 后端工作目录(父项目,h2 相对路径在此解析)
$app  = Join-Path $root 'kotlin-runner'

Set-Location $app
Write-Host '[1/4] 编译...' -ForegroundColor Cyan
& $mvn test-compile -q
if ($LASTEXITCODE -ne 0) { throw '编译失败,已中止' }

Write-Host '[2/4] 停止旧进程...' -ForegroundColor Cyan
Get-CimInstance Win32_Process -Filter "Name='java.exe'" |
    Where-Object { $_.CommandLine -match 'backend\.args' } |
    ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }
Start-Sleep -Seconds 2

Write-Host '[3/4] 刷新 classpath 并生成启动参数...' -ForegroundColor Cyan
& $mvn dependency:build-classpath '-Dmdep.outputFile=target/cp.txt' -q
$cpRaw  = (Get-Content "$app\target\cp.txt" -Raw).Trim().Replace('\','/')
$fullcp = (($app + '\target\classes').Replace('\','/')) + ';' + $cpRaw
$argfile = "$app\target\backend.args"
@('--enable-preview','-Dfile.encoding=UTF-8','-cp',$fullcp,'code.sibyl.KotlinApplicationKt') |
    Set-Content -Path $argfile -Encoding ASCII

Write-Host '[4/4] 启动...' -ForegroundColor Cyan
Start-Process -FilePath $java -ArgumentList "@$argfile" `
    -WorkingDirectory $root `
    -RedirectStandardOutput "$app\target\backend-out.log" `
    -RedirectStandardError  "$app\target\backend-err.log" `
    -WindowStyle Hidden

$ok = $false; $elapsed = 0
foreach ($i in 1..30) {
    Start-Sleep -Seconds 2; $elapsed += 2
    if (netstat -ano | Select-String ':8080\s.*LISTENING') { $ok = $true; break }
}
if ($ok) { Write-Host "✅ 后端已重启完成(耗时 ${elapsed}s): http://127.0.0.1:8080" -ForegroundColor Green }
else     { Write-Host '❌ 60 秒内未监听 8080,日志见 target\backend-out.log / backend-err.log' -ForegroundColor Red }