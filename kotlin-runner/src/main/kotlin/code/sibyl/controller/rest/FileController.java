package code.sibyl.controller.rest;

import code.sibyl.TBizUserHeart;
import code.sibyl.aop.ActionLog;
import code.sibyl.aop.ActionType;
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
import org.springframework.beans.BeanUtils;
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
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        //r.sleep(1000);
        long currentUserId = r.defaultUserId();
        final String entityType = "t_base_file";

        String isDeleted = jsonObject.getString("isDeleted");
        if (StringUtils.isBlank(isDeleted)) {
            isDeleted = "0";
        }
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
                    .collectList()
                    .switchIfEmpty(Mono.just(Arrays.asList("hash")))
            ;
        }
        String heart = jsonObject.getString("heart");
        Mono<List<Object>> heartQuery = Mono.just(new ArrayList<>());
        if ("1".equals(heart)) {
            heartQuery = PostgresqlService.getBean().template().getDatabaseClient()
                    .sql("""
                            select distinct entity_id from t_biz_user_heart 
                            where is_deleted = '0' 
                            and entity_type =:entityType 
                            and user_id =:userId   
                            """)
                    .bind("entityType", entityType)
                    .bind("userId", currentUserId)
                    .fetch()
                    .all()
                    .map(e -> e.get("entity_id"))
                    .switchIfEmpty(Mono.just(0L))
                    .collectList()
                    .switchIfEmpty(Mono.just(Arrays.asList(0L)));
        }
        Integer pageNumber = jsonObject.getInteger("pageNumber");
        pageNumber = Objects.isNull(pageNumber) ? 1 : pageNumber;
        Integer pageSize = jsonObject.getInteger("pageSize");
        pageSize = Objects.isNull(pageSize) ? 30 : pageSize;

        Integer finalPageNumber = pageNumber;
        Integer finalPageSize = pageSize;
        return Mono.zip(Mono.just(criteria), sha256Query, heartQuery)
                .flatMap(tuple -> {
                    Criteria t1 = tuple.getT1();
                    String orderField = jsonObject.getString("orderField");
                    final String methodName = STR."get\{orderField.substring(0, 1).toUpperCase()}\{orderField.substring(1)}";
                    Function<BaseFile, ?> function = (Function<BaseFile, Object>) baseFile ->
                    {
                        try {
                            return Arrays.stream(baseFile.getClass().getDeclaredMethods())
                                    .filter(e -> (methodName).equals(e.getName()))
                                    .findFirst()
                                    .orElse(null)
                                    .invoke(baseFile);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    };
                    Sort.TypedSort<?> orders = Sort.sort(BaseFile.class).by(function);
                    Sort sort = "asc".equals(jsonObject.getString("orderDirection")) ? orders.ascending() : orders.descending();
                    if (CollectionUtils.isNotEmpty(tuple.getT2())) {
                        System.err.println("sha256 -> " + tuple.getT2());
                        t1 = t1.and("sha256").in(tuple.getT2());
                        sort = (Sort.sort(BaseFile.class).by(BaseFile::getSha256).ascending()).and(sort);
                    }
                    if (CollectionUtils.isNotEmpty(tuple.getT3())) {
                        System.err.println("heart -> " + tuple.getT3());
                        t1 = t1.and("id").in(tuple.getT3());
                        sort = (Sort.sort(BaseFile.class).by(BaseFile::getCreateTime).ascending()).and(sort);
                    }

                    Query query = Query.query(t1)
                            .sort(sort)
                            .with(PageRequest.of(finalPageNumber - 1, finalPageSize)); // 0开始

                    return Mono.zip(PostgresqlService.getBean().template().count(query, BaseFile.class), PostgresqlService.getBean().template().select(query, BaseFile.class).collectList());
                })
                .flatMap(tuple -> Mono.zip(
                        Mono.just(tuple.getT1()),
                        Mono.just(tuple.getT2()),
                        CollectionUtils.isNotEmpty(tuple.getT2()) ?
                                PostgresqlService.getBean().template()
                                        .getDatabaseClient()
                                        .sql("""
                                                select * from t_biz_user_heart 
                                                where is_deleted = '0'
                                                and entity_type =:entityType
                                                and entity_id in (:entityIdList)
                                                and user_id =:userId  
                                                """)
                                        .bind("entityType", entityType)
                                        .bind("entityIdList", tuple.getT2().stream().map(e -> e.getId()).collect(Collectors.toList()))
                                        .bind("userId", currentUserId)
                                        .mapProperties(TBizUserHeart.class)
                                        .all()
                                        .collectList() : Mono.just(new ArrayList<TBizUserHeart>())
                ))
                .map(t -> {

                    List<BaseFile> collect = t.getT2().stream()
                            .peek(item -> {
                                long heartByCurrentUserCount = t.getT3().stream().filter(h -> item.getId().equals(h.getEntityId())).count();
                                item.setHeartByCurrentUserCount(Long.valueOf(heartByCurrentUserCount).intValue());
                            })
                            .collect(Collectors.toList());
                    Response response = Response.successPage(t.getT1(), collect, finalPageNumber, finalPageSize);
                    response.put("prevUrl", r.staticFileBasePath.replace("**", ""));
                    return response;
                });

    }

    // todo
    @PostMapping(value = "/sql/page")
    @ResponseBody
    public Mono<Response> sql_page(@RequestBody JSONObject jsonObject) {
        return PostgresqlService.getBean().fileQuery(jsonObject, BaseFile.class)
                .map(tuple -> {
                    //System.err.println(tuple);
                    Response response = Response.successPage(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4());
                    response.put("prevUrl", r.staticFileBasePath.replace("**", ""));
                    return response;
                });
    }

    @GetMapping(value = "/detail/{id}")
    @ResponseBody
    public Mono<Response> detail(@PathVariable String id) {
        r.sleep(500);
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .map(e -> Response.success(e));
    }

    @PostMapping(value = "/update")
    @ResponseBody
    @ActionLog(topic = "file update", type = ActionType.UPDATE)
    public Mono<Response> update(@RequestBody BaseFile baseFile) {
        r.sleep(500);
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(baseFile.getId())), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{baseFile.getId()}不存在")))
                .flatMap(e -> {
                    BeanUtils.copyProperties(baseFile, e);
                    e.setUpdateTime(LocalDateTime.now());
                    return PostgresqlService.getBean().template().update(e);
//                    return Mono.just(e);
                })
                .map(e -> Response.success(e));
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public Mono<Response> delete(@PathVariable String id) {
        r.sleep(1000);
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
        r.sleep(1000);
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    e.setDeleted("0");
                    e.setUpdateTime(LocalDateTime.now());
                    e.setUpdateId(r.defaultUserId());
                    return PostgresqlService.getBean().template().update(e);
                })
                .map(e -> Response.success(e));
    }

    @PostMapping(value = "/click/{id}")
    @ResponseBody
    public Mono<Response> click(@PathVariable Long id) {

        return PostgresqlService.getBean().template()
                .getDatabaseClient()
                .sql("update T_BASE_FILE set click_count = click_count + 1 where id = :id")
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .flatMap(update -> PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在"))))
                .map(e -> Response.success(e));
    }

    @PostMapping(value = "/heart/{id}")
    @ResponseBody
    public Mono<Response> heart(@PathVariable Long id) {
        long currentUserId = r.defaultUserId();
        final String entityType = "t_base_file";

        return PostgresqlService.getBean().template()
                .getDatabaseClient()
                .sql("""
                        select * from t_biz_user_heart 
                        where is_deleted = '0'
                        and entity_type =:entityType
                        and entity_id =:entityId 
                        and user_id =:userId  
                        """)
                .bind("entityType", entityType)
                .bind("entityId", id)
                .bind("userId", currentUserId)
                .mapProperties(TBizUserHeart.class)
                .first()
                .switchIfEmpty(Mono.just(new TBizUserHeart()))
                .flatMap(heart -> {
                    Long heartId = heart.getId();
                    Mono<TBizUserHeart> op;
                    if (Objects.isNull(heartId)) {
                        heart.setId(r.id());
                        heart.setDeleted("0");
                        heart.setCreateTime(LocalDateTime.now());
                        heart.setEntityType(entityType);
                        heart.setUserId(currentUserId);
                        heart.setEntityId(id);
                        op = PostgresqlService.getBean().template().insert(heart);
                    } else {
                        heart.setDeleted("1");
                        heart.setUpdateTime(LocalDateTime.now());
                        op = PostgresqlService.getBean().template().update(heart);
                    }
                    return Mono.zip(
                            op,
                            Mono.just(String.valueOf(id))
                    );
                })
                .flatMap(tuple ->
                        Mono.zip(
                                Mono.just(tuple.getT1()),
                                PostgresqlService.getBean().template()
                                        .getDatabaseClient()
                                        .sql("""
                                             select count(1) as count from t_biz_user_heart
                                                where is_deleted = '0'
                                                  and entity_type = 't_base_file'
                                                  and entity_id = :id 
                                            """)
                                        .bind("id", id)
                                        .fetch()
                                        .first()
                                        .map(e -> e.get("count"))
                                        .switchIfEmpty(Mono.just(0))
                        ))
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
