package code.sibyl.config;

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

public class R2dbdRoutingConfig extends AbstractRoutingConnectionFactory {
    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return null;
    }
}
