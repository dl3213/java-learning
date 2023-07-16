package me.sibyl.media;

import lombok.SneakyThrows;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ffmpegUtil {

    @SneakyThrows
    public static void main2(String[] args) {

        File file = new File("D:/4code/1/ffmpeg/input.mp4");
        System.err.println(file.exists());
        String command = "ffmpeg -re -i D:/4code/1/ffmpeg/input.mp4  -c copy -f segment -segment_format mp4 D:/4code/1/ffmpeg/test_outpout-%d.mp4";

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    public static void main1(String[] args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> meta = new ArrayList<String>();
        meta.add("ffmpeg");
        meta.add("-re");
        meta.add("-i");
        meta.add("D:/4code/1/ffmpeg/input.mp4");
        meta.add("-c");
        meta.add("copy");
        meta.add("-f");
        meta.add("segment");
        meta.add("-segment_format");
        meta.add("mp4");
        meta.add("D:/4code/1/ffmpeg/test_outpout-%d.mp4");
        processBuilder.command(meta);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
        String ss = null;
        while ((ss = input.readLine()) != null) {
            System.out.println(ss);
        }
        process.waitFor();
        System.out.println("正常输出");
    }

    /**
     *https://blog.csdn.net/seeblood/article/details/125926490
     */

    @SneakyThrows
    public static void main(String[] args) {
        avutil.av_log_set_level(avutil.AV_LOG_INFO);
        FFmpegLogCallback.set();

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new FileInputStream("D:/4code/1/ffmpeg/input.mp4"));
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("D:/4code/1/ffmpeg/output.m3u8", grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());

        recorder.setFormat("hls");
        recorder.setOption("hls_time", "5");
        recorder.setOption("hls_list_size", "0");
        recorder.setOption("hls_flags", "delete_segments");
        recorder.setOption("hls_delete_threshold", "1");
        recorder.setOption("hls_segment_type", "mp4");
        recorder.setOption("hls_segment_filename", "test_outpout-%d.mp4");

        // http属性
        recorder.setOption("method", "POST");

        recorder.setFrameRate(25);
        recorder.setGopSize(2 * 25);
        recorder.setVideoQuality(1.0);
        recorder.setVideoBitrate(10 * 1024);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.start();

        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            try {
                recorder.record(frame);
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
        recorder.setTimestamp(grabber.getTimestamp());
        recorder.close();
        grabber.close();
    }
}
