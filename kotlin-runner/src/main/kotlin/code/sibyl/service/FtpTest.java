package code.sibyl.service;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;

public class FtpTest {

    public static void main(String[] args) {
        File file = new File("C:\\Users\\18778\\Downloads\\125676108_p0.jpg");
        FtpUtil.uploadFile("/home/ubuntu/125676108_p0-12222.jpg", FileUtil.getInputStream(file));
    }
}
