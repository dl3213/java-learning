package code.sibyl.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 视频多帧指纹(方案 B):均匀抽 N 帧 → 中心裁剪 80%(去水印边缘)→ 每帧 pHash
 *
 * 相似判定:两个视频的对应帧汉明距离 ≤ frameThreshold 的帧数 ≥ minHits
 *  · 抗水印:裁掉四周 10%(水印多在角落) + pHash 低频特性
 *  · 抗转码/分辨率:抽帧统一缩放到 320 宽后再算 32x32 DCT
 *  · 不依赖起始时间完全对齐(中间段均匀采样,片头片尾变化影响小)
 */
@Slf4j
public class VideoFrameHashService {

    /** 默认抽帧数 */
    public static final int DEFAULT_FRAMES = 12;

    /** 单次 ffmpeg/ffprobe 超时(秒):USB 盘大文件定位关键帧较慢,但超过这个值基本是异常文件 */
    private static final int PROCESS_TIMEOUT_SECONDS = 30;

    /** 单视频总预算(秒):超时则放弃剩余帧(避免坏文件拖住整批) */
    private static final int VIDEO_BUDGET_SECONDS = 180;

    /**
     * 抽取多帧并计算指纹
     *
     * @param videoPath 视频绝对路径
     * @param frames    期望帧数(实际返回可能少于该值,失败的帧被跳过)
     * @return 指纹数组(每项 16 位十六进制);时长未知/全帧失败返回 null
     */
    public static String[] frameHashes(String videoPath, int frames) {
        if (videoPath == null || videoPath.isBlank()) return null;
        File video = new File(videoPath);
        if (!video.exists() || !video.isFile()) {
            log.warn("[vhash] 源文件不存在: {}", videoPath);
            return null;
        }
        int want = frames <= 0 ? DEFAULT_FRAMES : frames;
        double duration = probeDuration(videoPath);
        if (duration <= 0) {
            log.warn("[vhash] 无法获取时长(可能非视频/文件损坏): {}", videoPath);
            return null;
        }

        Path tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory("vhash-");
            List<String> hashes = new ArrayList<>();
            long budgetStart = System.currentTimeMillis();
            for (int i = 0; i < want; i++) {
                // 单视频总预算保护:坏文件/超大文件不拖住整批
                if ((System.currentTimeMillis() - budgetStart) / 1000 > VIDEO_BUDGET_SECONDS) {
                    log.warn("[vhash] 超总预算 {}s, 放弃剩余帧: {}", VIDEO_BUDGET_SECONDS, videoPath);
                    break;
                }
                // 取每个等分段的中点,避开片头片尾
                double seekSeconds = duration * (i + 0.5) / want;
                Path out = tmpDir.resolve(i + ".jpg");
                if (!captureFrame(videoPath, seekSeconds, out.toString())) continue;
                String hash = ImageHashService.pHash(out.toString());
                if (hash != null) hashes.add(hash);
            }
            if (hashes.isEmpty()) {
                log.warn("[vhash] 全部抽帧失败: {}", videoPath);
                return null;
            }
            return hashes.toArray(new String[0]);
        } catch (Exception e) {
            log.warn("[vhash] 计算失败: {}", videoPath, e);
            return null;
        } finally {
            if (tmpDir != null) deleteQuietly(tmpDir);
        }
    }

    /** 把多帧指纹数组拼成 DB 存储格式(逗号分隔) */
    public static String join(String[] hashes) {
        return hashes == null ? null : String.join(",", hashes);
    }

    /**
     * ffprobe 探测时长(秒);失败返回 0
     */
    public static double probeDuration(String videoPath) {
        List<String> cmd = List.of(
                FfmpegService.ffprobe,
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                videoPath
        );
        String out = runAndRead(cmd);
        if (out == null || out.isBlank()) return 0;
        try {
            return Double.parseDouble(out.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 抓单帧:-ss 置于 -i 之前用关键帧快速定位;中心裁剪 80% 去掉四周水印;缩放到 320 宽
     */
    private static boolean captureFrame(String videoPath, double seekSeconds, String outPath) {
        List<String> cmd = List.of(
                FfmpegService.ffmpeg,
                "-y", "-hide_banner", "-loglevel", "error",
                "-ss", String.format(java.util.Locale.ROOT, "%.3f", seekSeconds),
                "-i", videoPath,
                "-frames:v", "1",
                "-vf", "crop=iw*0.8:ih*0.8,scale=320:-2",
                outPath
        );
        Process p = null;
        try {
            p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            boolean done = p.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!done) {
                p.destroyForcibly();
                log.warn("[vhash] 抽帧超时 {}s: {}", PROCESS_TIMEOUT_SECONDS, videoPath);
                return false;
            }
            return new File(outPath).exists() && new File(outPath).length() > 0;
        } catch (Exception e) {
            log.warn("[vhash] 抽帧命令失败: {}", videoPath, e);
            return false;
        } finally {
            if (p != null && p.isAlive()) p.destroyForcibly();
        }
    }

    /** 执行命令并读取首行输出(用于 ffprobe) */
    private static String runAndRead(List<String> cmd) {
        Process p = null;
        try {
            p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                boolean done = p.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                if (!done) {
                    p.destroyForcibly();
                    return null;
                }
                return line;
            }
        } catch (Exception e) {
            log.warn("[vhash] ffprobe 执行失败", e);
            return null;
        } finally {
            if (p != null && p.isAlive()) p.destroyForcibly();
        }
    }

    private static void deleteQuietly(Path dir) {
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder()).forEach(pp -> {
                try {
                    Files.deleteIfExists(pp);
                } catch (Exception ignored) {
                }
            });
        } catch (Exception e) {
            log.warn("[vhash] 清理临时目录失败: {}", dir, e);
        }
    }
}
