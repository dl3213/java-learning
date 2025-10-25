package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.Box;
import code.sibyl.service.sql.PostgresqlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/rest/v1/base/box")
@Slf4j
public class BoxController {

    @RequestMapping(value = "/list/{code}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> list(@PathVariable String code) {
        return PostgresqlService.getBean().template()
                .select(Query.query(Criteria.where("is_deleted").is("0").and("code").is(code)).sort(Sort.sort(Box.class).by(Box::getCreateTime).ascending()), Box.class)
                .collectList()
                .map(e -> Response.success(e));
    }

    @RequestMapping(value = "/detail/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> detail(@PathVariable String id) {

        return PostgresqlService.getBean().selectById(id, new Box())
                .map(e -> Response.success(e));
    }

    @RequestMapping(value = "/put", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> put(@RequestBody Box entity) {
        String code = entity.getCode();
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("error: code is blank");
        }

        boolean isInsert = Objects.isNull(entity.getId());
        if (isInsert) {
            entity.setId(r.id());
            entity.setCreateId(r.defaultUserId());
            entity.setCreateTime(LocalDateTime.now());
        } else {
            entity.setUpdateId(r.defaultUserId());
            entity.setUpdateTime(LocalDateTime.now());
        }


        return isInsert ?
                PostgresqlService.getBean().template()
                        .insert(entity)
                        .map(e -> Response.success(e))
                :
                PostgresqlService.getBean().template()
                        .update(entity)
                        .map(e -> Response.success(e));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> insert(@RequestBody Box entity) {
        String code = entity.getCode();
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("error: code is blank");
        }
        entity.setId(r.id());
        entity.setCreateId(r.defaultUserId());
        entity.setCreateTime(LocalDateTime.now());
        return PostgresqlService.getBean().template()
                .insert(entity)
                .map(e -> Response.success(e));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> update(@RequestBody Box entity) {
        String code = entity.getCode();
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("error: code is blank");
        }
        entity.setUpdateId(r.defaultUserId());
        entity.setUpdateTime(LocalDateTime.now());
        return PostgresqlService.getBean().template()
                .update(entity)
                .map(e -> Response.success(e));
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public Mono<Response> delete(@PathVariable String id) {
        return PostgresqlService.getBean().selectById(id, new Box())
                .flatMap(entity -> PostgresqlService.getBean().deleteById(entity.getId(), "t_base_box"))
                .map(e -> Response.success(e));
    }


}
