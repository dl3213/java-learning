package code.sibyl.config.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConditionalOnProperty(name = "data-source.sibyl-mysql")
@Component
@ConfigurationProperties(prefix = "data-source.sibyl-mysql")
@Data
public class SibylMysqlConnectionProperty {
    private String driver;
    private String host;
    private Integer port;
    private String database;
    private String user;
    private String password;
}
