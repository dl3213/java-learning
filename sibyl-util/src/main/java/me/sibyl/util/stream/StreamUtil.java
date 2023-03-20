package me.sibyl.util.stream;

import lombok.SneakyThrows;
import me.sibyl.util.file.FileUtil;

import java.io.*;

/**
 * @Classname TreamUtil
 * @Description TreamUtil
 * @Date 2023/3/20 10:32
 * @Author by Qin Yazhi
 */

public class StreamUtil {

    @SneakyThrows
    public static void main(String[] args) {

        try (CloseTest closeTest = new CloseTest()) {
            int i = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println();
    }


    static class CloseTest implements Closeable {

        @Override
        public void close() throws IOException {
            System.err.println(" CloseTest is closing...");
        }
    }

    public static void close(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

}
