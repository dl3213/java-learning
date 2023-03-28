package me.sibyl.util.file;

import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author dyingleaf3213
 * @Classname Test
 * @Description TODO
 * @Create 2023/03/28 22:56
 */

public class Test {

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
//                .color(context -> new Color(255,0,0,255))
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
}
