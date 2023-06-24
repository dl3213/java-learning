package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.util.docx.DocxUtil;
import org.springframework.util.ClassUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Classname TestController
 * @Description TestController
 * @Date 2022/5/11 12:00
 * @Author by Qin Yazhi
 */
@RequestMapping("/api/v1")
@Slf4j
@RestController
public class DocxTempleteController {

    @GetMapping("/byTemplate/create")
    public void create(HttpServletResponse response, @Validated Object exportVO) throws Exception {

        HashMap<String, Object> dataMap = getDataMap();

        String templatePath = ClassUtils.getDefaultClassLoader().getResource("template-with-bookmark.docx").getPath();//  会多出一个斜杠 : /D:/dl3213/workspace/4me/sibyl-microservice/sibyl-boot/target/classes/template-with-bookmark.docx
        templatePath = templatePath.substring(1);
        String outPutFilePath = "D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\main\\resources\\output.docx";
        DocxUtil.newDocxByUsingDataReplaceBookmark(outPutFilePath, templatePath, dataMap);
    }

    @GetMapping("/byTemplate/response")
    public void response(HttpServletResponse response, @Validated Object exportVO) throws Exception {
        HashMap<String, Object> dataMap = getDataMap();

        String templatePath = ClassUtils.getDefaultClassLoader().getResource("template-with-bookmark.docx").getPath();
        templatePath = templatePath.substring(1);

        DocxUtil.newDocxByUsingDataReplaceBookmark(response, "测试文件名" + System.currentTimeMillis() + ".docx", templatePath, dataMap);
    }

    private HashMap<String, Object> getDataMap() {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("PO_ntjc_page_code", "PO_ntjc_page_code");
        dataMap.put("PO_ntjc_code", "PO_ntjc_code");
        dataMap.put("PO_ntjc_06_安全关键件清单_table", "<p style='font-size: 1132px;'>安全关键 \n 件清单测试文本内容</p>");
        dataMap.put("book_mark_e_mail", "1176748545@qq.com");
        String picUrl = "D:\\dl3213\\workspace\\4me\\sibyl-microservice\\sibyl-boot\\src\\main\\resources\\QQ图片20221201163828.png";
        dataMap.put("PO_ntjc_05_描述说明_样品_PicTable", DocxUtil.createPicture(picUrl, 200,150));
        dataMap.put("PIC_test", DocxUtil.createPicture(picUrl, 200,150));
        dataMap.put("PO_ntjc_04_描述说明_样品铭牌_PicTable", DocxUtil.createPicture(picUrl, 200,150));
        dataMap.put("test_table_bookmark_text", 1);
        //网格图片 todo 未处理
        dataMap.put("test_table_bookmark_pic", DocxUtil.createPicture(picUrl, 20,15));
//        dataMap.put("test_table_operator", DocxUtil.TableOperator.CELL_DEL);
//        dataMap.put("test_table_operator", DocxUtil.TableOperator.ROW_DEL);
        dataMap.put("test_table_operator", DocxUtil.TableOperator.TABLE_DEL);

        return dataMap;
    }

}
