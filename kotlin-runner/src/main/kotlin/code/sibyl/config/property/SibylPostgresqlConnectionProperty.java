package code.sibyl.config.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConditionalOnProperty(name = "data-source", havingValue = "sibyl-postgresql", matchIfMissing = false)
@Component
@ConfigurationProperties(prefix = "data-source.sibyl-postgresql")
@Data
public class SibylPostgresqlConnectionProperty {
    private String driver;
    private String host;
    private Integer port;
    private String database;
    private String user;
    private String password;
}
