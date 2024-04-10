package me.sibyl.util;

import cn.hutool.core.collection.CollUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ExcelToPdfUtil {

    private static String fontPath = "/usr/share/fonts/simsun.ttc,0";

    public static void main(String[] args) throws Exception {
        String inputExcelPath = "D:\\4dev\\workspace\\tanghe-BI\\file\\应收逾期推送记录\\2024-04-09\\应收逾期-2024-04-09.xlsx";
        String outputPdfPath = "D:\\4dev\\workspace\\tanghe-BI\\file\\应收逾期推送记录\\2024-04-09\\应收逾期-2024-04-09.pdf";

//        convertExcelToPdf(inputExcelPath, outputPdfPath);

        ExcelToPdfUtil.excelToPdf(inputExcelPath, outputPdfPath, ".xlsx");
    }

    public static void excelToPdf(String excelPath, String pdfPath, String excelSuffix) throws Exception {
        try (InputStream in = Files.newInputStream(Paths.get(excelPath)); OutputStream out = Files.newOutputStream(Paths.get(pdfPath))) {
            ExcelToPdfUtil.excelToPdf(in, out, excelSuffix);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Excel转PDF并写入输出流
     *
     * @param inStream    Excel输入流
     * @param outStream   PDF输出流
     * @param excelSuffix Excel类型 .xls 和 .xlsx
     * @throws Exception 异常信息
     */
    public static void excelToPdf(InputStream inStream, OutputStream outStream, String excelSuffix) throws Exception {
        // 输入流转workbook，获取sheet
        Sheet sheet = getPoiSheetByFileStream(inStream, 0, excelSuffix);
        // 获取列宽度占比
        float[] widths = getColWidth(sheet);
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        int colCount = widths.length;
        //设置基本字体
//        BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, false);
        // 遍历行
        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (Objects.isNull(row)) {
                // 插入空对象
                for (int i = 0; i < colCount; i++) {
                    table.addCell(createPdfPCell(null, 0, 13f, null));
                }
            } else {
                // 遍历单元格
                for (int columnIndex = 0; (columnIndex < row.getLastCellNum() || columnIndex < colCount) && columnIndex > -1; columnIndex++) {
                    PdfPCell pCell = excelCellToPdfCell(sheet, row.getCell(columnIndex), baseFont);
                    // 是否合并单元格
                    if (isMergedRegion(sheet, rowIndex, columnIndex)) {
                        int[] span = getMergedSpan(sheet, rowIndex, columnIndex);
                        //忽略合并过的单元格
                        boolean mergedCell = span[0] == 1 && span[1] == 1;
                        if (mergedCell) {
                            continue;
                        }
                        pCell.setRowspan(span[0]);
                        pCell.setColspan(span[1]);
                    }
                    table.addCell(pCell);
                }
            }
        }
        // 初始化PDF文档对象
        createPdfTableAndWriteDocument(outStream, table);
    }

    /**
     * 单元格转换，poi cell 转换为 itext cell
     *
     * @param sheet     poi sheet页
     * @param excelCell poi 单元格
     * @param baseFont  基础字体
     * @return com.itextpdf.text.pdf.PdfPCell
     */
    private static PdfPCell excelCellToPdfCell(Sheet sheet, Cell excelCell, BaseFont baseFont) throws Exception {
        if (Objects.isNull(excelCell)) {
            return createPdfPCell(null, 1, 13f, null);
        }
        int rowIndex = excelCell.getRowIndex();
        int columnIndex = excelCell.getColumnIndex();
        // 图片信息
        List<PicturesInfo> infos = getAllPictureInfos(sheet, rowIndex, rowIndex, columnIndex, columnIndex, false);
        PdfPCell pCell;
        if (CollUtil.isNotEmpty(infos)) {
            pCell = new PdfPCell(Image.getInstance(infos.get(0).getPictureData()));
        } else {
            Font excelFont = getExcelFont(sheet, excelCell);
            //设置单元格字体
            com.itextpdf.text.Font pdFont = new com.itextpdf.text.Font(baseFont, excelFont.getFontHeightInPoints(), excelFont.getBold() ? 1 : 0, BaseColor.BLACK);
            Integer border = hasBorder(excelCell) ? null : 0;
            String excelCellValue = getExcelCellValue(excelCell);
            pCell = createPdfPCell(excelCellValue, border, excelCell.getRow().getHeightInPoints(), pdFont);
        }
        // 水平居中
        pCell.setHorizontalAlignment(getHorAlign(excelCell.getCellStyle().getAlignment().getCode()));
        // 垂直对齐
        pCell.setVerticalAlignment(getVerAlign(excelCell.getCellStyle().getVerticalAlignment().getCode()));
        return pCell;
    }

    /**
     * 创建pdf文档，并添加表格
     *
     * @param outStream 输出流，目标文档
     * @param table     表格
     * @throws DocumentException 异常信息
     */
    private static void createPdfTableAndWriteDocument(OutputStream outStream, PdfPTable table) throws DocumentException {
        //设置pdf纸张大小 PageSize.A4 A4横向
        Document document = new Document(new RectangleReadOnly(8420.0F, 5950.0F));
        PdfWriter.getInstance(document, outStream);
        //设置页边距 宽
        document.setMargins(10, 10, 10, 10);
        document.open();
        document.add(table);
        document.close();
    }

    /**
     * Excel文档输入流转换为对应的workbook及获取对应的sheet
     *
     * @param inputStream Excel文档输入流
     * @param sheetNo     sheet编号，默认0 第一个sheet
     * @param excelSuffix 文件类型 .xls和.xlsx
     * @return poi sheet
     * @throws IOException 异常
     */
    public static Sheet getPoiSheetByFileStream(InputStream inputStream, int sheetNo, String excelSuffix) throws IOException {
        Workbook workbook;
        if (excelSuffix.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new HSSFWorkbook(inputStream);
        }
        return workbook.getSheetAt(sheetNo);
    }

    /**
     * 创建itext pdf 单元格
     *
     * @param content       单元格内容
     * @param border        边框
     * @param minimumHeight 高度
     * @param pdFont        字体
     * @return pdf cell
     */
    private static PdfPCell createPdfPCell(String content, Integer border, Float minimumHeight, com.itextpdf.text.Font pdFont) {
        String contentValue = content == null ? "" : content;
        com.itextpdf.text.Font pdFontNew = pdFont == null ? new com.itextpdf.text.Font() : pdFont;
        PdfPCell pCell = new PdfPCell(new Phrase(contentValue, pdFontNew));
        if (Objects.nonNull(border)) {
            pCell.setBorder(Rectangle.BOX);
            pCell.setBorderWidth(1);
        }
        if (Objects.nonNull(minimumHeight)) {
            pCell.setMinimumHeight(minimumHeight);
        }

        return pCell;
    }

    /**
     * excel垂直对齐方式映射到pdf对齐方式
     */
    private static int getVerAlign(int align) {
        switch (align) {
            case 2:
                return com.itextpdf.text.Element.ALIGN_BOTTOM;
            case 3:
                return com.itextpdf.text.Element.ALIGN_TOP;
            default:
                return com.itextpdf.text.Element.ALIGN_MIDDLE;
        }
    }

    /**
     * excel水平对齐方式映射到pdf水平对齐方式
     */
    private static int getHorAlign(int align) {
        switch (align) {
            case 1:
                return com.itextpdf.text.Element.ALIGN_LEFT;
            case 3:
                return com.itextpdf.text.Element.ALIGN_RIGHT;
            default:
                return com.itextpdf.text.Element.ALIGN_CENTER;
        }
    }

    /*============================================== POI获取图片及文本内容工具方法 ==============================================*/

    /**
     * 获取字体
     *
     * @param sheet excel 转换的sheet页
     * @param cell  单元格
     * @return 字体
     */
    private static Font getExcelFont(Sheet sheet, Cell cell) {
        // xls
        if (sheet instanceof HSSFSheet) {
            Workbook workbook = sheet.getWorkbook();
            return ((HSSFCell) cell).getCellStyle().getFont(workbook);
        }
        // xlsx
        return ((XSSFCell) cell).getCellStyle().getFont();
    }

    /**
     * 判断excel单元格是否有边框
     */
    private static boolean hasBorder(Cell excelCell) {
        short top = excelCell.getCellStyle().getBorderTop().getCode();
        short bottom = excelCell.getCellStyle().getBorderBottom().getCode();
        short left = excelCell.getCellStyle().getBorderLeft().getCode();
        short right = excelCell.getCellStyle().getBorderRight().getCode();
        return top + bottom + left + right > 2;
    }

    /**
     * 判断单元格是否是合并单元格
     */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 计算合并单元格合并的跨行跨列数
     */
    private static int[] getMergedSpan(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        int[] span = {1, 1};
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (firstColumn == column && firstRow == row) {
                span[0] = lastRow - firstRow + 1;
                span[1] = lastColumn - firstColumn + 1;
                break;
            }
        }
        return span;
    }

    /**
     * 获取excel中每列宽度的占比
     */
    private static float[] getColWidth(Sheet sheet) {
        int rowNum = getMaxColRowNum(sheet);
        Row row = sheet.getRow(rowNum);
        int cellCount = row.getPhysicalNumberOfCells();
        int[] colWidths = new int[cellCount];
        int sum = 0;

        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                colWidths[i] = sheet.getColumnWidth(i);
                sum += sheet.getColumnWidth(i);
            }
        }

        float[] colWidthPer = new float[cellCount];
        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            colWidthPer[i] = (float) colWidths[i] / sum * 100;
        }
        return colWidthPer;
    }

    /**
     * 获取excel中列数最多的行号
     */
    private static int getMaxColRowNum(Sheet sheet) {
        int rowNum = 0;
        int maxCol = 0;
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            Row row = sheet.getRow(r);
            if (row != null && maxCol < row.getPhysicalNumberOfCells()) {
                maxCol = row.getPhysicalNumberOfCells();
                rowNum = r;
            }
        }
        return rowNum;
    }

    /**
     * poi 根据单元格类型获取单元格内容
     *
     * @param excelCell poi单元格
     * @return 单元格内容文本
     */
    public static String getExcelCellValue(Cell excelCell) {
        if (excelCell == null) {
            return "";
        }
        // 判断数据的类型
        CellType cellType = excelCell.getCellType();

        if (cellType == CellType.STRING) {
            return excelCell.getStringCellValue();
        }
        if (cellType == CellType.BOOLEAN) {
            return String.valueOf(excelCell.getBooleanCellValue());
        }
        if (cellType == CellType.FORMULA) {
            return excelCell.getCellFormula();
        }
        if (cellType == CellType.NUMERIC) {
            //short s = excelCell.getCellStyle().getDataFormat();
            if (DateUtil.isCellDateFormatted(excelCell)) {// 处理日期格式、时间格式
                SimpleDateFormat sdf;
                // 验证short值
                if (excelCell.getCellStyle().getDataFormat() == 14) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd");
                } else if (excelCell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                } else if (excelCell.getCellStyle().getDataFormat() == 22) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                } else {
                    throw new RuntimeException("日期格式错误!!!");
                }
                Date date = excelCell.getDateCellValue();
                return sdf.format(date);
            } else if (excelCell.getCellStyle().getDataFormat() == 0) {
                //处理数值格式
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(excelCell);
            }
        }
        if (cellType == CellType.ERROR) {
            return "非法字符";
        }
        return "";
    }

    /**
     * 获取sheet内的所有图片信息
     *
     * @param sheet        sheet表
     * @param onlyInternal 单元格内部
     * @return 照片集合
     * @throws Exception 异常
     */
    public static List<PicturesInfo> getAllPictureInfos(Sheet sheet, boolean onlyInternal) throws Exception {
        return getAllPictureInfos(sheet, null, null, null, null, onlyInternal);
    }

    /**
     * 根据sheet和单元格信息获取图片
     *
     * @param sheet        sheet表
     * @param minRow       最小行
     * @param maxRow       最大行
     * @param minCol       最小列
     * @param maxCol       最大列
     * @param onlyInternal 是否内部
     * @return 图片集合
     * @throws Exception 异常
     */
    public static List<PicturesInfo> getAllPictureInfos(Sheet sheet, Integer minRow, Integer maxRow, Integer minCol, Integer maxCol, boolean onlyInternal) throws Exception {
        if (sheet instanceof HSSFSheet) {
            return getXLSAllPictureInfos((HSSFSheet) sheet, minRow, maxRow, minCol, maxCol, onlyInternal);
        } else if (sheet instanceof XSSFSheet) {
            return getXLSXAllPictureInfos((XSSFSheet) sheet, minRow, maxRow, minCol, maxCol, onlyInternal);
        } else {
            throw new Exception("未处理类型，没有为该类型添加：GetAllPicturesInfos()扩展方法！");
        }
    }

    private static List<PicturesInfo> getXLSAllPictureInfos(HSSFSheet sheet, Integer minRow, Integer maxRow, Integer minCol, Integer maxCol, Boolean onlyInternal) {
        List<PicturesInfo> picturesInfoList = new ArrayList<>();
        HSSFShapeContainer shapeContainer = sheet.getDrawingPatriarch();
        if (shapeContainer == null) {
            return picturesInfoList;
        }
        List<HSSFShape> shapeList = shapeContainer.getChildren();
        for (HSSFShape shape : shapeList) {
            if (shape instanceof HSSFPicture && shape.getAnchor() instanceof HSSFClientAnchor) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();

                if (isInternalOrIntersect(minRow, maxRow, minCol, maxCol, anchor.getRow1(), anchor.getRow2(), anchor.getCol1(), anchor.getCol2(), onlyInternal)) {
                    HSSFPictureData pictureData = picture.getPictureData();
                    picturesInfoList.add(new PicturesInfo().setMinRow(anchor.getRow1()).setMaxRow(anchor.getRow2()).setMinCol(anchor.getCol1()).setMaxCol(anchor.getCol2()).setPictureData(pictureData.getData()).setExt(pictureData.getMimeType()));
                }
            }
        }
        return picturesInfoList;
    }

    private static List<PicturesInfo> getXLSXAllPictureInfos(XSSFSheet sheet, Integer minRow, Integer maxRow, Integer minCol, Integer maxCol, Boolean onlyInternal) {
        List<PicturesInfo> picturesInfoList = new ArrayList<>();

        List<POIXMLDocumentPart> documentPartList = sheet.getRelations();
        for (POIXMLDocumentPart documentPart : documentPartList) {
            if (documentPart instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) documentPart;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    if (shape instanceof XSSFPicture) {
                        XSSFPicture picture = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = picture.getPreferredSize();

                        if (isInternalOrIntersect(minRow, maxRow, minCol, maxCol, anchor.getRow1(), anchor.getRow2(), anchor.getCol1(), anchor.getCol2(), onlyInternal)) {
                            XSSFPictureData pictureData = picture.getPictureData();
                            picturesInfoList.add(new PicturesInfo().setMinRow(anchor.getRow1()).setMaxRow(anchor.getRow2()).setMinCol(anchor.getCol1()).setMaxCol(anchor.getCol2()).setPictureData(pictureData.getData()).setExt(pictureData.getMimeType()));
                        }
                    }
                }
            }
        }

        return picturesInfoList;
    }

    private static boolean isInternalOrIntersect(Integer rangeMinRow, Integer rangeMaxRow, Integer rangeMinCol, Integer rangeMaxCol, int pictureMinRow, int pictureMaxRow, int pictureMinCol, int pictureMaxCol, Boolean onlyInternal) {
        int _rangeMinRow = rangeMinRow == null ? pictureMinRow : rangeMinRow;
        int _rangeMaxRow = rangeMaxRow == null ? pictureMaxRow : rangeMaxRow;
        int _rangeMinCol = rangeMinCol == null ? pictureMinCol : rangeMinCol;
        int _rangeMaxCol = rangeMaxCol == null ? pictureMaxCol : rangeMaxCol;

        if (onlyInternal) {
            return (_rangeMinRow <= pictureMinRow && _rangeMaxRow >= pictureMaxRow && _rangeMinCol <= pictureMinCol && _rangeMaxCol >= pictureMaxCol);
        } else {
            return ((Math.abs(_rangeMaxRow - _rangeMinRow) + Math.abs(pictureMaxRow - pictureMinRow) >= Math.abs(_rangeMaxRow + _rangeMinRow - pictureMaxRow - pictureMinRow)) && (Math.abs(_rangeMaxCol - _rangeMinCol) + Math.abs(pictureMaxCol - pictureMinCol) >= Math.abs(_rangeMaxCol + _rangeMinCol - pictureMaxCol - pictureMinCol)));
        }
    }

    /**
     * 图片基本信息
     */
    @Data
    @Accessors(chain = true)
    private class PicturesInfo {

        private int minRow;
        private int maxRow;
        private int minCol;
        private int maxCol;
        private String ext;
        private byte[] pictureData;

        public PicturesInfo() {
        }

        public byte[] getPictureData() {
            return pictureData;
        }

        public PicturesInfo setPictureData(byte[] pictureData) {
            this.pictureData = pictureData;
            return this;
        }

        public int getMinRow() {
            return minRow;
        }

        public PicturesInfo setMinRow(int minRow) {
            this.minRow = minRow;
            return this;
        }

        public int getMaxRow() {
            return maxRow;
        }

        public PicturesInfo setMaxRow(int maxRow) {
            this.maxRow = maxRow;
            return this;
        }

        public int getMinCol() {
            return minCol;
        }

        public PicturesInfo setMinCol(int minCol) {
            this.minCol = minCol;
            return this;
        }

        public int getMaxCol() {
            return maxCol;
        }

        public PicturesInfo setMaxCol(int maxCol) {
            this.maxCol = maxCol;
            return this;
        }

        public String getExt() {
            return ext;
        }

        public PicturesInfo setExt(String ext) {
            this.ext = ext;
            return this;
        }
    }

}
