package code.sibyl.service.sql;

import code.sibyl.common.r;
import code.sibyl.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SibylMysqlService {

    @Autowired
    @Qualifier("sibyl-mysql")
    private R2dbcEntityTemplate sibylMysqlTemplate;

    public static SibylMysqlService getBean() {
        return r.getBean(SibylMysqlService.class);
    }

    public R2dbcEntityTemplate template(){
        return sibylMysqlTemplate;
    }
}
