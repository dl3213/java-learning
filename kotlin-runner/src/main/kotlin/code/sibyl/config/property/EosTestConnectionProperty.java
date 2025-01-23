package code.sibyl.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConditionalOnProperty(name = "data-source.sibyl-mysql")
//@Component
//@ConfigurationProperties(prefix = "data-source.eos-test")
@Data
public class EosTestConnectionProperty {
    private String driver;
    private String host;
    private Integer port;
    private String database;
    private String user;
    private String password;
}
