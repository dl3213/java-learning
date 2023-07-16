package me.sibyl.controller;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.service.VideoHttpRequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
@Slf4j
public class StreamVideoController {

    private final String url = "D:\\4test\\BV16V411g7w5-1183064141.mp4";
    private final String url2 = "E:\\电影\\你的名字.Your.Name.2016.BD1080P.X264.AAC.Japanese.CHS.Mp4Ba\\你的名字.Your.Name.2016.BD1080P.X264.AAC.Japanese.CHS.Mp4Ba.mp4";
    private final VideoHttpRequestHandler videoHttpRequestHandler;

    @RequestMapping(value = "/player", method = {RequestMethod.GET, RequestMethod.POST})
    public void getPlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Path path = Paths.get(url2);
        if (Files.exists(path)) {
            String mimeType = Files.probeContentType(path);
            log.info(mimeType);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(VideoHttpRequestHandler.ATTR_FILE, path);
            videoHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }


    /**
     * 目录路径,这个路径需要包含test.info文件，test.key文件和test.mp4文件
     */
    private static final String PATH = "D:\\4code\\1\\ffmpeg\\";


    @PostMapping("upload/{fileName}")
    public void upload(HttpServletRequest request, @PathVariable("fileName") String fileName) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        FileWriter writer = new FileWriter(PATH + fileName);
        writer.writeFromStream(inputStream);
        IoUtil.close(inputStream);
    }

    /**
     * 预览加密文件
     */
    @PostMapping("preview/{fileName}")
    public void preview(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        FileReader fileReader = new FileReader(PATH + fileName);
        fileReader.writeToStream(response.getOutputStream());
    }

    /**
     * 预览加密文件
     */
    @GetMapping("download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        FileReader fileReader = new FileReader(PATH + fileName);
        fileReader.writeToStream(response.getOutputStream());
    }
}
