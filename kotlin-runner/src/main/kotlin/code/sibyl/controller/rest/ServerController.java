package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 服务器信息接口
 * <p>
 * 用途:前端在服务器本机以回环地址(localhost/127.0.0.1)打开页面时,
 * 无法从 location.hostname 得知局域网 IP;通过本接口获取服务器自身的局域网 IPv4,
 * 让 API 地址统一指向局域网 IP(同网段其他设备亦可访问)。
 */
@RestController
@RequestMapping("/api/rest/v1/server")
@Slf4j
public class ServerController {

    /**
     * 返回服务器第一个局域网 IPv4(优先 192.168.x;无则取第一个非回环 IPv4;失败返回 null)
     *
     * GET /api/rest/v1/server/lan-ip
     */
    @GetMapping("/lan-ip")
    @ResponseBody
    public Mono<Response> lanIp() {
        String ip = findLanIp();
        log.info("[server/lan-ip] -> {}", ip);
        return Mono.just(Response.success(ip));
    }

    private String findLanIp() {
        try {
            List<String> ips = new ArrayList<>();
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;
                // 排除虚拟网卡:VMware(VMnet)/VirtualBox/Hyper-V/WSL/Docker 的 vEthernet 等
                String name = ni.getDisplayName();
                if (name != null && name.toLowerCase().matches(".*(vmnet|vmware|virtualbox|vEthernet|hyper-v|docker|wsl).*")) {
                    continue;
                }
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress a = addrs.nextElement();
                    if (a instanceof Inet4Address) {
                        ips.add(a.getHostAddress());
                    }
                }
            }
            // 优先 192.168.x 网段(真实局域网);无则取第一个非回环 IPv4
            return ips.stream().filter(ip -> ip.startsWith("192.168.")).findFirst()
                    .orElse(ips.isEmpty() ? null : ips.get(0));
        } catch (Exception e) {
            log.warn("[server/lan-ip] 枚举网卡失败", e);
            return null;
        }
    }
}
