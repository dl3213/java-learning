package code.sibyl.service.sql;

import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

//@Service
//@Slf4j
@RequiredArgsConstructor
public class H2Service {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public static H2Service getBean() {
        return r.getBean(H2Service.class);
    }

    public R2dbcEntityTemplate template(){
        return r2dbcEntityTemplate;
    }

    public Flux<BaseFile> fileList(String fileName, String type) {
        return r2dbcEntityTemplate.select(Query.query(Criteria.where("is_deleted").is("0").and("file_name").is(fileName).and("type").is(type)), BaseFile.class);
    }

    public Flux<BaseFile> fileListByHash(String hash) {
        return r2dbcEntityTemplate.select(Query.query(Criteria.where("is_deleted").is("0").and("SHA256").is(hash)), BaseFile.class);
    }
}
