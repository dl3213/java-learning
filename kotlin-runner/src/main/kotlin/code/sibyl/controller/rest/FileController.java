package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.model.FileInfo;
import code.sibyl.service.FileService;
import code.sibyl.service.sql.PostgresqlService;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/rest/v1/file")
@Slf4j
public class FileController {

    @Autowired
    FileService storageService;


    @PostMapping("/upload")
    @ResponseBody
    public Mono<ResponseEntity<Response>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return storageService.save(filePartMono)
                .map((baseFile) -> ResponseEntity.ok().body(Response.success(baseFile)));
    }

    @PostMapping(value = "/page")
    @ResponseBody
    public Mono<Response> page(@RequestBody JSONObject jsonObject) {
        String isDeleted = jsonObject.getString("isDeleted");
        Criteria criteria = Criteria.where("IS_DELETED").is(isDeleted); //.and("type").like("image%");

        String type = jsonObject.getString("type");
        if (StringUtils.isNotBlank(type)) {
            criteria = criteria.and("type").like(type + "%");
        }
        String keyword = jsonObject.getString("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            criteria = criteria.and(
                    Criteria.empty().and("real_name").like("%" + keyword + "%")
                            .or("sha256").like("%" + keyword + "%")
                            .or("type").like("%" + keyword + "%")
                            .or("file_name").like("%" + keyword + "%")
            );
        }
        String hash = jsonObject.getString("hash");
        Mono<List<Object>> sha256Query = Mono.just(new ArrayList<>());
        if ("1".equals(hash)) {
            sha256Query = PostgresqlService.getBean().template().getDatabaseClient()
                    .sql(STR."""
                            select * from (
                                select sha256, count(1) as count from T_BASE_FILE
                                where is_deleted = '\{isDeleted}' and sha256 is not null
                                group by sha256
                            )t where count >=2
                            """)
                    .fetch()
                    .all()
                    .map(e -> e.get("sha256"))
                    .switchIfEmpty(Mono.just("hash"))
                    .collectList();
        }

        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");
        return Mono.zip(Mono.just(criteria), sha256Query)
                .flatMap(tuple -> {
                    //System.err.println(tuple.getT2());
                    Criteria t1 = tuple.getT1();
                    Sort sort = Sort.sort(BaseFile.class).by(BaseFile::getCreateTime).ascending();
                    if (CollectionUtils.isNotEmpty(tuple.getT2())) {
                        t1 = t1.and("sha256").in(tuple.getT2());
                        sort = (Sort.sort(BaseFile.class).by(BaseFile::getSha256).ascending()).and(sort);
                    }
                    Query query = Query.query(t1)
                            .sort(sort)
                            .with(PageRequest.of(pageNumber - 1, pageSize)); // 0开始

                    return Mono.zip(PostgresqlService.getBean().template().count(query, BaseFile.class), PostgresqlService.getBean().template().select(query, BaseFile.class).collectList());
                })
                .map(t -> Response.successPage(t.getT1(), t.getT2(), pageNumber, pageSize));

    }

    @PostMapping(value = "/recycle/page")
    @ResponseBody
    public Mono<Response> recycle_page(@RequestBody JSONObject jsonObject) {
        Criteria criteria = Criteria.where("IS_DELETED").is("1");

        String type = jsonObject.getString("type");
        if (StringUtils.isNotBlank(type)) {
            criteria = criteria.and("type").like(type + "%");
        }
        String keyword = jsonObject.getString("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            criteria = criteria.and(
                    Criteria.empty().and("real_name").like("%" + keyword + "%")
                            .or("sha256").like("%" + keyword + "%")
                            .or("type").like("%" + keyword + "%")
                            .or("file_name").like("%" + keyword + "%")
            );
        }
        String hash = jsonObject.getString("hash");
        Mono<List<Object>> sha256Query = Mono.just(new ArrayList<>());
        if ("1".equals(hash)) {
            sha256Query = PostgresqlService.getBean().template().getDatabaseClient()
                    .sql("""
                            select sha256, count(1) as count from T_BASE_FILE
                            where is_deleted = '0'
                            group by sha256
                            having count >=2
                            """)
                    .fetch()
                    .all()
                    .map(e -> e.get("sha256"))
                    .switchIfEmpty(Mono.just("hash"))
                    .collectList();
        }

        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");

        return Mono.zip(Mono.just(criteria), sha256Query)
                .flatMap(tuple -> {
                    //System.err.println(tuple.getT2());
                    Criteria t1 = tuple.getT1();
                    Sort sort = Sort.sort(BaseFile.class).by(BaseFile::getCreateTime).ascending();
                    if (CollectionUtils.isNotEmpty(tuple.getT2())) {
                        t1 = t1.and("sha256").in(tuple.getT2());
                        sort = (Sort.sort(BaseFile.class).by(BaseFile::getSha256).ascending()).and(sort);
                    }
                    Query query = Query.query(t1)
                            .sort(sort)
                            .with(PageRequest.of(pageNumber - 1, pageSize)); // 0开始

                    return Mono.zip(PostgresqlService.getBean().template().count(query, BaseFile.class), PostgresqlService.getBean().template().select(query, BaseFile.class).collectList());
                })
                .map(t -> Response.successPage(t.getT1(), t.getT2(), pageNumber, pageSize));

    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public Mono<Response> delete(@PathVariable String id) {
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    e.setDeleted("1");
                    e.setUpdateTime(LocalDateTime.now());
                    e.setUpdateId(r.defaultUserId());
                    return PostgresqlService.getBean().template().update(e);
                })
                .map(e -> Response.success(e));
    }
    @PostMapping(value = "/restore/{id}")
    @ResponseBody
    public Mono<Response> restore(@PathVariable String id) {
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    e.setDeleted("0");
                    e.setUpdateTime(LocalDateTime.now());
                    e.setUpdateId(r.defaultUserId());
                    return PostgresqlService.getBean().template().update(e);
                })
                .map(e -> Response.success(e));
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Flux<FileInfo>> getListFiles() {
        Stream<FileInfo> fileInfoStream = storageService.loadAll()
                .map(path -> {
                    String filename = path.getFileName().toString();
                    String url = UriComponentsBuilder.newInstance().path("/files/{filename}").buildAndExpand(filename).toUriString();
                    File file = new File(r.fileBaseDir() + File.separator + path.getFileName());
                    return new FileInfo(filename, url, 0l, file.isDirectory(), file.isFile());
                });

        Flux<FileInfo> fileInfosFlux = Flux.fromStream(fileInfoStream);

        return ResponseEntity.status(HttpStatus.OK).body(fileInfosFlux);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Flux<DataBuffer>> getFile(@PathVariable String filename) {
        Flux<DataBuffer> file = storageService.load(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
    }

    @DeleteMapping("/files/{filename:.+}")
    @ResponseBody
    public Mono<ResponseEntity<Response>> deleteFile(@PathVariable String filename) {
        String message = "";

        try {
            boolean existed = storageService.delete(filename);
            if (existed) {
                message = "Delete the file successfully: " + filename;
                return Mono.just(ResponseEntity.ok().body(Response.success(message)));
            }

            message = "The file does not exist!";
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.success(message)));
        } catch (Exception e) {
            message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.success(message)));
        }
    }


}
