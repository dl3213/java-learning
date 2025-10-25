package code.sibyl;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;

public class Demo {
    public static void main(String[] args) {

        // 图片地址
        String imgPath = "D:\\z\\page_1.png";
        File imgFile = new File(imgPath);

        // 语言库位置
        String languageDataPath = "D:\\4code\\4dev\\workspace\\tessdata_best";
        Tesseract tesseract = new Tesseract();
        // 设置训练库位置
        tesseract.setDatapath(languageDataPath);
        // 中文：chi_sim
        // 英文：eng
        // 中英文：chi_sim+eng
        tesseract.setLanguage("chi_sim+eng");
        String result = null;
        try {
            result = tesseract.doOCR(imgFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        System.out.println(result);
    }
}
