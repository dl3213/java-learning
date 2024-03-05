package code.sibyl.service;

import code.sibyl.domain.Database;
import code.sibyl.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataBaseService {
    private final DatabaseRepository databaseRepository;

    public Flux<Database> list() {
        return databaseRepository.list();
    }
}
