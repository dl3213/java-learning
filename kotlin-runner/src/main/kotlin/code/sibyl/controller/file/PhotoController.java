package code.sibyl.controller.file;

import cn.hutool.core.lang.Tuple;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.service.FileService;
import code.sibyl.service.sql.H2Service;
import code.sibyl.service.UpdateService;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    FileService fileService;
    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @GetMapping({"/list-view"})
    public Mono<String> model(final Model model) {
        String s = "/photo/list-view";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        model.addAttribute("staticFileBasePath", r.staticFileBasePath);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @PostMapping(value = "/pixiv/page1")
    @ResponseBody
    public Mono<Response> pixiv_page1(@RequestBody JSONObject jsonObject) {
        Criteria criteria = Criteria.where("IS_DELETED").is("0").and("type").like("image%");
        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");

        Query query = Query.query(criteria)
                .sort(Sort.sort(code.sibyl.domain.base.BaseFile.class).by(BaseFile::getFileName).ascending())
                .with(PageRequest.of(pageNumber, pageSize)) // 1开始
                ;
        return Mono.zip(r2dbcEntityTemplate.count(query, BaseFile.class), r2dbcEntityTemplate.select(query, BaseFile.class).collectList())
                .map(t -> Response.successPage(t.getT1(), t.getT2(), pageNumber, pageSize));
    }

    @PostMapping(value = "/pixiv/page")
    @ResponseBody
    public Mono<Response> pixiv_page2(@RequestBody JSONObject jsonObject) {

        final Integer pageNumber = jsonObject.getInteger("pageNumber");
        final Integer pageSize = jsonObject.getInteger("pageSize");
        String keyword = jsonObject.getString("keyword");
        keyword = StringUtils.isNotBlank(keyword) ? keyword : "";

        Criteria criteria = Criteria.where("IS_DELETED").is("0").and("type").like("image%");
        Query query = Query.query(criteria)
                .sort(Sort.sort(code.sibyl.domain.base.BaseFile.class).by(BaseFile::getFileName))
                .with(PageRequest.of(pageNumber, pageSize)) // 1开始
                ;


        return r2dbcEntityTemplate.getDatabaseClient()
                .sql("""
                        select * from T_BASE_FILE 
                        where IS_DELETED = '0' 
                        and type like 'image%' 
                        -- and SHA256 like '%' || :keyword || '%'
                        order by FILE_NAME asc
                        """)
                //.bind("keyword", keyword)
                .mapProperties(BaseFile.class)
                .all()
                .skip((pageNumber - 1) * pageSize)
                .take(pageSize)
                .collectList()
                .map(list -> Response.successPage(0, list, pageNumber, pageSize));

//        return Mono.zip(r2dbcEntityTemplate.count(query, BaseFile.class), r2dbcEntityTemplate.select(query, BaseFile.class).collectList())
//                .map(t -> Response.successPage(t.getT1(), t.getT2(), pageNumber, pageSize));
    }

}
