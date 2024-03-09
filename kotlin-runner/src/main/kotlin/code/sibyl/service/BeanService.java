package code.sibyl.service;

import code.sibyl.repository.DatabaseRepository;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class BeanService {

    private final Map<String,ConnectionFactoryOptions> map = new HashMap<>();

    private final DatabaseRepository databaseRepository;


    //static静态块 > 构造方法 > @PostConstruct注解 > InitializingBean接口
    @PostConstruct
    public void init() {
        log.info("BeanService . init ");
    }

    public long test() {
        return System.currentTimeMillis();
    }
}
