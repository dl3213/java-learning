package code.sibyl.service.sql;

import code.sibyl.common.r;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SibylPostgresqlService {

    @Autowired
    @Qualifier("sibyl-postgresql")
    private R2dbcEntityTemplate sibylPostgresqlTemplate;

    public static SibylPostgresqlService getBean() {
        return r.getBean(SibylPostgresqlService.class);
    }

    public R2dbcEntityTemplate template(){
        return sibylPostgresqlTemplate;
    }
}
