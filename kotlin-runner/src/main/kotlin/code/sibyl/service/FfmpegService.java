package code.sibyl.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.StringTemplate.STR;

public class FfmpegService {

    public final static String basePath = "D:\\4code\\4dev\\ffmpeg-7.1-essentials_build\\bin\\";

    public final static String ffmpeg = STR."\{basePath}ffmpeg.exe";
    public final static String ffprobe = STR."\{basePath}ffprobe.exe";

    public final static Runtime runtime = Runtime.getRuntime();

    public static void main(String[] args) throws Exception {



        FfmpegService.copy(" ", " ", "02:25:26", "02:31:11");
//        FfmpegService.showWindowSize("E:\\4me\\video\\1.mp4");
    }



    public static void copy(String fromFile, String toFile, String stateTime, String endTime) throws Exception {

        Process process = runtime.exec(STR."\{ffmpeg} -ss \{stateTime} -to \{endTime} -i \{fromFile}  -c copy \{toFile}");
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.out.println(STR."FFmpeg error: \{line}");
        }
        process.waitFor();
        System.out.println("Conversion completed successfully.");
    }

    public static void showWindowSize(String filePath) throws Exception {
        String command = STR."\{ffprobe} -v error -select_streams v:0 -show_entries stream=width,height -of csv=p=0 \{filePath}";
        System.err.println(command);
        Process process = runtime.exec(command);
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.out.println(STR."FFmpeg error: \{line}");
        }

        process.waitFor();

        System.out.println("Conversion completed successfully.");

    }

}
