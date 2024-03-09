package code.sibyl.domain.database;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class DatabaseConverter implements Converter<Database, Database> {

    @Override
    public Database convert(Database database) {
        System.err.println("DatabaseConverter");
        return database;
    }
}