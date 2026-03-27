package code.sibyl.controller;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import com.alibaba.fastjson2.JSONObject;
import io.r2dbc.pool.ConnectionPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import oshi.hardware.GlobalMemory;
import oshi.util.GlobalConfig;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/core")
public class SystemInfoController {

    static {
        GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true);
    }

    private final int interval = 5;

    @GetMapping(value = "/cpu", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> cpu(ServerWebExchange serverWebExchange) {
//        System.err.println(serverWebExchange.getRequest().getCookies());
        return Flux.interval(Duration.ofSeconds(interval))
                .map(index -> {
                    JSONObject jsonObject = new JSONObject();
                    CpuInfo cpuInfo = OshiUtil.getCpuInfo(); // hutool封装的oshi

                    jsonObject.put("coreSize", cpuInfo.getCpuNum());
                    jsonObject.put("loadPercent", cpuInfo.getUsed());

                    //System.err.println(jsonObject);
                    return Response.success(jsonObject);
                });
    }

    @GetMapping(value = "/memory", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> memory() {
        return Flux.interval(Duration.ofSeconds(interval))
                .map(index -> {
                    JSONObject jsonObject = new JSONObject();
                    GlobalMemory info = OshiUtil.getMemory(); // hutool封装的oshi

                    jsonObject.put("total", info.getTotal() / 1024.0 / 1024.0 / 1024.0); //GB
                    jsonObject.put("used", (info.getTotal() - info.getAvailable()) / 1024.0 / 1024.0 / 1024.0); // GB
                    jsonObject.put("time", r.formatDate(LocalTime.now(), r.HH_mm_ss));

                    //System.err.println(jsonObject);
                    return Response.success(jsonObject);
                });
    }

    @GetMapping(value = "/jvm", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> jvm() {
        return Flux.interval(Duration.ofSeconds(interval))
                .map(index -> {
                    JSONObject jsonObject = new JSONObject();
                    val runtime = Runtime.getRuntime();
                    jsonObject.put("time", r.formatDate(LocalTime.now(), r.HH_mm_ss));
                    jsonObject.put("memoryTotal", runtime.totalMemory() / 1024.0 / 1024.0 ); //
                    jsonObject.put("memoryUsed", (runtime.totalMemory() - runtime.freeMemory()) / 1024.0 / 1024.0); //
                    Thread thread = Thread.currentThread();
                    long stackSize = 0; // todo
                    jsonObject.put("stackSize", thread.getThreadGroup().activeCount() * thread.getThreadGroup().activeGroupCount() * (stackSize)); //

                    ProcessHandle currentProcess = ProcessHandle.current();
                    ProcessHandle.Info info = currentProcess.info();
                    String user = info.user().get();
                    jsonObject.put("user", user);
                    jsonObject.put("pid", currentProcess.pid()); // 21964

                    //System.err.println(jsonObject);
                    return Response.success(jsonObject);
                });
    }

    @GetMapping(value = "/db", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> db() {
        return Flux.interval(Duration.ofSeconds(interval))
                .map(index -> {
                    JSONObject jsonObject = new JSONObject();
                    R2dbcEntityTemplate r2dbcEntityTemplate = r.getBean(R2dbcEntityTemplate.class);
                    return Response.success(jsonObject);
                });
    }

    public void cpuInfo() {
        // from oshi-core
//        SystemInfo systemInfo = new SystemInfo();
//        GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true);
//        CentralProcessor processor = systemInfo.getHardware().getProcessor();
//
//        long[] prevTicks = processor.getSystemCpuLoadTicks();
//        r.sleep(1000);
//        long[] ticks = processor.getSystemCpuLoadTicks();
//        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
//        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
//        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
//        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
//        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
//        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
//        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
//        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
//        long total = user + nice + cSys + idle + iowait + irq + softirq + steal;
//
//
//        jsonObject.put("coreSize", processor.getLogicalProcessorCount());
//        jsonObject.put("used", user);
//        jsonObject.put("idle ", idle);
//        jsonObject.put("wait ", iowait);
//        jsonObject.put("total", total);
//        jsonObject.put("loadPercent", r.percent(r.divide(r.bigDecimal(user), r.bigDecimal(total))));
    }
}
