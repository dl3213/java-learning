package code.sibyl.domain.database;


import code.sibyl.common.r;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

//@Component
public class DatabaseBeforeSaveCallback implements org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback<Database> {

    @Override
    public Publisher<Database> onBeforeSave(Database entity, OutboundRow row, SqlIdentifier table) {
        row.put("host", Parameter.from(r.base64Encoder().encode(entity.getHost().getBytes(StandardCharsets.UTF_8))));
        row.put("port", Parameter.from(r.base64Encoder().encode(entity.getPort().getBytes(StandardCharsets.UTF_8))));
        row.put("username", Parameter.from(r.base64Encoder().encode(entity.getUsername().getBytes(StandardCharsets.UTF_8))));
        row.put("password", Parameter.from(r.base64Encoder().encode(entity.getPassword().getBytes(StandardCharsets.UTF_8))));
        return Mono.just(entity);
    }
}