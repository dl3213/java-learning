# ============================================================
# 视频 vphash 回填(方案 A:封面帧指纹,相似视频初筛用)
# 用法: powershell -ExecutionPolicy Bypass -File backfill-video-vphash.ps1
# 说明: 走 Maven surefire 执行 AppTest#videoVphashBackfill,
#       绕开 IDEA 运行配置(避免 -javaagent + GraalVM + preview 的类加载问题)
# ============================================================
$ErrorActionPreference = 'Stop'
$env:JAVA_HOME = 'C:\Users\dl3213\.jdks\graalvm-jdk-21.0.7'
$mvn = 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\plugins\maven\lib\maven3\bin\mvn.cmd'
$app = 'C:\dl3213\4dev\workspace\java-learning\kotlin-runner'
Set-Location $app

Write-Host '[回填] 视频封面指纹 vphash(幂等,只补空的)...' -ForegroundColor Cyan
& $mvn test '-Dsurefire.skip=false' '-Dtest=AppTest#videoVphashBackfill' '-DargLine=--enable-preview'

if ($LASTEXITCODE -ne 0) {
    Write-Host '[失败] 见上方 Maven 日志。若出现 ClassCircularityError,请把 JAVA_HOME 换成标准 JDK 21 后重跑。' -ForegroundColor Red
    exit 1
}
Write-Host '[完成] 回填结束,若上方输出 "完成回填 N 个" 即成功' -ForegroundColor Green