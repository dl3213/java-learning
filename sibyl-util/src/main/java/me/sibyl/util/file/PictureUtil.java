package me.sibyl.util.file;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Classname PictureUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/13 20:28
 */

public class PictureUtil {

    public static void main2(String[] args) throws Exception {
        String path = "D:\\85704476-2.png";

        byte[] bytes = null;

        File file = new File(path);
        System.err.println(file.length());// 1024 b = 1kb

        FileInputStream inputStream = new FileInputStream(path);

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage, "png", byteOutputStream);
        bytes = byteOutputStream.toByteArray();
        System.err.println(bytes.length);

        inputStream.close();
        byteOutputStream.close();

        String outPath = "D:\\" + System.currentTimeMillis() + ".png";

        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
        imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
        imageOutput.close();

    }

    public static void main20230313(String[] args) throws Exception {
        String path = "D:\\85704476-2.png";

        File file = new File(path);
        System.err.println("file = " + file.length());// 1024 b = 1kb
        FileInputStream inputStream = new FileInputStream(path);

        byte[] bytes = getByte(inputStream);

        System.err.println("bytes = " + bytes.length);

        if (bytes.length >= (256 * 1024)) {
            // verion-1
            ByteArrayInputStream intputStream = new ByteArrayInputStream(bytes);
            //Thumbnails.of(inputStream).scale(0.25f).outputQuality(1.0f).toOutputStream(baos);
            float i = ((float) (256 * 1024)) / ((float) bytes.length);
            System.err.println(i);
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(intputStream).scale(i);
            BufferedImage bufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            bytes = outputStream.toByteArray();
            System.err.println(bytes.length);
        }


        //version-2
//        BufferedImage templateImage = ImageIO.read(new File(path));
//        int height = templateImage.getHeight();
//        int width = templateImage.getWidth();
//        float scale = 0.1f;
//        int doWithHeight = (int) (scale * height);
//        int dowithWidth = (int) (scale * width);
//        BufferedImage finalImage = new BufferedImage(dowithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);
//        finalImage.getGraphics()
//                .drawImage(templateImage.getScaledInstance(dowithWidth, doWithHeight, java.awt.Image.SCALE_AREA_AVERAGING), 0, 0, null);
//        //图片
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
//        encoder.encode(finalImage);
//        outputStream.close();
//        System.err.println(outputStream.toByteArray().length);

        String outPath = "D:\\" + System.currentTimeMillis() + ".png";
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
        imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
        imageOutput.close();
    }

    public static byte[] getByte(FileInputStream inputStream) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inputStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inputStream.close();
        outStream.close();
        return outStream.toByteArray();
    }


    public static void main(String[] args) throws Exception {

        String path = "D:\\图片1.png";

        File file = new File(path);
        System.err.println("file = " + file.length());// 1024 b = 1kb
        FileInputStream inputStream = new FileInputStream(path);

        byte[] bytes = getByte(inputStream);

        int maxSize = 1024 * 1024;
        if (bytes.length >= maxSize) {
            FileInputStream input = new FileInputStream(path);
            BufferedImage bufferedImage = ImageIO.read(input);

            ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            float i = ((float) maxSize) / ((float) bytes.length);
            iwp.setCompressionQuality(i);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageOutputStream imgOutStrm = ImageIO.createImageOutputStream(output);
            writer.setOutput(imgOutStrm);
            IIOImage image = new IIOImage(bufferedImage, null, null);
            writer.write(null, image, iwp);
            writer.dispose();

            bytes = output.toByteArray();
            input.close();
            output.close();
            imgOutStrm.close();
        }

        String outPath = "D:\\图片1" + System.currentTimeMillis() + ".png";
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
        imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
        imageOutput.close();
    }

    @SneakyThrows
    public static void main20230317(String[] args) {

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
