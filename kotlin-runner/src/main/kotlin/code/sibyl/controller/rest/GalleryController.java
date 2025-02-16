package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.domain.base.BaseFile;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/rest/v1/gallery")
@Slf4j
public class GalleryController {

    @Autowired
//    @Qualifier("sibyl-postgresql")
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @PostMapping(value = "/page")
    @ResponseBody
    public Mono<Response> page(@RequestBody JSONObject jsonObject) {
        Criteria criteria = Criteria.where("IS_DELETED").is("0"); //.and("type").like("image%");

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
            sha256Query = r2dbcEntityTemplate.getDatabaseClient()
                    .sql(""" 
                            select * from (
                                select sha256, count(1) as count from T_BASE_FILE
                                where is_deleted = '0'
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
        System.err.println(jsonObject);
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

                    return Mono.zip(r2dbcEntityTemplate.count(query, BaseFile.class), r2dbcEntityTemplate.select(query, BaseFile.class).collectList());
                })
                .map(t -> Response.successPage(t.getT1(), t.getT2(), pageNumber, pageSize));

    }
}
