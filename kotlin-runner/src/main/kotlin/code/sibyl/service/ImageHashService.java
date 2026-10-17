package code.sibyl.service;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * 图片感知哈希:双指纹
 *
 * pHash:32x32 灰度 → DCT → 左上 8x8 低频 → 中位数二值化 → 64bit
 *        抓"整图近似重复"(缩放/转码/压缩损);对大面积换背景色敏感
 * eHash:Canny 边缘 → 8x8 边缘密度 → 均值二值化 → 64bit
 *        纯色背景不产生边缘,抓"同主体异背景";对纹理背景/低对比主体较弱
 *
 * 相似判定:对应指纹汉明距离 ≤ 10(可按效果调整)
 */
@Slf4j
public class ImageHashService {

    private static volatile boolean nativeLoaded = false;

    static {
        try {
            // bytedeco Loader 会从 jar 中解压原生库并加载(含 opencv_java4100),
            // 之后 Core.NATIVE_LIBRARY_NAME 的 loadLibrary 即为已加载状态的幂等操作
            Loader.load(opencv_java.class);
            nativeLoaded = true;
        } catch (Throwable t) {
            log.error("[ImageHashService] OpenCV 原生库加载失败", t);
        }
    }

    /**
     * 一次解码同时计算双指纹
     *
     * @return [0]=pHash, [1]=eHash,均为 16 位十六进制;解码失败/原生库不可用返回 null
     */
    public static String[] computeHashes(String imagePath) {
        if (!nativeLoaded) return null;
        Mat img = null;
        Mat source = null;
        Mat dct = null;
        Mat edge = null;
        try {
            img = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);
            if (img.empty()) {
                log.warn("[ImageHashService] 无法读取图片: {}", imagePath);
                return null;
            }

            // ---- pHash:32x32 → DCT → 左上 8x8 低频 → 中位数二值化 ----
            Imgproc.resize(img, img, new Size(32, 32));
            source = new Mat();
            img.convertTo(source, CvType.CV_32F);
            dct = new Mat();
            Core.dct(source, dct);
            double[] cells = new double[64];
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    cells[row * 8 + col] = dct.get(row, col)[0];
                }
            }
            // 中位数(排除 [0][0] 直流分量)
            double[] rest = new double[63];
            System.arraycopy(cells, 1, rest, 0, 63);
            Arrays.sort(rest);
            double median = rest[31];

            long phash = 0L;
            for (int i = 0; i < 64; i++) {
                boolean bit = (i == 0) ? (cells[0] > median) : (cells[i] > median);
                if (bit) phash |= (1L << (63 - i));
            }

            // ---- eHash:Canny 边缘 → 8x8 密度(INTER_AREA 均值)→ 均值二值化 ----
            edge = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);
            Imgproc.GaussianBlur(edge, edge, new Size(3, 3), 0);
            Imgproc.Canny(edge, edge, 100, 200);
            Imgproc.resize(edge, edge, new Size(8, 8), 0, 0, Imgproc.INTER_AREA);
            double edgeMean = Core.mean(edge).val[0];
            long ehash = 0L;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (edge.get(r, c)[0] > edgeMean) {
                        ehash |= (1L << (63 - (r * 8 + c)));
                    }
                }
            }

            return new String[]{String.format("%016x", phash), String.format("%016x", ehash)};
        } catch (Exception e) {
            log.warn("[ImageHashService] 计算失败: {}", imagePath, e);
            return null;
        } finally {
            if (img != null) img.release();
            if (source != null) source.release();
            if (dct != null) dct.release();
            if (edge != null) edge.release();
        }
    }

    /**
     * 计算图片 pHash
     *
     * @param imagePath 图片绝对路径
     * @return 16 位十六进制指纹;解码失败/原生库不可用返回 null
     */
    public static String pHash(String imagePath) {
        String[] both = computeHashes(imagePath);
        return both == null ? null : both[0];
    }

    /**
     * 计算图片 eHash(Canny 边缘密度指纹)
     */
    public static String eHash(String imagePath) {
        String[] both = computeHashes(imagePath);
        return both == null ? null : both[1];
    }

    /**
     * 解析视频封面对应的图片路径(用于视频相似初筛的指纹来源)
     * 优先 thumbnail 字段,回退 {absolutePath}.jpg(缩略图约定,前端预览同款)
     *
     * @return 实际存在的路径;都不存在返回 null
     */
    public static String thumbnailPath(String thumbnail, String absolutePath) {
        if (thumbnail != null && !thumbnail.trim().isEmpty()) {
            java.io.File f = new java.io.File(thumbnail.trim());
            if (f.exists() && f.isFile()) return f.getAbsolutePath();
        }
        if (absolutePath != null && !absolutePath.trim().isEmpty()) {
            java.io.File f2 = new java.io.File(absolutePath.trim() + ".jpg");
            if (f2.exists() && f2.isFile()) return f2.getAbsolutePath();
        }
        return null;
    }

    /**
     * 两个十六进制指纹的汉明距离
     *
     * @return 距离(0~64);任一入参非法返回 Integer.MAX_VALUE
     */
    public static int hamming(String hex1, String hex2) {
        if (hex1 == null || hex2 == null || hex1.length() != 16 || hex2.length() != 16) return Integer.MAX_VALUE;
        try {
            long v1 = Long.parseUnsignedLong(hex1, 16);
            long v2 = Long.parseUnsignedLong(hex2, 16);
            return Long.bitCount(v1 ^ v2);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}
