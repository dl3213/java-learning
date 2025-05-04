package code.sibyl.service.cdc;

import cn.hutool.core.date.DateUtil;
import code.sibyl.common.r;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer implements ObjectReader<LocalDateTime> {
    @Override
    public LocalDateTime readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        String str = jsonReader.readString();
        if (str.length() == 16) {
            Long timelong = Long.valueOf(str);
            return Instant.ofEpochMilli(timelong / 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (str.length() == 13) {
            return Instant.ofEpochMilli(Long.valueOf(str)).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            return DateUtil.parse(str).toLocalDateTime();
        }

    }
}
