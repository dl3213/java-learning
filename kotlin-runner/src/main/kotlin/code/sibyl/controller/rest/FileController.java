package code.sibyl.controller.rest;

import code.sibyl.domain.TBizUserHeart;
import code.sibyl.aop.ActionLog;
import code.sibyl.aop.ActionType;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.domain.base.Tag;
import code.sibyl.model.FileInfo;
import code.sibyl.service.FfmpegService;
import code.sibyl.service.FileService;
import code.sibyl.service.FileUploadService;
import code.sibyl.service.sql.PostgresqlService;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
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
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.beans.FeatureDescriptor;

@RestController
@RequestMapping("/api/rest/v1/file")
@Slf4j
public class FileController {

    @Autowired
    FileService storageService;
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    @ResponseBody
    public Mono<ResponseEntity<Response>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono, @RequestPart("json") String json) {
        //System.err.println(json);
        return storageService.save(filePartMono, json)
                .map((baseFile) -> ResponseEntity.ok().body(Response.success(baseFile)));
    }


    /**
     * 响应式文件夹上传接口
     */
    @PostMapping(value = "/folder-reactive", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ApiResponse<UploadResult>> uploadFolderReactive(
            @RequestPart("files") Flux<FilePart> files,
            @RequestPart("paths") Flux<String> paths,
            @RequestPart(value = "folderName", required = false) String folderName,
            @RequestPart(value = "totalFiles", required = false) String totalFiles) {

        return files.collectList()
                .zipWith(paths.collectList())
                .flatMap(tuple -> {
                    List<FilePart> fileList = tuple.getT1();
                    List<String> pathList = tuple.getT2();

                    String actualFolderName = folderName != null ? folderName : "unnamed";

                    return fileUploadService.uploadFolderReactive(fileList, pathList, actualFolderName)
                            .map(result -> ApiResponse.success(
                                    "文件夹上传成功",
                                    result
                            ))
                            .onErrorResume(e -> Mono.just(
                                    ApiResponse.error("上传失败", e.getMessage())
                            ));
                });
    }

    /**
     * 传统方式文件夹上传接口（兼容MultipartFile）
     */
    @PostMapping(value = "/folder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse<UploadResult>> uploadFolder(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("paths") List<String> paths,
            @RequestPart(value = "folderName", required = false) String folderName,
            @RequestPart(value = "totalFiles", required = false) Integer totalFiles) {

        String actualFolderName = folderName != null ? folderName : "unnamed";

        return fileUploadService.uploadFolderTraditional(files, paths, actualFolderName)
                .map(result -> ApiResponse.success(
                        "文件夹上传成功",
                        result
                ))
                .onErrorResume(e -> Mono.just(
                        ApiResponse.error("上传失败", e.getMessage())
                ));
    }

    /**
     * 处理混合multipart请求
     */
    @PostMapping(value = "/folder-mixed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ApiResponse<UploadResult>> uploadFolderMixed(@RequestBody Mono<Part> partMono) {
        return partMono.flatMap(part -> {
            // 这里可以处理更复杂的multipart请求
            return Mono.just(ApiResponse.error("暂不支持", "此接口暂未实现"));
        });
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public Mono<ApiResponse<String>> test() {
        return Mono.just(ApiResponse.success("服务正常运行", "Folder Upload WebFlux Service"));
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Mono<ApiResponse<String>> healthCheck() {
        return Mono.just(ApiResponse.success("服务健康", "OK"));
    }

    @PostMapping(value = "/page")
    @ResponseBody
    public Mono<Response> page(@RequestBody JSONObject jsonObject) {
        long currentUserId = r.defaultUserId();
        final String entityType = "t_base_file";

        String isDeleted = jsonObject.getString("isDeleted");
        if (StringUtils.isBlank(isDeleted)) {
            isDeleted = "0";
        }
        Criteria criteria = Criteria.where("IS_DELETED").is(isDeleted); //.and("type").like("image%");

        String type = jsonObject.getString("type");
        if (StringUtils.isNotBlank(type)) {
            String[] typeParts = type.split(",");
            Criteria typeCriteria = Criteria.empty();
            boolean first = true;
            for (String t : typeParts) {
                t = t.trim();
                if (StringUtils.isNotBlank(t)) {
                    if (first) {
                        typeCriteria = typeCriteria.and("type").like(t + "%");
                        first = false;
                    } else {
                        typeCriteria = typeCriteria.or("type").like(t + "%");
                    }
                }
            }
            criteria = criteria.and(typeCriteria);
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
        String withTags = jsonObject.getString("withTags");
        return PostgresqlService.getBean().fileQuery(jsonObject)
                .flatMap(tuple -> {
                    List<BaseFile> files = tuple.getT2();
                    if (!"1".equalsIgnoreCase(withTags) || files.isEmpty()) {
                        return Mono.just(tuple);
                    }
                    // 收集所有 file id
                    List<Long> fileIds = files.stream()
                            .map(e -> e.getId())
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    // 查询这些文件的所有标签
                    return PostgresqlService.getBean().template()
                            .select(Query.query(
                                    Criteria.where("is_deleted").is("0")
                                            .and("entity_type").is("t_base_file")
                                            .and("entity_id").in(fileIds)
                            ), Tag.class)
                            .collectList()
                            .switchIfEmpty(Mono.just(new ArrayList<>()))
                            .map(tags -> {
                                // 按 entityId 分组
                                Map<Long, List<Tag>> tagsByFileId = new HashMap<>();
                                for (Tag tag : tags) {
                                    tagsByFileId.computeIfAbsent(Long.valueOf((tag.getEntityId())), k -> new ArrayList<>()).add(tag);
                                }
                                // 挂到每个文件上
                                for (BaseFile file : files) {
                                    if (file.getId() != null) {
                                        List<Tag> fileTags = tagsByFileId.get((file.getId()));
                                        file.setTags(fileTags != null ? fileTags : new ArrayList<>());
                                    }
                                }
                                return tuple;
                            });
                })
                .map(tuple -> {
                    Response response = Response.successPage(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4());
                    response.put("prevUrl", r.staticFileBasePath.replace("**", ""));
                    return response;
                });
    }

    @GetMapping(value = "/detail/{id}")
    @ResponseBody
    public Mono<Response> detail(@PathVariable String id) {
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .map(e -> Response.success(e));
    }

    @PostMapping(value = "/update")
    @ResponseBody
    @ActionLog(topic = "file update", type = ActionType.UPDATE)
    public Mono<Response> update(@RequestBody BaseFile baseFile) {
        return PostgresqlService.getBean().template()
                .selectOne(Query.query(Criteria.where("id").is(baseFile.getId())), BaseFile.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Entity with ID " + baseFile.getId() + " does not exist")))
                .flatMap(existingEntity -> {
                    // Copy non-null properties from `baseFile` to `existingEntity`
                    BeanUtils.copyProperties(baseFile, existingEntity, getNullPropertyNames(baseFile));
                    existingEntity.setUpdateTime(LocalDateTime.now());
                    return PostgresqlService.getBean().template().update(existingEntity);
                })
                .map(updatedEntity -> Response.success(updatedEntity));
    }

    // Helper method to get null property names
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> src.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
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

    @DeleteMapping(value = "/pixiv/delete/all/{id}")
    @ResponseBody
    public Mono<Response> pixivDeleteAll(@PathVariable String id) {
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    String[] split = e.getRealName().contains("_p") ? e.getRealName().split("_p") : e.getRealName().split("-p");
                    return PostgresqlService.getBean().template().getDatabaseClient().sql("""
                                    update t_base_file
                                    set is_deleted = '1',
                                    update_time = :updateTime,
                                    update_id = :updateId
                                    where is_deleted = '0' and code = 'pixiv' 
                                    and (
                                    real_name like ('%' || :pixivId || '_p%')
                                    or
                                    real_name like ('%' || :pixivId || '-p%')
                                    )
                                    """)
                            .bind("pixivId", split[0])
                            .bind("updateTime", LocalDateTime.now())
                            .bind("updateId", r.defaultUserId())
                            .fetch()
                            .rowsUpdated();
                })
                .map(e -> Response.success(e));
    }

    @PostMapping(value = "/pixiv/restore/all/{id}")
    @ResponseBody
    public Mono<Response> pixivRestoreAll(@PathVariable String id) {
        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class).switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMap(e -> {
                    String[] split = e.getRealName().contains("_p") ? e.getRealName().split("_p") : e.getRealName().split("-p");
                    return PostgresqlService.getBean().template().getDatabaseClient().sql("""
                                    update t_base_file
                                    set is_deleted = '0',
                                    update_time = :updateTime,
                                    update_id = :updateId
                                    where is_deleted = '1' and code = 'pixiv' 
                                    and (
                                    real_name like ('%' || :pixivId || '_p%')
                                    or
                                    real_name like ('%' || :pixivId || '-p%')
                                    )
                                    """)
                            .bind("pixivId", split[0])
                            .bind("updateTime", LocalDateTime.now())
                            .bind("updateId", r.defaultUserId())
                            .fetch()
                            .rowsUpdated();
                })
                .map(e -> Response.success(e));
    }

    @GetMapping(value = "/pixiv/heart/all/{id}")
    @ResponseBody
    public Mono<Response> pixivHeartAll(@PathVariable String id) {

        long currentUserId = r.defaultUserId();
        final String entityType = "t_base_file";

        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .switchIfEmpty(Mono.error(new RuntimeException(STR."\{id}不存在")))
                .flatMapMany(e -> {
                    String[] split = e.getRealName().split("_p");
                    Criteria criteria = Criteria.where("is_deleted").is("0")
                            .and("code").is("pixiv")
                            .and("real_name").like(STR."\{split[0]}%");
                    return PostgresqlService.getBean().template().select(Query.query(criteria), BaseFile.class);
                })
                .flatMap(baseFile -> Mono.zip(Mono.just(baseFile), PostgresqlService.getBean().template()
                        .getDatabaseClient()
                        .sql("""
                                select * from t_biz_user_heart 
                                where is_deleted = '0'
                                and entity_type =:entityType
                                and entity_id =:entityId 
                                and user_id =:userId  
                                """)
                        .bind("entityType", entityType)
                        .bind("entityId", baseFile.getId())
                        .bind("userId", currentUserId)
                        .mapProperties(TBizUserHeart.class)
                        .first().switchIfEmpty(Mono.just(new TBizUserHeart()))))
                .flatMap(tuple -> {

                    BaseFile baseFile = tuple.getT1();
                    TBizUserHeart heart = tuple.getT2();

                    Long heartId = heart.getId();
                    heart.setDeleted("0");
                    heart.setCreateTime(LocalDateTime.now());
                    heart.setEntityType(entityType);
                    heart.setUserId(currentUserId);
                    heart.setEntityId(baseFile.getId());
                    Mono<TBizUserHeart> op;
                    if (Objects.isNull(heartId)) {
                        heart.setId(r.id());
                        heart.setDeleted("0");
                        heart.setCreateTime(LocalDateTime.now());
                        heart.setEntityType(entityType);
                        heart.setUserId(currentUserId);
                        heart.setEntityId(baseFile.getId());
                        op = PostgresqlService.getBean().template().insert(heart);
                    } else {
                        heart.setDeleted("1");
                        heart.setUpdateTime(LocalDateTime.now());
                        op = PostgresqlService.getBean().template().update(heart);
                    }
                    return op;
                })
                .count()
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


    @PostMapping(value = "/convert-format/{id}")
    @ResponseBody
    public Mono<Response> convertFormat(@PathVariable Long id) {
        long currentUserId = r.defaultUserId();
        final String entityType = "t_base_file";

        return PostgresqlService.getBean().template()
                .selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .map(e -> Response.success(e));
    }

    @PostMapping(value = "/convert2m3u8/{id}")
    @ResponseBody
    public Mono<Response> convert2m3u8(@PathVariable Long id) {

        return PostgresqlService.getBean().template()
                .selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .flatMap(entity -> {
                    String absolutePath = entity.getAbsolutePath();
                    String m3u8Path = File.separator + "m3u8" + File.separator + entity.getId() + File.separator + entity.getId() + ".m3u8";
                    File fromFile = new File(absolutePath);
                    String toFilePath = r.fileBaseDir() + File.separator + "cache" + m3u8Path;
                    File toFile = new File(toFilePath);
                    r.createParentDirectories(toFile);
                    FfmpegService.convert2m3u8(
                            absolutePath, toFilePath
                    );
                    entity.setM3u8Path(m3u8Path);
                    return PostgresqlService.getBean().template().update(entity);
                })
                .map(e -> {
                    Response response = Response.success(e);
                    response.put("prevUrl", r.staticFileBasePath.replace("**", ""));
                    return response;
                });
    }

    @PostMapping(value = "/find-similar/{id}")
    @ResponseBody
    public Mono<Response> findSimilar(@PathVariable Long id) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        return PostgresqlService.getBean().template()
                .selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .map(e -> Arrays.asList(e))
//                .flatMap(entity -> {
//                    String targetPath = entity.getAbsolutePath();
//                    log.info("[findSimilar] targetPath = {}", targetPath);
////                    ORB orb = ORB.create();
//
//                    ORB orb = ORB.create(
//                            500,  // 增加特征点数量  ;特征匹配数 = 这里 * 0.4 视为相似， 默认500
//                            1.2f,  // 多尺度检测
//                            8,  // 深层金字塔
//                            31,  // 更大边缘阈值
//                            0,
//                            2,
//                            ORB.HARRIS_SCORE,  // 更好的特征点评分
//                            31,
//                            20
//                    );
//
//                    return PostgresqlService.getBean().template().getDatabaseClient()
//                            .sql("""
//                                    select * from t_base_file
//                                    where is_deleted = '0'
//                                    and type = :type
//                                    and code not in ('pixiv')
//                                    and id != :id
//                                    and size >= :min
//                                    and size <= :max
//                                    """)
//                            .bind("id", entity.getId())
//                            .bind("type", entity.getType())
//                            .bind("min", entity.getSize() - 10240000 / 4)
//                            .bind("max", entity.getSize() + 10240000 / 4)
//                            .mapProperties(BaseFile.class)
//                            .all()
//                            .filter(item -> {
//                                long totalMatches = r.matches(targetPath, item.getAbsolutePath(), orb);
////                                double totalMatches = r.matchesV2(targetPath, item.getAbsolutePath(), orb);
//                                log.info("[findSimilar] input = {}, totalMatches = {} ", item.getAbsolutePath(), totalMatches);
//                                return totalMatches >= (500 * 0.4);
//                            })
//                            .collectList()
//                            ;
//                })
                .map(e -> {
                    Response response = Response.success(e);
                    response.put("prevUrl", r.staticFileBasePath.replace("**", ""));
                    return response;
                });
    }


    /**
     * 获取所有不重复的code值（来源标识）
     * 用于文件筛选下拉框
     */
    @GetMapping(value = "/codes")
    @ResponseBody
    public Mono<Response> codes() {
        return PostgresqlService.getBean().template()
                .getDatabaseClient()
                .sql("SELECT DISTINCT code FROM t_base_file WHERE is_deleted = '0' AND code IS NOT NULL  ORDER BY code")
                .fetch()
                .all()
                .map(e -> e.get("code"))
                .collectList()
                .map(list -> Response.success(list));
    }

    /**
     * 获取所有不重复的type值（MIME类型）
     * 用于文件筛选下拉框
     */
    @GetMapping(value = "/types")
    @ResponseBody
    public Mono<Response> types() {

        return PostgresqlService.getBean().template()
                .getDatabaseClient()
                .sql("SELECT DISTINCT type FROM t_base_file WHERE is_deleted = '0' AND type IS NOT NULL  ORDER BY type")
                .fetch()
                .all()
                .map(e -> e.get("type"))
                .collectList()
                .map(list -> Response.success(list));
    }

    @PostMapping(value = "/video/copy")
    @ResponseBody
    @ActionLog(topic = "video copy", type = ActionType.UPDATE)
    public Mono<Response> videoCopy(@RequestBody JSONObject jsonObject) {
        final String id = jsonObject.getString("id");
        final String startTime = jsonObject.getString("startTime");
        final String endTime = jsonObject.getString("endTime");
        final String toPath = jsonObject.getString("toPath");

        return PostgresqlService.getBean().template()
                .selectOne(Query.query(Criteria.where("id").is(id)), BaseFile.class)
                .map(file -> {
                    String toPathName = toPath + File.separator + file.getRealName() + "-" + System.currentTimeMillis() + "." + file.getSuffix();
                    FfmpegService.copy(file.getAbsolutePath(), toPathName, startTime, endTime);
                    return file;
                })
                .map(e -> Response.success(e));
    }

}
