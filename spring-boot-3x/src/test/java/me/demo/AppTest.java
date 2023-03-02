//package me.demo;
//
//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.config.Configure;
//import com.deepoove.poi.config.ConfigureBuilder;
//import com.deepoove.poi.data.Numberings;
//import com.deepoove.poi.data.TextRenderData;
//import com.deepoove.poi.plugin.bookmark.BookmarkRenderPolicy;
//import com.deepoove.poi.policy.AbstractRenderPolicy;
//import com.deepoove.poi.render.RenderContext;
//import com.deepoove.poi.render.WhereDelegate;
//import com.deepoove.poi.xwpf.XWPFParagraphWrapper;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.poi.util.IOUtils;
//import org.apache.poi.util.Units;
//import org.apache.poi.xwpf.usermodel.*;
//import org.junit.jupiter.api.Test;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.ClassUtils;
//import org.w3c.dom.Node;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//
///**
// * @Classname AppTest
// * @Description AppTest
// * @Date 2023/2/2 10:18
// * @Author by Qin Yazhi
// */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
//@Slf4j
//public class AppTest {
//
//    public static final String BOOKMARK_START_TAG = "w:bookmarkStart";
//    public static final String BOOKMARK_END_TAG = "w:bookmarkEnd";
//
//    public static void main(String[] args) {
//        String url = "D:\\dl3213\\work%%%@@@@??????space\\4me\\sibyl-microservice\\sibyl-boot\\src\\main\\resources\\QQ图片20221201163828.png";
//        String pic = "https://bkimg.cdn.bcebos.com/pic/cdbf6c81800a19d8bc3e2629cfb5958ba61ea8d3e8c5";
//        if (url.matches("([A-Z|a-z]:\\\\[^*|\"<>?\\n]*)|(\\\\\\\\.*?\\\\.*)")) {
//            System.out.println("磁盘路径正确。。。");
//        } else {
//            System.out.println("磁盘路径不正确。。。");
//        }
//        if (pic.matches("([A-Z|a-z]:\\\\[^*|\"<>?\\n]*)|(\\\\\\\\.*?\\\\.*)")) {
//            System.out.println("网络路径正确。。。");
//        } else {
//            System.out.println("网络路径不正确。。。");
//        }
//    }
//
//    @SneakyThrows
//    @Test
//    void test() throws FileNotFoundException {
//
//        HashMap<String, Object> dataMap = new HashMap<>();
//        dataMap.put("PO_ntjc_page_code", "PO_ntjc_page_code");
//        dataMap.put("PO_ntjc_code", "PO_ntjc_code");
//        dataMap.put("PO_ntjc_06_安全关键件清单_table", "安全关键件清单测试文本内容");
//        dataMap.put("book_mark_e_mail", "1176748545@qq.com");
//        String picUrl = "D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\main\\resources\\QQ图片20221201163828.png";
//        dataMap.put("PO_ntjc_05_描述说明_样品_PicTable", picUrl);
//        dataMap.put("PIC_test", picUrl);
//        dataMap.put("PO_ntjc_04_描述说明_样品铭牌_PicTable", picUrl);
//        dataMap.put("test_table_bookmark_text", picUrl);
//        dataMap.put("test_table_bookmark_pic", picUrl);
//
//        //System.err.println(System.getProperty("user.dir")); // D:\dl3213\workspace\4me\sibyl-microservice\sibyl-boot
//        // String path = ResourceUtils.getURL("classpath:template.docx").getPath();
//
//        //模板位置
//        String templatePath = ClassUtils.getDefaultClassLoader().getResource("template-with-bookmark.docx").getPath();
////        System.err.println(templatePath);
//        templatePath = templatePath.substring(1);
//        InputStream is = Files.newInputStream(Paths.get(templatePath));
//        XWPFDocument wordDoc = new XWPFDocument(is);
//        //读取书签 todo
//
//        //段落
//        for (XWPFParagraph paragraph : wordDoc.getParagraphs()) {
//            replaceContentByBookmark(dataMap, paragraph);
//        }
//
//        //页眉页脚
//        for (XWPFHeader header : wordDoc.getHeaderList()) {
//            for (XWPFParagraph paragraph : header.getParagraphs()) {
//                replaceContentByBookmark(dataMap, paragraph);
//            }
//        }
//        for (XWPFFooter footer : wordDoc.getFooterList()) {
//            for (XWPFParagraph paragraph : footer.getParagraphs()) {
//                replaceContentByBookmark(dataMap, paragraph);
//            }
//        }
//
//        //嵌套表格处理
//        tableHandler(dataMap, wordDoc);
//
//        is.close();
//
//        String outFilePath = "D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\main\\resources\\output.docx";
//        OutputStream os = new FileOutputStream(outFilePath);
//        wordDoc.write(os);
//        is.close();
//        os.close();
//
//    }
//
//    private void tableHandler(HashMap<String, Object> dataMap, XWPFDocument wordDoc) {
//
//        if (Objects.isNull(wordDoc)) return;
//
//        List<XWPFTable> tables = wordDoc.getTables();
//
//        while (CollectionUtils.isNotEmpty(tables)) {
//
//            List<XWPFTable> tablesForEach = tables;
//            List<XWPFTable> tablesCopy = new ArrayList<>();
//
//            for (XWPFTable table : tablesForEach) {
//                for (XWPFTableRow row : table.getRows()) {
//                    for (ICell cell : row.getTableICells()) {
//                        XWPFTableCell c = (XWPFTableCell) cell;
//                        //子表格段落处理
//                        List<XWPFTable> sonTables = c.getTables();
//                        tablesCopy.addAll(sonTables);
//
////                        for (XWPFTable sonTable : sonTables) {
////                            for (XWPFTableRow sonTableRow : sonTable.getRows()) {
////                                for (ICell tableICell : sonTableRow.getTableICells()) {
////                                    XWPFTableCell tableCell = (XWPFTableCell) tableICell;
////                                    for (XWPFParagraph paragraph : tableCell.getParagraphs()) {
////                                        replaceContentByBookmark(dataMap, paragraph);
////                                    }
////                                }
////                            }
////                        }
//
//                        //表格自己的段落处理
//                        for (XWPFParagraph paragraph : c.getParagraphs()) {
//                            replaceContentByBookmark(dataMap, paragraph);
//                        }
//                    }
//                }
//            }
//
//            tables = tablesCopy;
//        }
//
//
//    }
//
//    private void replaceContentByBookmark(HashMap<String, Object> dataMap, XWPFParagraph xwpfParagraph) {
//        CTP ctp = xwpfParagraph.getCTP();
//        for (CTBookmark bookmark : ctp.getBookmarkStartList()) {
//            if (Objects.isNull(bookmark)) continue;
//            if (!dataMap.containsKey(bookmark.getName())) continue;
//            XWPFRun run = xwpfParagraph.createRun();
//            String mapData = String.valueOf(dataMap.get(bookmark.getName()));
//
//            runContextBuilder(run, mapData);
//
//            Node firstNode = bookmark.getDomNode();
//            Node nextNode = firstNode.getNextSibling();
//            while (nextNode != null) {
//                String nodeName = nextNode.getNodeName();
//                if (nodeName.equals(BOOKMARK_END_TAG)) break;
//
//                // 删除中间的非结束节点，即删除原书签内容
//                Node delNode = nextNode;
//                nextNode = nextNode.getNextSibling();
//                ctp.getDomNode().removeChild(delNode);
//            }
//            if (nextNode == null) {
//                // 始终找不到结束标识的，就在书签前面添加
//                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), firstNode);
//            } else {
//                // 找到结束符，将新内容添加到结束符之前，即内容写入bookmark中间
//                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
//            }
//        }
//    }
//
//    private void runContextBuilder(XWPFRun run, String mapData) {
//        //传入url，是图片就返回文件输入流
//        File picFile = getPicFile(mapData);
//        //判断存在文件流就添加图片
//        if (Objects.nonNull(picFile)) {
//            try {
//                FileInputStream inputStream = new FileInputStream(picFile);
//                //BufferedImage bi = ImageIO.read(picFile);
////                int width = bi.getWidth();
////                int height = bi.getHeight();
//                //inputStream.reset();
//                //run.addPicture(inputStream, getPictureType(picFile.getName().split("\\.")[1]), mapData, Units.toEMU(height), Units.toEMU(width)); // 这个是按着图片自身高宽
//                run.addPicture(inputStream, getPictureType(picFile.getName().split("\\.")[1]), mapData, Units.toEMU(300), Units.toEMU(200)); // 200x200 pixels
//                inputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {//不是就单纯的文本
//            run.setText(mapData);
//        }
//    }
//
//    private int getPictureType(String picType) {
//        int res = XWPFDocument.PICTURE_TYPE_PICT;
//        if (picType != null) {
//            if (picType.equalsIgnoreCase("png")) {
//                res = XWPFDocument.PICTURE_TYPE_PNG;
//            } else if (picType.equalsIgnoreCase("dib")) {
//                res = XWPFDocument.PICTURE_TYPE_DIB;
//            } else if (picType.equalsIgnoreCase("emf")) {
//                res = XWPFDocument.PICTURE_TYPE_EMF;
//            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
//                res = XWPFDocument.PICTURE_TYPE_JPEG;
//            } else if (picType.equalsIgnoreCase("wmf")) {
//                res = XWPFDocument.PICTURE_TYPE_WMF;
//            }
//        }
//        return res;
//    }
//
//    private File getPicFile(String url) {
//        try {
//            if (!url.matches("([A-Z|a-z]:\\\\[^*|\"<>?\\n]*)|(\\\\\\\\.*?\\\\.*)")) return null;
//            File file = new File(url);
//            if (!file.exists()) return null;
//            if (file.isDirectory()) return null;
//
//            return new File(url);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    @SneakyThrows
//    public static void test2(String[] args) {
//
//        String templatePath = "D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\test\\java\\me\\demo\\template.docx";
////        XWPFTemplate template = XWPFTemplate
////                .compile(templateFile)
////                .render(new HashMap<String, Object>(){{ put("title", "Hi, poi-tl Word模板引擎"); }});
////        template.writeAndClose(new FileOutputStream("D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\test\\java\\me\\demo\\output.docx"));
//
//
//        ConfigureBuilder builder = Configure.builder();
//        Configure config = builder
//                .bind("sea", new AbstractRenderPolicy<String>() {
//                    @Override
//                    public void doRender(RenderContext<String> context) throws Exception {
//                        // anywhere
//                        XWPFRun where = context.getWhere();
//                        // anything
//                        String thing = context.getThing();
//                        // do 文本
//                        where.setText(thing, 0);
//                    }
//                })
//                .bind("sea_img", new AbstractRenderPolicy<String>() {
//                    @Override
//                    public void doRender(RenderContext<String> context) throws Exception {
//                        // anywhere delegate
//                        WhereDelegate where = context.getWhereDelegate();
//                        // any thing
//                        String thing = context.getThing();
//                        // do 图片
//                        FileInputStream stream = null;
//                        try {
//                            stream = new FileInputStream(thing);
//                            where.addPicture(stream, XWPFDocument.PICTURE_TYPE_JPEG, 400, 450);
//                        } finally {
//                            IOUtils.closeQuietly(stream);
//                        }
//                        // clear
//                        clearPlaceholder(context, false);
//                    }
//                }).bind("sea_feature", new AbstractRenderPolicy<List<String>>() {
//                    @Override
//                    public void doRender(RenderContext<List<String>> context) throws Exception {
//                        // anywhere delegate
//                        WhereDelegate where = context.getWhereDelegate();
//                        // anything
//                        List<String> thing = context.getThing();
//                        // do 列表
//                        where.renderNumbering(Numberings.of(thing.toArray(new String[]{})).create());
//                        // clear
//                        clearPlaceholder(context, true);
//                    }
//                }).bind("sea", new BookmarkRenderPolicy() {
//                    @Override
//                    public void doRender(RenderContext<TextRenderData> context) throws Exception {
//                        Helper.renderTextRun(context.getRun(), (TextRenderData) context.getData());
//                        XWPFRun run = context.getRun();
//                        XWPFParagraphWrapper wapper = new XWPFParagraphWrapper((XWPFParagraph) run.getParent());
//                        CTBookmark bookmarkStart = wapper.insertNewBookmark(run);
//                        Object renderData = context.getData();
//                        TextRenderData data = renderData instanceof TextRenderData ? (TextRenderData) renderData : new TextRenderData(renderData.toString());
//                        String text = null == data.getText() ? "" : data.getText();
//                        bookmarkStart.setName(text);
//                    }
//                })
//                .build();
//
//        // 初始化where的数据
//        HashMap<String, Object> data = new HashMap<String, Object>();
//        data.put("sea", "Hello, world!");
//        data.put("test_bookmark", "Hello, world!");
//        data.put("sea_img", "sea.jpg");
//        data.put("sea_feature", Arrays.asList("面朝大海春暖花开", "今朝有酒今朝醉"));
//        data.put("sea_location", Arrays.asList("日落：日落山花红四海", "花海：你想要的都在这里"));
//
//        InputStream is = Files.newInputStream(Paths.get(templatePath));
//        XWPFDocument document = new XWPFDocument(is);
//        XWPFTemplate template = XWPFTemplate.compile(document, config).render(data);
//        template.writeAndClose(new FileOutputStream("D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\test\\java\\me\\demo\\output.docx"));
//    }
//}
