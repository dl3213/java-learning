package me.sibyl.util.file;

import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Classname Test
 * @Description Test
 * @Date 2023/3/17 9:45
 * @Author by Qin Yazhi
 */
public class Test {
    @SneakyThrows
    public static void main(String[] args) {

        String path = "D:\\图片1.png";

        File file = new File(path);
        System.err.println("file = " + file.length());// 1024 b = 1kb
        FileInputStream inputStream = new FileInputStream(path);

        byte[] bytes = FileUtil.getByte(inputStream);
        System.err.println(bytes.length);
        int maxSize = 256 << 10;//等效256*1024
        if (bytes.length > (maxSize)) {
            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bytes);
            double i = ((double) (maxSize)) / ((double) bytes.length);
            System.err.println(i);
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(inputStream2).scale(i);
            BufferedImage bufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            bytes = outputStream.toByteArray();

            inputStream.close();
            outputStream.close();
        }

        String outPath = "D:\\图片1" + System.currentTimeMillis() + ".png";
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
        imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
        imageOutput.close();

    }

}
