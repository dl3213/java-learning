package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.biz.SysConfig;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 系统配置管理接口
 */
@RestController
@RequestMapping("/api/rest/v1/sys/config")
@Slf4j
public class SysConfigController {

    private static final String TABLE_NAME = "t_sys_config";

    /**
     * 分页列表
     * GET /sys/config/page?pageNumber=1&pageSize=10&configType=string&keyword=xxx
     */
    @GetMapping("/page")
    public Mono<Response> page(@RequestParam(defaultValue = "1") Integer pageNumber,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false) String configType,
                               @RequestParam(required = false) String keyword) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(TABLE_NAME).append(" WHERE is_deleted = '0'");

        if (configType != null && !configType.isBlank()) {
            sql.append(" AND type = '").append(configType).append("'");
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (key ILIKE '%").append(keyword).append("%' OR value ILIKE '%").append(keyword).append("%' OR name ILIKE '%").append(keyword).append("%' OR description ILIKE '%").append(keyword).append("%')");
        }
        sql.append(" ORDER BY id ASC");
        sql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((pageNumber - 1) * pageSize);

        String countSql = "SELECT COUNT(1) as count FROM " + TABLE_NAME + " WHERE is_deleted = '0'"
                + (configType != null && !configType.isBlank() ? " AND type = '" + configType + "'" : "")
                + (keyword != null && !keyword.isBlank() ? " AND (key ILIKE '%" + keyword + "%' OR value ILIKE '%" + keyword + "%' OR name ILIKE '%" + keyword + "%' OR description ILIKE '%" + keyword + "%')" : "");

        var template = r.getBean("sibyl-postgresql", org.springframework.data.r2dbc.core.R2dbcEntityTemplate.class);

        return template.getDatabaseClient()
                .sql(countSql)
                .fetch()
                .first()
                .flatMap(countRow -> {
                    long total = ((Number) countRow.get("count")).longValue();
                    return template.getDatabaseClient()
                            .sql(sql.toString())
                            .mapProperties(SysConfig.class)
                            .all()
                            .collectList()
                            .map(list -> Response.successPage(total, list, pageNumber, pageSize));
                });
    }

    /**
     * 根据key查询详情
     * GET /sys/config/{key}
     */
    @GetMapping("/{key}")
    public Mono<Response> getByKey(@PathVariable String key) {
        var template = r.getBean("sibyl-postgresql", org.springframework.data.r2dbc.core.R2dbcEntityTemplate.class);
        return template.selectOne(Query.query(
                        Criteria.where("key").is(key).and("is_deleted").is("0")
                ), SysConfig.class)
                .map(config -> Response.success(config))
                .defaultIfEmpty(Response.error("配置不存在"));
    }

    /**
     * 根据key查询值（简化返回）
     * GET /sys/config/value/{key}
     */
    @GetMapping("/value/{key}")
    public Mono<Response> getValue(@PathVariable String key) {
        var template = r.getBean("sibyl-postgresql", org.springframework.data.r2dbc.core.R2dbcEntityTemplate.class);
        return template.selectOne(Query.query(
                        Criteria.where("key").is(key).and("is_deleted").is("0")
                ), SysConfig.class)
                .map(config -> Response.success(config.getValue()))
                .defaultIfEmpty(Response.error("配置不存在"));
    }

    /**
     * 新增配置
     * POST /sys/config
     * body: { key: string, value: string, type: string, name: string, description: string, isSystem: boolean }
     */
    @PostMapping
    public Mono<Response> create(@RequestBody JSONObject json) {
        String key = json.getString("key");
        String value = json.getString("value");
        if (key == null || key.isBlank()) {
            return Mono.just(Response.error("key不能为空"));
        }

        var template = r.getBean("sibyl-postgresql", org.springframework.data.r2dbc.core.R2dbcEntityTemplate.class);

        // 检查key是否已存在
        return template.selectOne(Query.query(
                        Criteria.where("key").is(key).and("is_deleted").is("0")
                ), SysConfig.class)
                .flatMap(existing -> Mono.<Response>just(Response.error("配置键已存在: " + key)))
                .switchIfEmpty(Mono.defer(() -> {
                    SysConfig config = new SysConfig();
                    config.setId(r.nextId()); // 由数据库自动生成
                    config.setKey(key);
                    config.setValue(value != null ? value : "");
                    config.setType(json.getString("type") != null ? json.getString("type") : "string");
                    config.setName(json.getString("name"));
                    config.setDescription(json.getString("description"));
                    config.setSystem(json.getBoolean("isSystem") != null ? json.getBoolean("isSystem") : false);
                    config.setDeleted("0");
                    config.setCreateTime(LocalDateTime.now());
                    return template.insert(config).map(c -> Response.success(c));
                }));
    }

    /**
     * 更新配置
     * PUT /sys/config/{key}
     * body: { value?: string, name?: string, type?: string, description?: string }
     */
    @PutMapping("/{key}")
    public Mono<Response> update(@PathVariable String key, @RequestBody JSONObject json) {
        var template = r.getBean("sibyl-postgresql", org.springframework.data.r2dbc.core.R2dbcEntityTemplate.class);

        return template.selectOne(Query.query(
                        Criteria.where("key").is(key).and("is_deleted").is("0")
                ), SysConfig.class)
                .switchIfEmpty(Mono.error(new RuntimeException("配置不存在")))
                .flatMap(existing -> {
                    if (json.containsKey("value")) existing.setValue(json.getString("value"));
                    if (json.containsKey("name")) existing.setName(json.getString("name"));
                    if (json.containsKey("type")) existing.setType(json.getString("type"));
                    if (json.containsKey("description")) existing.setDescription(json.getString("description"));
                    existing.setUpdateTime(LocalDateTime.now());
                    return template.update(existing).map(c -> Response.success(c));
                })
                .onErrorResume(e -> Mono.just(Response.error(e.getMessage())));
    }

    /**
     * 删除配置（系统内置不可删除）
     * DELETE /sys/config/{key}
     */
    @DeleteMapping("/{key}")
    public Mono<Response> delete(@PathVariable String key) {
        var template = r.getBean("sibyl-postgresql", org.springframework.data.r2dbc.core.R2dbcEntityTemplate.class);

        return template.selectOne(Query.query(
                        Criteria.where("key").is(key).and("is_deleted").is("0")
                ), SysConfig.class)
                .switchIfEmpty(Mono.error(new RuntimeException("配置不存在")))
                .flatMap(existing -> {
                    if (Boolean.TRUE.equals(existing.isSystem())) {
                        return Mono.<Response>just(Response.error("系统内置配置不可删除: " + key));
                    }
                    existing.setDeleted("1");
                    existing.setUpdateTime(LocalDateTime.now());
                    return template.update(existing).map(c -> Response.success("删除成功"));
                })
                .onErrorResume(e -> Mono.just(Response.error(e.getMessage())));
    }
}
