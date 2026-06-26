package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.domain.base.Tag;
import code.sibyl.service.sql.PostgresqlService;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文件标签管理接口
 */
@RestController
@RequestMapping("/api/rest/v1/file/tag")
@Slf4j
public class FileTagController {

    private static final String ENTITY_TYPE = "t_base_file";

    /**
     * 获取某文件的全部标签
     * GET /file/tag/list/{fileId}
     */
    @GetMapping("/list/{fileId}")
    public Mono<Response> listTags(@PathVariable Long fileId) {
        return PostgresqlService.getBean().template()
                .select(Query.query(
                        Criteria.where("is_deleted").is("0")
                                .and("entity_id").is(fileId)
                                .and("entity_type").is(ENTITY_TYPE)
                ), Tag.class)
                .collectList()
                .map(Response::success);
    }

    /**
     * 给文件添加已有标签
     * POST /file/tag/add
     * body: { fileId: Long, tagId: Long }
     */
    @PostMapping("/add")
    public Mono<Response> addTag(@RequestBody JSONObject json) {
        String fileId = json.getString("fileId");
        String tagId = json.getString("tagId");

        return PostgresqlService.getBean().template()
                .selectOne(Query.query(
                        Criteria.where("id").is(tagId)
                                .and("is_deleted").is("0")
                ), Tag.class)
                .switchIfEmpty(Mono.error(new RuntimeException("标签不存在")))
                .flatMap(tag -> {
                    return PostgresqlService.getBean().template()
                            .select(Query.query(
                                    Criteria.where("is_deleted").is("0")
                                            .and("entity_id").is(fileId)
                                            .and("entity_type").is(ENTITY_TYPE)
                                            .and("name").is(tag.getName())
                            ), Tag.class)
                            .collectList()
                            .flatMap(existing -> {
                                if (!existing.isEmpty()) {
                                    return Mono.just(Response.error("该标签已绑定到此文件"));
                                }
                                Tag newTag = new Tag();
                                newTag.setId(r.id());
                                newTag.setEntityId(Long.valueOf(String.valueOf(fileId)));
                                newTag.setEntityType(ENTITY_TYPE);
                                newTag.setName(tag.getName());
                                newTag.setDeleted("0");
                                newTag.setCreateTime(LocalDateTime.now());
                                newTag.setCreateId(r.defaultUserId());
                                return PostgresqlService.getBean().template().insert(newTag);
                            });
                })
                .map(Response::success);
    }

    /**
     * 移除文件的某个标签
     * POST /file/tag/remove
     * body: { fileId: Long, tagId: Long }
     */
    @PostMapping("/remove")
    public Mono<Response> removeTag(@RequestBody JSONObject json) {
        Long fileId = json.getLong("fileId");
        Long tagId = json.getLong("tagId");

        return PostgresqlService.getBean().template()
                .selectOne(Query.query(
                        Criteria.where("id").is(tagId)
                                .and("entity_id").is(fileId)
                                .and("entity_type").is(ENTITY_TYPE)
                ), Tag.class)
                .switchIfEmpty(Mono.error(new RuntimeException("文件标签记录不存在")))
                .flatMap(tag -> {
                    tag.setDeleted("1");
                    tag.setUpdateTime(LocalDateTime.now());
                    tag.setUpdateId(r.defaultUserId());
                    return PostgresqlService.getBean().template().update(tag);
                })
                .map(Response::success);
    }

    /**
     * 绑定标签到文件（按名称）
     * POST /file/tag/bind
     * body: { entityId: String, tag: String }
     * 如果 name + entityId 在表中不存在则新增，否则什么都不做
     */
    @PostMapping("/bind")
    public Mono<Response> bindTags(@RequestBody JSONObject json) {
        String entityId = json.getString("entityId");
        String tagName = json.getString("tag");

        if (entityId == null || entityId.isBlank() || tagName == null || tagName.isBlank()) {
            return Mono.just(Response.error("entityId 和 tag 不能为空"));
        }

        Long fileId = Long.parseLong(entityId);

        // 先查文件是否存在
        return PostgresqlService.getBean().template()
                .selectOne(Query.query(
                        Criteria.where("id").is(fileId)
                                .and("is_deleted").is("0")
                ), BaseFile.class)
                .switchIfEmpty(Mono.error(new RuntimeException("文件不存在")))
                .flatMap(file -> {
                    // 检查 name + entityId 是否已存在
                    return PostgresqlService.getBean().template()
                            .select(Query.query(
                                    Criteria.where("is_deleted").is("0")
                                            .and("entity_id").is(fileId)
                                            .and("entity_type").is(ENTITY_TYPE)
                                            .and("name").is(tagName.trim())
                            ), Tag.class)
                            .collectList()
                            .flatMap(existing -> {
                                if (!existing.isEmpty()) {
                                    // 已存在，什么都不做
                                    return Mono.empty();
                                }
                                // 不存在则新增
                                Tag newTag = new Tag();
                                newTag.setId(r.id());
                                newTag.setEntityId(Long.valueOf(String.valueOf(fileId)));
                                newTag.setEntityType(ENTITY_TYPE);
                                newTag.setName(tagName.toLowerCase().trim());
                                newTag.setDeleted("0");
                                newTag.setCreateTime(LocalDateTime.now());
                                newTag.setCreateId(r.defaultUserId());
                                return PostgresqlService.getBean().template().insert(newTag);
                            });
                })
                .map(Response::success);
    }

    /**
     * 创建新标签并绑定到文件
     * POST /file/tag/create
     * body: { fileId: Long, tagName: String }
     */
    @PostMapping("/create")
    public Mono<Response> createAndBind(@RequestBody JSONObject json) {
        Long fileId = json.getLong("fileId");
        String tagName = json.getString("tagName");

        if (fileId == null || tagName == null || tagName.isBlank()) {
            return Mono.just(Response.error("fileId 和 tagName 不能为空"));
        }

        return PostgresqlService.getBean().template()
                .selectOne(Query.query(
                        Criteria.where("id").is(fileId)
                                .and("is_deleted").is("0")
                ), BaseFile.class)
                .switchIfEmpty(Mono.error(new RuntimeException("文件不存在")))
                .flatMap(file -> {
                    Tag newTag = new Tag();
                    newTag.setId(r.id());
                    newTag.setEntityId(Long.valueOf(String.valueOf(fileId)));
                    newTag.setEntityType(ENTITY_TYPE);
                    newTag.setName(tagName.trim());
                    newTag.setDeleted("0");
                    newTag.setCreateTime(LocalDateTime.now());
                    newTag.setCreateId(r.defaultUserId());
                    return PostgresqlService.getBean().template().insert(newTag);
                })
                .map(Response::success);
    }

    /**
     * 获取所有可用标签（可按 entityType 过滤）
     * GET /file/tag/all?entityType=t_base_file
     */
    @GetMapping("/all")
    public Mono<Response> listAllTags(@RequestParam(required = false) String entityType) {
        Criteria criteria = Criteria.where("is_deleted").is("0");
        if (entityType != null && !entityType.isBlank()) {
            criteria = criteria.and("entity_type").is(entityType);
        }
        return PostgresqlService.getBean().template()
                .select(Query.query(criteria), Tag.class)
                .collectList()
                .map(Response::success);
    }

    @GetMapping("/name/distinct/all")
    public Mono<Response> nameDistinctAll(@RequestParam(required = false) String entityType) {
        Criteria criteria = Criteria.where("is_deleted").is("0");
        if (entityType != null && !entityType.isBlank()) {
            criteria = criteria.and("entity_type").is(entityType);
        }
        return PostgresqlService.getBean().template()
                .select(Query.query(criteria), Tag.class)
                .map(e -> e.getName())
                .distinct()
                .collectList()
                .map(Response::success);
    }

    /**
     * 删除标签
     * DELETE /file/tag/{tagId}
     */
    @DeleteMapping("/{tagId}")
    public Mono<Response> deleteTag(@PathVariable Long tagId) {
        return PostgresqlService.getBean().template()
                .selectOne(Query.query(
                        Criteria.where("id").is(tagId)
                ), Tag.class)
                .switchIfEmpty(Mono.error(new RuntimeException("标签不存在")))
                .flatMap(tag -> {
                    tag.setDeleted("1");
                    tag.setUpdateTime(LocalDateTime.now());
                    tag.setUpdateId(r.defaultUserId());
                    return PostgresqlService.getBean().template().update(tag);
                })
                .map(Response::success);
    }
}
