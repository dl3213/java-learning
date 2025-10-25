package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.Box;
import code.sibyl.domain.base.Tag;
import code.sibyl.service.sql.PostgresqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rest/v1/base/tag")
@Slf4j
public class TagController {

    @RequestMapping(value = "/list/{entityId}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> list(@PathVariable String entityId) {

        return PostgresqlService.getBean().template()
                .select(Query.query(Criteria.where("is_deleted").is("0").and("entity_id").is(entityId)), Tag.class)
                .collectList()
                .map(e -> Response.success(e));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Response> list(@RequestBody Tag entity) {
        entity.setId(r.id());
        return PostgresqlService.getBean().template()
                .insert(entity)
                .map(e -> Response.success(e));
    }
}
