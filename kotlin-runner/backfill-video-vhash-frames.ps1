# ============================================================
# 视频多帧指纹 vhash 回填(方案 B:12 帧 + 中心裁剪 + 每帧 pHash)
# 用法: powershell -ExecutionPolicy Bypass -File backfill-video-vhash-frames.ps1
# 说明: 每个视频 ffprobe 探时长 + 12 次关键帧抽帧,HDD 上约 3~5 秒/个
#       3000 个视频预计 3~5 小时;幂等,可随时中断后重跑(只补空的)
# ============================================================
$ErrorActionPreference = 'Stop'
$env:JAVA_HOME = 'C:\Users\dl3213\.jdks\graalvm-jdk-21.0.7'
$mvn = 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\plugins\maven\lib\maven3\bin\mvn.cmd'
$app = 'C:\dl3213\4dev\workspace\java-learning\kotlin-runner'
Set-Location $app

Write-Host '[回填] 视频多帧指纹 vhash(预计较久,可中断后重跑)...' -ForegroundColor Cyan
& $mvn test '-Dsurefire.skip=false' '-Dtest=AppTest#videoVhashFramesBackfill' '-DargLine=--enable-preview'

if ($LASTEXITCODE -ne 0) {
    Write-Host '[失败] 见上方 Maven 日志;若 ClassCircularityError 请换标准 JDK 21 后重跑' -ForegroundColor Red
    exit 1
}
Write-Host '[完成] 回填结束,日志出现 "完成回填 N 个" 即成功' -ForegroundColor Green