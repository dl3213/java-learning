package me.sibyl.util.file;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * @Classname ImageOperator
 * @Description ImageUtil
 * @Date 2023/3/28 14:37
 * @Author by Qin Yazhi
 */

public class ImageOperator {

    @Getter
    private byte[] bytes;
    @Getter
    private BufferedImage image;
    @Getter
    @Setter
    private String waterText;
    @Getter
    @Setter
    private Color color;
    @Getter
    @Setter
    private Integer fontSize;
    @Getter
    @Setter
    private Font font;
    @Getter
    @Setter
    private Integer x;
    @Getter
    @Setter
    private Integer y;

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\4test\\TPbQ2K2l.jpeg";
        File file = new File(filePath);
        System.err.println(file.getAbsolutePath());
        System.err.println(file.length());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] test = ImageOperator.builder()
                //1.先设置输入源：三选一
//                .file(file) //三选一
//                .inputStreamWithClose(new FileInputStream(file)) //三选一
                .bytes(ImageOperator.getByteWithCloseStream(new FileInputStream(file))) //三选一
                .waterText("test")
                //2.设置内容
                //  颜色
//                .color(new Color(0, 0, 0, 255))
//                .color(context -> new Color(0,0,0,255))
//                .fontSize(50)
                .fontSize(context ->
                        context.getImage().getHeight() > context.getImage().getWidth() ?
                                (context.getImage().getHeight() / 3 / context.getWaterText().length()) : (context.getImage().getWidth() / 2) / context.getWaterText().length())

//                .font(new Font("微软雅黑", Font.ITALIC, 75))
//                .font(context -> new Font("微软雅黑", Font.ITALIC, context.getFontSize()))
//                .x(120)
//                .x(context -> context.getImage().getWidth() / 2)
//                .y(20)
//                .y(context -> context.getImage().getHeight() / 2)
                //3.输出，四选一
//                .out(file.getAbsolutePath())//四选一，无返回值
//                .out(file)//四选一，无返回值
//                .out(outputStream)//四选一，无返回值
                .out()//四选一,有返回值
                ;
        //相同的属性，后覆盖上，大步骤顺序固定，其他步骤：字体大小->子样式

        String outPath = filePath.replace(".", System.currentTimeMillis() + ".");
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
        imageOutput.write(test, 0, test.length);//将byte写入硬盘
        imageOutput.close();
        System.err.println("end");
    }

    public ImageOperator waterText(String waterText) {
        this.waterText = waterText;
        return this;
    }

    public ImageOperator bytes(byte[] bytes) {
        this.bytes = bytes;
        imageBuilder();
        return this;
    }

    public ImageOperator inputStreamWithClose(InputStream inputStream) {
        this.bytes = getByteWithCloseStream(inputStream);
        imageBuilder();
        return this;
    }

    public ImageOperator file(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            this.bytes = getByteWithCloseStream(fileInputStream);
            imageBuilder();
        } catch (Exception e) {
        }

        return this;
    }

    public static ImageOperator builder() {
        return new ImageOperator();
    }

    private ImageOperator() {
    }

    /**
     * 图片添加水印
     * filePath 输出文件路径
     * waterText 文字水印
     */
    public void out(String filePath) {
        if (StringUtils.isBlank(this.waterText)) {
            return;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText(this.bytes, this.waterText, this.color, this.font, this.x, this.y);

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);) {
            fileOutputStream.write(this.bytes, 0, this.bytes.length);//将byte写入硬盘
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void out(File file) {
        if (StringUtils.isBlank(this.waterText)) {
            return;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText(this.bytes, this.waterText, this.color, this.font, this.x, this.y);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            fileOutputStream.write(this.bytes, 0, this.bytes.length);//将byte写入硬盘
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void out(OutputStream outputStream) {
        if (StringUtils.isBlank(this.waterText)) {
            return;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText(this.bytes, this.waterText, this.color, this.font, this.x, this.y);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bytes);) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = byteArrayInputStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            //byte[] bytes = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] out() {
        if (StringUtils.isBlank(this.waterText)) {
            return null;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText(this.bytes, this.waterText, this.color, this.font, this.x, this.y);
        return this.bytes;
    }

    private void defaultValidatedBytes() {
        if (Objects.isNull(this.bytes)) {
            throw new RuntimeException("文件byte为空");
        }
        this.defaultValidated();
    }

    private void defaultValidated() {
        if (Objects.isNull(this.image)) {
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bytes);) {
                this.image = ImageIO.read(byteArrayInputStream);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (Objects.isNull(this.color)) {
            this.color = new Color(255, 0, 0, 255);
        }
        if (Objects.isNull(this.fontSize)) {
            this.fontSize = (this.image.getWidth() / 4) / this.waterText.length();
        }
        if (Objects.isNull(this.font)) {
            this.font = new Font("微软雅黑", Font.ITALIC, this.fontSize);
        }
        if (Objects.isNull(this.x)) {
            this.x = 0;
        }
        if (Objects.isNull(this.y)) {
            this.y = this.fontSize;
        }
    }

    private void imageOperate(String waterText, Color color, Font font, int x, int y, OutputStream fos, String formatter) throws IOException {
        //图片绘制
        penDrawString(this.image, waterText, color, font, x, y);
        ImageIO.write(this.image, formatter, fos);
    }

    public byte[] addWaterText(byte[] srcBytes, String waterText, Color color, Font font, int x, int y) {

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(srcBytes);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
        ) {

            String formatter = getFormatter(inputStream);
            if (StringUtils.isBlank(formatter)) {
                System.err.println("非图片文件");
                return srcBytes;
            }
            if (StringUtils.isBlank(waterText)) {
                System.err.println("无文字输入");
                return srcBytes;
            }

            imageOperate(waterText, color, font, x, y, output, formatter);

            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return srcBytes;
        }
    }


    public static void penDrawString(BufferedImage image, String waterText, Color color, Font font, int x, int y) {
        // 创建画笔
        Graphics2D pen = image.createGraphics();
        pen.setColor(color);
        pen.setFont(font);
        // 这三个参数分别为你的文字内容，起始位置横坐标(px)，纵坐标位置(px)。
        pen.drawString(waterText, x, y);
        pen.dispose();
    }

    public static byte[] getByteWithCloseStream(InputStream inputStream) {

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
                inputStream.close();
            }
            outStream.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 获取图片格式
     */
    public String getFormatter(InputStream is) {// 检查是否是图片
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(is);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) return null;
            ImageReader reader = (ImageReader) iter.next();
            String formatter = reader.getFormatName();
            iis.close();// 关闭图片输入流
            is.close();//因为下文还会使用，所以现不关闭流 todo
            return formatter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageOperator y(Integer y) {
        imageBuilder();
        this.y = y;
        return this;
    }

    public ImageOperator y(Function<ImageOperator, Integer> yFunction) {
        imageBuilder();
        this.y = yFunction.apply(this);
        return this;
    }

    public ImageOperator x(Integer x) {
        imageBuilder();
        this.x = x;
        return this;
    }

    public ImageOperator x(Function<ImageOperator, Integer> xFunction) {
        imageBuilder();
        this.x = xFunction.apply(this);
        return this;
    }

    public ImageOperator fontSize(Integer fontSize) {
        imageBuilder();
        this.fontSize = fontSize;
        return this;
    }

    public ImageOperator fontSize(Function<ImageOperator, Integer> function) {
        imageBuilder();
        this.fontSize = function.apply(this);
        return this;
    }

    public ImageOperator font(Font font) {
        imageBuilder();
        this.font = font;
        return this;
    }

    public ImageOperator font(Function<ImageOperator, Font> function) {
        imageBuilder();
        this.font = function.apply(this);
        return this;
    }

    public ImageOperator color(Color color) {
        imageBuilder();
        this.color = color;
        return this;
    }

    public ImageOperator color(Function<ImageOperator, Color> function) {
        imageBuilder();
        this.color = function.apply(this);
        return this;
    }

    private void imageBuilder() {
        if (Objects.isNull(this.image)) {
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bytes);) {
                this.image = ImageIO.read(byteArrayInputStream);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
