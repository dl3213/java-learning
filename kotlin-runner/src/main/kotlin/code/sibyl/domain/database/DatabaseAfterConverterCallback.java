package code.sibyl.domain.database;


import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class DatabaseAfterConverterCallback implements org.springframework.data.r2dbc.mapping.event.AfterConvertCallback<Database> {

    @Autowired
    Base64.Decoder decoder;

    @Override
    public Publisher<Database> onAfterConvert(Database entity, SqlIdentifier table) {
        entity.setHost(new String(decoder.decode(entity.getHost().getBytes(StandardCharsets.UTF_8))));
        entity.setPort(new String(decoder.decode(entity.getPort().getBytes(StandardCharsets.UTF_8))));
        entity.setUsername(new String(decoder.decode(entity.getUsername().getBytes(StandardCharsets.UTF_8))));
        entity.setPassword(new String(decoder.decode(entity.getPassword().getBytes(StandardCharsets.UTF_8))));
        return Mono.just(entity);
    }
}