package code.sibyl.controller.file;

import cn.hutool.core.lang.Tuple;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.service.FileService;
import code.sibyl.service.QueryService;
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
import java.util.Objects;

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

    @DeleteMapping(value = "/pixiv/delete/{id}")
    @ResponseBody
    public Mono<Response> pixiv_delete(@PathVariable String id) {
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    System.err.println(e.getAbsolutePath());
                    e.setDeleted("1");
                    e.setUpdateTime(LocalDateTime.now());
                    e.setUpdateId(r.defaultUserId());
//                    File file = new File(e.getAbsolutePath());
//                    if (Objects.nonNull(file) && file.exists()) {
//                        file.delete();
//                    }
                    return r2dbcEntityTemplate.update(e);
                }).map(e -> Response.success(e));
    }

    @PostMapping("/pixiv/upload")
    @ResponseBody
    public Mono<Response> th_finance_collection_book_upload(@RequestPart("file") Mono<FilePart> filePartMono) {

        return filePartMono
                .flatMap(e -> Mono.zip(Mono.just(e.filename()), DataBufferUtils.join(e.content())))
                .map(t -> {
                    DataBuffer dataBuffer = t.getT2();
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    System.err.println(bytes.length);
                    return new Tuple(t.getT1(), bytes);
                })
                .flatMap(t -> {
                    String fileName = t.get(0);
                    byte[] bytes = t.get(1);
                    String detect = r.getBean(Tika.class).detect(bytes);
                    String hash = r.hashBytes(bytes);
                    return Mono.zip(
                            Mono.just(fileName),
                            Mono.just(bytes),
                            Mono.just(detect),
                            Mono.just(new Object()) , //ReactiveSecurityContextHolder.getContext(),
                            QueryService.getBean().fileList(fileName, detect).collectList(),
                            QueryService.getBean().fileListByHash(hash).collectList()
                    );
                })
                .flatMap(t -> {
                    String fileName = t.getT1();
                    System.err.println(fileName);
                    byte[] bytes = t.getT2();
                    System.err.println(bytes.length);
                    String fileType = t.getT3();
                    System.err.println(fileType);
                    Object user = (Object) t.getT4() ;//.getAuthentication().getPrincipal();
                    System.err.println(user);
                    List<BaseFile> baseFileList = t.getT5();
                    System.err.println(baseFileList);
                    List<BaseFile> hashList = t.getT6();

                    if (!fileType.contains("image")) {
                        return Mono.error(new RuntimeException("上传失败：非图像文件"));
                    }
                    if (CollectionUtils.isNotEmpty(baseFileList)) {
                        return Mono.error(new RuntimeException("上传失败：同名图像文件已存在"));
                    }
                    if (CollectionUtils.isNotEmpty(hashList)) {
                        return Mono.error(new RuntimeException("上传失败：相同hash文件已存在"));
                    }

                    String dir = r.fileBaseDir() + r.yyyy_MM_dd() + File.separator;
                    String absolutePath = dir + fileName;
                    BaseFile file = UpdateService.getBean().file(fileName, absolutePath, null, bytes, true);

                    return r2dbcEntityTemplate.insert(file);
                })
                .map(e -> Response.success(e))
                ;
    }
}
