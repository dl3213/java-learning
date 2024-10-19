package code.sibyl.service;

import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public static QueryService getBean() {
        return r.getBean(QueryService.class);
    }

    public Flux<BaseFile> fileList(String fileName, String type) {
        return r2dbcEntityTemplate.select(Query.query(Criteria.where("is_deleted").is("0").and("file_name").is(fileName).and("type").is(type)), BaseFile.class);
    }
}
