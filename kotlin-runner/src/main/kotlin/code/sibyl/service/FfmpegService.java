package code.sibyl.service;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.StringTemplate.STR;

public class FfmpegService {

    public final static String basePath = "D:\\4code\\4dev\\ffmpeg-7.1-essentials_build\\bin\\";

    public final static String ffmpeg = STR."\{basePath}ffmpeg.exe";
    public final static String ffprobe = STR."\{basePath}ffprobe.exe";

    public final static Runtime runtime = Runtime.getRuntime();



    public static void main123(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String path1 = "";
        path1 = "D:\\z\\1864319636821118976.png";
        path1 = "E:/sibyl-system/file/2025-01-23/1882441280106139648.png";
        path1 = "E:/sibyl-system/file/2025-03-17/1901623869651947520.jpg";
        path1 = "E:/sibyl-system/file/2025-01-23/1882441280215191552.jpeg"; // 相似图
        String path2 = "E:/sibyl-system/file/2024-12-04/1864319636821118976.png";

        Mat img1 = Imgcodecs.imread(path1);
        Mat img2 = Imgcodecs.imread(path2);

        // 初始化ORB检测器
        ORB orb = ORB.create(
                1000,            // 增加特征点数量  ;特征匹配数 = 这里 * 0.7 视为相似， 默认500
                1.2f,            // 多尺度检测
                8,               // 深层金字塔
                31,              // 更大边缘阈值
                0,
                2,
                ORB.HARRIS_SCORE, // 更好的特征点评分
                31,
                20
        );
        MatOfKeyPoint kp1 = new MatOfKeyPoint(), kp2 = new MatOfKeyPoint();
        Mat desc1 = new Mat(), desc2 = new Mat();

        // 检测特征点
        orb.detectAndCompute(img1, new Mat(), kp1, desc1);
        orb.detectAndCompute(img2, new Mat(), kp2, desc2);

        // 匹配特征点
        BFMatcher matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING, true);
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(desc1, desc2, matches);

        // 计算匹配率
        long totalMatches = matches.rows();
        System.out.println("特征匹配数: " + totalMatches);

    }

    public static void main(String[] args) throws Exception {
        FfmpegService.copy("C:\\迅雷下载\\hhd800.com@PRED-452.mp4", "D:\\4pc\\dl3213\\PRED-452-1.mp4", "01:53:10","02:00:08" );
//        FfmpegService.convert2mp4("E:\\kill la kill\\[Beatrice-Raws] Kill la Kill 02 [BDRip 1920x1080 x264 FLAC].mkv", "E:\\kill la kill\\op.mp4");
    }

    public static void convert2mp4(String fromFile, String toFile) throws Exception{
        String command = STR."\{ffmpeg} -i \"\{fromFile}\" -c:v copy -c:a copy \"\{toFile}\"";
        System.err.println(command);
        Process process = runtime.exec(command );
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.out.println(STR."FFmpeg --> : \{line}");
        }
        process.waitFor();
        System.out.println("Conversion completed successfully.");
    }

    public static void copy(String fromFile, String toFile, String stateTime, String endTime) throws Exception {

        String command = STR."\{ffmpeg} -ss \{stateTime} -to \{endTime} -i \"\{fromFile}\"  -c copy \"\{toFile}\"";
        System.err.println(command);
        Process process = runtime.exec(command );
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.out.println(STR."FFmpeg --> : \{line}");
        }
        process.waitFor();
        System.out.println("Conversion completed successfully.");
    }

    public static void showWindowSize(String filePath) throws Exception {
        String command = STR."\{ffprobe} -v error -select_streams v:0 -show_entries stream=width,height -of csv=p=0 \"\{filePath}\"";
        System.err.println(command);
        Process process = runtime.exec(command);
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.out.println(STR."FFmpeg -->: \{line}");
        }

        process.waitFor();

        System.out.println("Conversion completed successfully.");

    }

}
