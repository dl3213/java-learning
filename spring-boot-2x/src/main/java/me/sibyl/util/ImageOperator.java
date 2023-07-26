package me.sibyl.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class ImageOperator {

    @Getter
    private byte[] bytes; // todo 只适用于单一图片，且常规大小图片
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
    @Getter
    @Setter
    private Integer degree;

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\test\\图片1.png";
        File file = new File(filePath);
        System.err.println(file.getAbsolutePath());
        System.err.println(file.length());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] test = null;
        ImageOperator.builder()
                //1.先设置输入源：三选一
//                .file(file) //三选一
//                .inputStreamWithClose(new FileInputStream(file)) //三选一
//                .bytes(ImageOperator.getByteWithCloseStream(new FileInputStream(file))) //三选一
                .input(file)
//                .input(new FileInputStream(file))
//                .input(ImageOperator.getByteWithCloseStream(new FileInputStream(file)))
                .waterText("test")
                //2.设置内容
                //  颜色
//                .color(new Color(0, 0, 0, 255))
//                .color(context -> new Color(0,0,0,255))
//                .fontSize(50)
//                .fontSize(context ->
//                        context.getImage().getHeight() > context.getImage().getWidth() ?
//                                (context.getImage().getHeight() / 3 / context.getWaterText().length()) : (context.getImage().getWidth() / 2) / context.getWaterText().length())
//                .font(new Font("微软雅黑", Font.ITALIC, 75))
//                .font(context -> new Font("微软雅黑", Font.ITALIC, context.getFontSize()))
//                .x(120)
//                .x(context -> context.getImage().getWidth() / 2)
//                .y(20)
//                .y(context -> context.getImage().getHeight() / 2)
//                .degree(45)// 水平顺时针
//                .degree(context -> context.getImage().getHeight() / 2)
                //3.输出，四选一
                .output(file.getAbsolutePath())//四选一，无返回值，输出到指定文件路径
//                .output(file)//四选一，无返回值，输出到指定文件
//                .output(outputStream)//四选一，无返回值，输出到指定输出流
//                .output()//四选一,有返回值，输出 图片byte[]
        ;
        //相同的属性，后覆盖前，大步骤顺序固定，其他步骤：字大小->字样式(因为字样式里才是最终的字大小)

//        String outPath = filePath.replace(".", System.currentTimeMillis() + ".");
//        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
//        imageOutput.write(test, 0, test.length);//将byte写入硬盘
//        imageOutput.close();
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

    public ImageOperator input(byte[] bytes) {
        this.bytes = bytes;
        imageBuilder();
        return this;
    }

    public ImageOperator inputStreamWithClose(InputStream inputStream) {
        this.bytes = getByteWithCloseStream(inputStream);
        imageBuilder();
        return this;
    }

    public ImageOperator input(InputStream inputStream) {
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

    public ImageOperator input(File file) {
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
     * filePath 输出到指定文件路径
     */
    public void output(String filePath) {
        if (StringUtils.isBlank(this.waterText)) {
            return;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText();

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);) {
            fileOutputStream.write(this.bytes, 0, this.bytes.length);//将byte写入硬盘
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片添加水印
     * file 输出到指定文件
     */
    public void output(File file) {
        if (StringUtils.isBlank(this.waterText)) {
            return;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            fileOutputStream.write(this.bytes, 0, this.bytes.length);//将byte写入硬盘
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片添加水印
     * outputStream 输出到指定输出流
     */
    public void output(OutputStream outputStream) {
        if (StringUtils.isBlank(this.waterText)) {
            return;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText();

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

    /**
     * 图片添加水印
     * 返回处理之后的图片byte[]
     */
    public byte[] output() {
        if (StringUtils.isBlank(this.waterText)) {
            return null;
        }
        this.defaultValidatedBytes();
        this.bytes = this.addWaterText();
        return this.bytes;
    }

    /**
     * 必要校验，byte[] 不能为空
     */
    private void defaultValidatedBytes() {
        if (Objects.isNull(this.bytes)) {
            throw new RuntimeException("文件byte为空");
        }
        this.defaultValidated();
    }

    /**
     * 默认校验：未设置的参数提供默认值
     */
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
            this.fontSize = this.image.getHeight() > this.image.getWidth() ?
                    (this.image.getHeight() / 3 / this.waterText.length()) : (this.image.getWidth() / 2) / this.waterText.length();
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

    /**
     * 图片水印操作
     */
    public byte[] addWaterText() {

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(this.bytes);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
        ) {

            String formatter = getFormatter(inputStream);
            if (StringUtils.isBlank(formatter)) {
                System.err.println("非图片文件");
                return this.bytes;
            }
            if (StringUtils.isBlank(this.waterText)) {
                System.err.println("无文字输入");
                return this.bytes;
            }

            imageOperate(formatter, output);

            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return this.bytes;
        }
    }

    /**
     * 图片绘制操作，并输出到输出流
     */
    private void imageOperate(String formatter, OutputStream outputStream) throws IOException {
        //图片绘制
        penDrawString();
        ImageIO.write(this.image, formatter, outputStream);
    }

    /**
     * 图片绘制操作
     */
    public void penDrawString() {
        // 创建画笔
        Graphics2D pen = this.image.createGraphics();
        pen.setColor(this.color);
        pen.setFont(this.font);

        if (null != this.degree) {
            pen.rotate(Math.toRadians(this.degree), this.x, this.y);
        }

        // 这三个参数分别为你的文字内容，起始位置横坐标(px)，纵坐标位置(px)。
        pen.drawString(this.waterText, this.x, this.y);
        pen.dispose();
    }

    /**
     * 输入流转byte[],并关闭流
     */
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

    /**
     * ByteArrayInputStream转byte，并重置流
     */
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

    public ImageOperator degree(int degree) {
        imageBuilder();
        this.degree = degree;
        return this;
    }

    public ImageOperator degree(Function<ImageOperator, Integer> function) {
        imageBuilder();
        this.degree = function.apply(this);
        return this;
    }

    /**
     * 设置水印位置 y , 输入数字
     */
    public ImageOperator y(int y) {
        imageBuilder();
        this.y = y;
        return this;
    }

    /**
     * 设置水印位置 y, 提供内部属性
     */
    public ImageOperator y(Function<ImageOperator, Integer> yFunction) {
        imageBuilder();
        this.y = yFunction.apply(this);
        return this;
    }

    /**
     * 设置水印位置 x , 输入数字
     */
    public ImageOperator x(int x) {
        imageBuilder();
        this.x = x;
        return this;
    }

    /**
     * 设置水印位置 x, 提供内部属性
     */
    public ImageOperator x(Function<ImageOperator, Integer> xFunction) {
        imageBuilder();
        this.x = xFunction.apply(this);
        return this;
    }

    /**
     * 设置水印 文字大小 , 输入数字
     */
    public ImageOperator fontSize(Integer fontSize) {
        imageBuilder();
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 设置水印 文字大小 , 提供内部属性
     */
    public ImageOperator fontSize(Function<ImageOperator, Integer> function) {
        imageBuilder();
        this.fontSize = function.apply(this);
        return this;
    }

    /**
     * 设置水印 文字样式 , 输入Font类
     */
    public ImageOperator font(Font font) {
        imageBuilder();
        this.font = font;
        return this;
    }

    /**
     * 设置水印 文字样式 , 提供内部属性
     */
    public ImageOperator font(Function<ImageOperator, Font> function) {
        imageBuilder();
        this.font = function.apply(this);
        return this;
    }

    /**
     * 设置水印 文字颜色 , 输入Color类
     */
    public ImageOperator color(Color color) {
        imageBuilder();
        this.color = color;
        return this;
    }

    /**
     * 设置水印 文字颜色 , 提供内部属性
     */
    public ImageOperator color(Function<ImageOperator, Color> function) {
        imageBuilder();
        this.color = function.apply(this);
        return this;
    }

    /**
     * 内部构建 BufferedImage ，提供内部属性时必须不为空
     */
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
