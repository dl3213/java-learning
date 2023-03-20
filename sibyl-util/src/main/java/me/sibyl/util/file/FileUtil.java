package me.sibyl.util.file;

import lombok.SneakyThrows;
import me.sibyl.util.stream.StreamUtil;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @Classname FileUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/16 20:20
 */
public class FileUtil {

    //    public static String testFilePath = "D:\\4test\\gif\\73500614.gif";
//    public static String testFilePath = "D:\\4test\\85704476-2.png";
    public static String testFilePath = "D:\\图片1.png";

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File(testFilePath);
        System.err.println("file.length() = " + file.length());
        ByteArrayInputStream byteInputStream = FileUtil.getByteInputStream(file);
        System.err.println("file type = " + FileUtil.getFileTypeFromByteStream(byteInputStream));

        byte[] sourceBytes = FileUtil.getByteWithResetStream(byteInputStream);
//        System.err.println("sourceBytes.length = " + sourceBytes.length);
//        System.err.println("sourceBytes.type = " + FileUtil.getFileTypeFromByte(sourceBytes));

//        BufferedImage inputImage = ImageIO.read(new File(testFilePath));
        // 图片类型转换 : 所有图片先转jpg再压缩,效果更好
//        String outputFile = testFilePath.replace(".", System.currentTimeMillis() + ".");
//        System.err.println("outputFile = " + outputFile);
//        File output = new File(outputFile);
//        ImageIO.write(inputImage, "jpg", output);
//        System.err.println("outputFile type =" + FileUtil.getFileTypeFromByteStream(FileUtil.trans2ByteStream(new FileInputStream(outputFile))));

        //图片压缩
        byte[] zipPic = FileUtil.zipPic(sourceBytes, FileUtil.getFileTypeFromByteStream(byteInputStream), 0.1f);
        System.err.println("zipPic.length = " + zipPic.length);
        System.err.println("zipPic.type = " + FileUtil.getFileTypeFromByte(zipPic));

//        fileInputStream.close();

        //压缩后的图片写入文件
//        String outPath = "D:\\" + System.currentTimeMillis() + ".png";
//        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
//        imageOutput.write(zipPic, 0, zipPic.length);//将byte写入硬盘
//        imageOutput.close();
    }

    public static byte[] zipPic(byte[] sourceBytes, String picType, float quality) {

        byte[] bytes = null;
        try (
                ByteArrayInputStream input = new ByteArrayInputStream(sourceBytes);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
        ) {
            BufferedImage bufferedImage = ImageIO.read(input);
            ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);

            ImageOutputStream imgOutStrm = ImageIO.createImageOutputStream(output);
            writer.setOutput(imgOutStrm);
            IIOImage image = new IIOImage(bufferedImage, null, null);
            writer.write(null, image, iwp);

            writer.dispose();
            bytes = output.toByteArray();

            input.close();
            output.close();
            imgOutStrm.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static byte[] getByteWithResetStream(ByteArrayInputStream inputStream) {

        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
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
            if (inputStream.markSupported()) {
                inputStream.reset();
            } else {
                throw new Exception("markSupported = false");
            }
            outStream.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] getByte(InputStream inputStream) {

        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ByteArrayInputStream trans2ByteStream(InputStream inputStream) {

        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
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
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();

    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        // CAD
        mFileTypes.put("41433130", "dwg");
        mFileTypes.put("38425053", "psd");
        // 日记本
        mFileTypes.put("7B5C727466", "rtf");
        mFileTypes.put("3C3F786D6C", "xml");
        mFileTypes.put("68746D6C3E", "html");
        // 邮件
        mFileTypes.put("44656C69766572792D646174653A", "eml");
        mFileTypes.put("D0CF11E0", "doc");
        //excel2003版本文件
        mFileTypes.put("D0CF11E0", "xls");
        mFileTypes.put("5374616E64617264204A", "mdb");
        mFileTypes.put("252150532D41646F6265", "ps");
        mFileTypes.put("255044462D312E", "pdf");
        mFileTypes.put("504B0304", "docx");
        //excel2007以上版本文件
        mFileTypes.put("504B0304", "xlsx");
        mFileTypes.put("52617221", "rar");
        mFileTypes.put("57415645", "wav");
        mFileTypes.put("41564920", "avi");
        mFileTypes.put("2E524D46", "rm");
        mFileTypes.put("000001BA", "mpg");
        mFileTypes.put("000001B3", "mpg");
        mFileTypes.put("6D6F6F76", "mov");
        mFileTypes.put("3026B2758E66CF11", "asf");
        mFileTypes.put("4D546864", "mid");
        mFileTypes.put("1F8B08", "gz");
    }

//    public static String getFileType(String filePath) throws FileNotFoundException {
//        File file = new File(filePath);
//        if (file.isDirectory()) {
//            throw new RuntimeException("当前路径是目录");
//        }
//        return getFileType(new FileInputStream(file));
//    }

//    public static String urlFileType(String fileUrl) throws Exception {
//        log.info("url:{}获取文件类型", fileUrl);
//        BufferedInputStream bis = null;
//        HttpURLConnection urlconnection = null;
//        URL url = null;
//        url = new URL(fileUrl);
//        urlconnection = (HttpURLConnection) url.openConnection();
//        urlconnection.connect();
//        String fileType = getFileType(urlconnection.getInputStream());
//        log.info("url:{}获取文件类型：{}", fileUrl, fileType);
//        return fileType;
//    }

    public static String getFileTypeFromByte(byte[] src) {
        byte[] to = Arrays.copyOfRange(src, 0, 4);
        String key = bytesToHexString(to);
        if (StringUtils.startsWith(key, "FFD8FF")) {
            key = key.substring(0, 6);
        }
        return mFileTypes.get(key);
    }

    public static String getFileTypeFromByteStream(ByteArrayInputStream byteArrayInputStream) {
        //InputStream copy = copyByteStream(byteArrayInputStream);
        String fileHeader = getFileHeader(byteArrayInputStream);
        return mFileTypes.get(fileHeader);
    }

    public static ByteArrayInputStream copyByteStream(ByteArrayInputStream inputStream) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            if (inputStream.markSupported()) {
                inputStream.reset();
            } else {
                System.err.println("inputStream.markSupported = false");
                System.err.println("inputStream closed");
                inputStream.close();
            }
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileHeader(InputStream inputStream) {
        InputStream is = null;
        String value = null;
        try {
            is = inputStream;
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。
             * int read(byte[] b) 从此输入流中将最多 b.length个字节的数据读入一个 byte 数组中。
             * int read(byte[] b, int off, int len)从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);

            if (inputStream.markSupported()) {
                inputStream.reset();
            } else {
                System.err.println("inputStream.markSupported = false");
                System.err.println("inputStream closed");
                inputStream.close();
            }
        } catch (IOException e) {
        } finally {
            StreamUtil.close(is);
        }
        if (StringUtils.startsWith(value, "FFD8FF")) {
            value = value.substring(0, 6);
        }
        return value;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }


    public static ByteArrayInputStream getByteInputStream(File file) {

        try (
                FileInputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ) {
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
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
