package me.sibyl.util.docx;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.w3c.dom.Node;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @Classname DocxUtil
 * @Description DocxUtil
 * @Date 2023/2/9 15:51
 * @Author by Qin Yazhi
 */
public final class DocxUtil {

    private static final String BOOKMARK_START_TAG = "w:bookmarkStart";
    private static final String BOOKMARK_END_TAG = "w:bookmarkEnd";

    /**
     * docx文件hashMap替换同名key书签内容,将生成的新文件的文件流写入response,
     * 模板文件一些注意项：
     * 1.书签最好不要嵌套，
     * 2.且书签最好是完整正常书签
     * (完整正常书签在xml格式上体现为:以w:bookmarkStart开始，以w:bookmarkEnd结束，中间存在标签列如w:r,w:t之类的)
     * (不完整异常书签在xml格式上体现为:以w:bookmarkStart开始，以w:bookmarkEnd结束，中间缺少部分标签列如w:r,w:t之类的重要标签的)
     * 出现不完整异常书签会抛异常，此时手动删掉再新建书签即可
     */
    public static void newDocxByUsingDataReplaceBookmark(HttpServletResponse response, String fileName, String templateFilePath, HashMap<String, Object> hashMap) throws Exception {

        XWPFDocument wordDocx = xwpfDocxBuilder(templateFilePath, hashMap);
        //写响应内容
        response.setContentType("application/force-download");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        ServletOutputStream os = response.getOutputStream();
        wordDocx.write(os);
        os.flush();
        os.close();
    }

    /**
     * docx文件hashMap替换同名key书签内容,生产一个新文件
     */
    public static void newDocxByUsingDataReplaceBookmark(String outPutFilePath, String templateFilePath, HashMap<String, Object> hashMap) throws Exception {

        XWPFDocument wordDocx = xwpfDocxBuilder(templateFilePath, hashMap);

        //嵌套表格处理
        tableHandler(hashMap, wordDocx);

        OutputStream os = new FileOutputStream(outPutFilePath);
        wordDocx.write(os);
        os.close();
    }

    private static XWPFDocument xwpfDocxBuilder(String templateFilePath, HashMap<String, Object> hashMap) throws IOException {

        InputStream is = Files.newInputStream(Paths.get(templateFilePath));

        XWPFDocument docx = new XWPFDocument(is);

        //段落
        for (XWPFParagraph paragraph : docx.getParagraphs()) {
            replaceContentByBookmark(hashMap, paragraph);
        }

        //页眉页脚
        for (XWPFHeader header : docx.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                replaceContentByBookmark(hashMap, paragraph);
            }
        }
        for (XWPFFooter footer : docx.getFooterList()) {
            for (XWPFParagraph paragraph : footer.getParagraphs()) {
                replaceContentByBookmark(hashMap, paragraph);
            }
        }

        //嵌套表格处理
        tableHandler(hashMap, docx);

        try {
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return docx;
    }

    private static void tableHandler(HashMap<String, Object> dataMap, XWPFDocument wordDoc) {

        if (Objects.isNull(wordDoc)) return;

        List<XWPFTable> tables = wordDoc.getTables();

        // todo 递归???
        while (CollectionUtils.isNotEmpty(tables)) {

            List<XWPFTable> tablesForEach = tables;
            List<XWPFTable> tablesCopy = new ArrayList<>();
            //表格处理
            for (XWPFTable table : tablesForEach) {
                boolean tableDel = tableHandler(dataMap, tablesCopy, table, wordDoc);
                if(tableDel) return;
            }

            tables = tablesCopy;//后处理子表格
        }


    }

    /**
     * 表格处理
     */
    private static boolean tableHandler(HashMap<String, Object> dataMap, List<XWPFTable> tablesCopy, XWPFTable table, XWPFDocument wordDoc) {
        for (int rowIndex = 0; rowIndex < table.getRows().size(); rowIndex++) {
            boolean tableDel = rowHandler(dataMap, tablesCopy, table, rowIndex, wordDoc);
            if (tableDel) return true;//存在删除操作，提前返回
        }
        return false;
    }

    /**
     * 表格行处理
     */
    private static boolean rowHandler(HashMap<String, Object> dataMap, List<XWPFTable> tablesCopy, XWPFTable table, int rowIndex, XWPFDocument wordDocx) {
        XWPFTableRow row = table.getRows().get(rowIndex);
        for (int celIndex = 0; celIndex < row.getTableICells().size(); celIndex++) {
            boolean rowDel = cellHandler(dataMap, tablesCopy, table, rowIndex, row, celIndex, wordDocx);
            if (rowDel) return true;//存在删除操作，提前返回
        }
        return false;
    }

    /**
     * 表格单元格处理
     */
    private static boolean cellHandler(HashMap<String, Object> dataMap, List<XWPFTable> tablesCopy, XWPFTable table, int rowIndex, XWPFTableRow row, int celIndex, XWPFDocument wordDocx) {
        XWPFTableCell c = (XWPFTableCell) row.getTableICells().get(celIndex);
        //子表格:先集中
        List<XWPFTable> sonTables = c.getTables();
        tablesCopy.addAll(sonTables);//
        //表格自己的行段落处理
        for (XWPFParagraph paragraph : c.getParagraphs()) {
            boolean rowDel = replaceContentByBookmark(dataMap, paragraph, celIndex, c, rowIndex, row, table, wordDocx);
            if (rowDel) return true;//存在删除操作，提前返回
        }
        return false;
    }

    private static boolean replaceContentByBookmark(HashMap<String, Object> dataMap, XWPFParagraph xwpfParagraph) {
        return replaceContentByBookmark(dataMap, xwpfParagraph, null, null, null, null, null, null);
    }

    private static boolean replaceContentByBookmark(HashMap<String, Object> dataMap, XWPFParagraph xwpfParagraph, Integer cellIndex, XWPFTableCell cell, Integer rowIndex, XWPFTableRow row, XWPFTable table, XWPFDocument wordDocx) {
        if (Objects.isNull(xwpfParagraph)) return false;
        CTP ctp = xwpfParagraph.getCTP();
        if (Objects.isNull(ctp)) return false;
        List<CTBookmark> bookmarkStartList = ctp.getBookmarkStartList();
        if (CollectionUtils.isEmpty(bookmarkStartList)) return false;
        for (int i = 0; i < bookmarkStartList.size(); i++) {
            CTBookmark bookmark = bookmarkStartList.get(i);
            if (Objects.isNull(bookmark)) continue;
            if (!dataMap.containsKey(bookmark.getName())) continue;
            if (Objects.isNull(dataMap.get(bookmark.getName()))) continue;
            XWPFRun run = xwpfParagraph.createRun();

            //System.err.println(bookmark.getName() + " => " + dataMap.get(bookmark.getName()) + " => " + dataMap.get(bookmark.getName()).getClass().getName());

            //内容处理
            boolean bookmarkDel = runContextBuilder(run, dataMap.get(bookmark.getName()), cellIndex, cell, rowIndex, row, table, wordDocx);
            if (bookmarkDel) return true;//删除了就跳过本此处理
            //书签处理
            Node firstNode = bookmark.getDomNode();
            Node nextNode = firstNode.getNextSibling();
            while (nextNode != null) {
                String nodeName = nextNode.getNodeName();
                if (nodeName.equals(BOOKMARK_END_TAG)) break;

                // 删除中间的非结束节点，即删除原书签内容
                Node delNode = nextNode;
                nextNode = nextNode.getNextSibling();
                ctp.getDomNode().removeChild(delNode);
            }
            if (nextNode == null) {
                // 始终找不到结束标识的，就在书签前面添加
                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), firstNode);
            } else {
                // 找到结束符，将新内容添加到结束符之前，即内容写入bookmark中间
                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
            }
        }

        return false;
    }

    /**
     * 处理书签内容，涉及删除的返回true，其余false
     */
    private static boolean runContextBuilder(XWPFRun run, Object mapData, Integer cellIndex, XWPFTableCell cell, Integer rowIndex, XWPFTableRow row, XWPFTable table, XWPFDocument wordDocx) {
        //传入url，是图片就返回文件输入流

        //判断数据类型
        //System.err.println(" => " + mapData + " => " + mapData.getClass().getName());
        switch (mapData.getClass().getName()) {
            //字符串类型
            case "java.lang.String":
                textHandler(run, mapData);
                return false;
            case "me.demo.util.DocxUtil$Picture":
                pictureHandler(run, (Picture) mapData);
                return false;
            case "me.demo.util.DocxUtil$TableOperator":
                tableOperationHandler(cellIndex, cell, rowIndex, mapData, table, wordDocx);
                return true;
            default:
                textHandler(run, mapData);
                return false;
        }
    }

    private static void tableOperationHandler(Integer cellIndex, XWPFTableCell cell, Integer rowIndex, Object mapData, XWPFTable table, XWPFDocument wordDocx) {
        TableOperator operator = (TableOperator) mapData;
        switch (operator) {
            case CELL_DEL:
                cell.getTableRow().getCtRow().removeTc(cellIndex);
                break;
            case ROW_DEL:
                cell.getTableRow().getTable().removeRow(rowIndex);
                break;
            case TABLE_DEL:
                cell.getTableRow().getTable().getCTTbl().getDomNode().getParentNode().removeChild(cell.getTableRow().getTable().getCTTbl().getDomNode());
                break;
            default:
                break;
        }
    }

    private static void pictureHandler(XWPFRun run, Picture mapData) {
        Picture pic = mapData;
        File picFile = getPicFile(pic.getPath());
        //判断存在文件流就添加图片
        if (Objects.nonNull(picFile)) {
            try {
                FileInputStream inputStream = new FileInputStream(picFile);
                //BufferedImage bi = ImageIO.read(picFile);
                //inputStream.reset();
                run.addPicture(inputStream, getPictureType(picFile.getName().split("\\.")[1]), pic.getPath(), Units.toEMU(pic.getWidth()), Units.toEMU(pic.getHeight())); // 这个是按着图片自身高宽
                //run.addPicture(inputStream, getPictureType(picFile.getName().split("\\.")[1]), text, Units.toEMU(200), Units.toEMU(100)); // 200x200 pixels
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                run.setText(e.getMessage());
            }
        }
    }

    private static void textHandler(XWPFRun run, Object mapData) {
        String text = String.valueOf(mapData);
        run.setText(text);
    }

    private static int getPictureType(String picType) {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    private static File getPicFile(String url) {
        try {
            if (!url.matches("([A-Z|a-z]:\\\\[^*|\"<>?\\n]*)|(\\\\\\\\.*?\\\\.*)")) return null;
            File file = new File(url);
            if (!file.exists()) return null;
            if (file.isDirectory()) return null;

            return new File(url);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum TableOperator {//暂不支持列操作
        CELL_DEL,//删除单元格
        ROW_DEL,//删除行
        TABLE_DEL,//删除表格
        ;
    }

    public static Picture createPicture(String path, int width, int height) {
        return new Picture(path, width, height);
    }

    @Data
    @AllArgsConstructor
    static class Picture {
        private String path;
        private int width;
        private int height;
    }
}
