#!/bin/sh
# kotlin-runner 容器启动脚本
# 每次启动时探测宿主机局域网 IPv4，幂等写入 config/application.yml 的 ip-whitelist.ips，
# 保证宿主机 DHCP IP 变化后无需手工改配置。探测不到或已存在时不做任何修改。

set -e

CONF=/app/config/application.yml

# 1) 探测局域网 IP：hostname -I 输出所有非回环地址，过滤掉 IPv6 / 回环 / 链路本地 / Docker 网桥
LAN_IP=""
for cand in $(hostname -I 2>/dev/null); do
  case "$cand" in
    *:*) continue ;;                                          # 跳过 IPv6
    127.*|169.254.*) continue ;;                              # 回环 / 链路本地
    172.1[6-9].*|172.2[0-9].*|172.3[0-1].*) continue ;;       # Docker 网桥 172.16-31.x
  esac
  LAN_IP="$cand"
  break
done

# 2) 幂等写入白名单（仅追加缺失项，不动已有列表）
if [ -n "$LAN_IP" ] && [ -f "$CONF" ]; then
  if grep -qE "(^|[, ])$LAN_IP([, ]|$)" "$CONF"; then
    echo "[entrypoint] $LAN_IP 已在白名单，无需更新"
  else
    sed -i "s/^\([[:space:]]*ips:.*\)/\1,$LAN_IP/" "$CONF"
    echo "[entrypoint] 已自动追加 $LAN_IP 到 IP 白名单"
  fi
else
  echo "[entrypoint] 未探测到局域网 IPv4，跳过白名单更新"
fi

# 3) 启动应用（exec 保持 PID 1，SIGTERM 直达 java，优雅停机不受影响）
exec java $JAVA_OPTS --enable-preview -jar kotlin-runner-0.0.1-SNAPSHOT.jar
