package me.sibyl.util.ocr;

import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

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
    public static void main2(String[] args) {

        //图片路径
        String path = "D:\\4test\\批注 2023-03-21 221001.png";
        //语言位置
        String languagePath = "D:\\4code\\4java\\workspace\\sibyl-microserivce\\sibyl-microservice\\sibyl-util\\src\\main\\resources\\tess4j";
        File file = new File(path);
        Tesseract instance = new Tesseract();
        //设置训练库位置
        //git clone https://github.com/tesseract-ocr/tessdata.git
        instance.setDatapath(languagePath);
        //chi_sim:简体中文，eng根据需求选择语言库
        instance.setLanguage("chi_sim");
        String result = null;
        try{
            result = instance.doOCR(file);
        }catch (TesseractException e){
            e.printStackTrace();
        }
        System.out.println("图片中的文字为:"+result);
    }

}
