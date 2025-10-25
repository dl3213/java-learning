package code.sibyl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFToImage {
    public static void convertPDFToImages(String pdfPath, String outputDir) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage img = renderer.renderImageWithDPI(page, 300); // Adjust DPI as needed
                ImageIO.write(img, "PNG", new File(outputDir + "/page_" + (page + 1) + ".png"));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        convertPDFToImages("D:\\z\\仪征东闸钢板桩施工.pdf", "D:\\z");
    }
}

