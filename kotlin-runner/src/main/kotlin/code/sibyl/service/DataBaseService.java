package code.sibyl.service;

import code.sibyl.domain.Database;
import code.sibyl.repository.DatabaseRepository;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataBaseService {
    private final DatabaseRepository databaseRepository;

    public Flux<Database> list() {
        return databaseRepository.list();
    }

    public void connect(String id) {
        databaseRepository.findById(Long.valueOf(id)).doOnError(e -> {
            System.err.println("findById error");
        }).doOnSuccess(database -> {
            System.err.println("findById succ");
            System.err.println(database);
            log.info("BeanService . init ");
            ConnectionFactory factory = ConnectionFactories.get(
                    builder()
                            .option(DRIVER, database.getType())
                            .option(HOST, database.getHost())
                            .option(PORT, Integer.valueOf(database.getPort()))
                            .option(USER, database.getUsername())
                            .option(PASSWORD, database.getPassword())
                            .option(DATABASE, database.getDatabase())
                            .build());
            DatabaseClient databaseClient = DatabaseClient.create(factory);
            databaseClient.sql("select * from ods_xcl_eos_org_organization").fetch().all().map(m -> {
                System.err.println(m);
                return m;
            }).subscribe();
        }).subscribe();
    }
}
