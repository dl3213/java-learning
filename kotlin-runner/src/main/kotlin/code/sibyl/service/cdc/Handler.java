package code.sibyl.service.cdc;

import code.sibyl.common.r;
import code.sibyl.config.adapter.LocalDateTimeTypeAdapter;
import code.sibyl.config.adapter.LocalDateTypeAdapter;
import code.sibyl.domain.base.BaseFile;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.filter.NameFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface Handler<T> {

    String beanNamePrev = "cdc-handler-";

    default Class getEntityClass() {
        return getClass();
    }

    default Mono<T> handler(String jsonStr) {

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        // todo 可以自定义转换
        JSONObject source = jsonObject.getJSONObject("source");
        String table = source.getString("table");
        String before = jsonObject.getString("before");
        T beforeEntity = (T) JSONObject.parseObject(before, getEntityClass(), JSONReader.Feature.SupportSmartMatch);
        String after = jsonObject.getString("after");
        T afterEntity = (T) JSONObject.parseObject(after, getEntityClass(), JSONReader.Feature.SupportSmartMatch);
        String op = jsonObject.getString("op");
        if (StringUtils.isBlank(op)) {
            return Mono.error(new NullPointerException("op is blank"));
        }
        switch (op) {
            case "c" -> {
                return this.insert(afterEntity);
            }
            case "u" -> {
                return this.update(beforeEntity, afterEntity);
            }
            case "d" -> {
                return this.delete(beforeEntity);
            }
        }
        return Mono.error(new UnsupportedOperationException(STR."op is \{op}"));
    }

    default Mono<T> insert(T afterEntity) {
        return Mono.just(afterEntity);
    }

    default Mono<T> update(T beforeEntity, T afterEntity) {
        return Mono.just(afterEntity);
    }

    default Mono<T> delete(T beforeEntity) {
        return Mono.just(beforeEntity);
    }


    public static void main(String[] args) {
        // 1746332842430451 = 2025-05-04 04:27:22.430451
        String str = "{\n" +
                "        \"id\": 1918764339091869696,\n" +
                "        \"file_name\": \"1918764339091869696.txt\",\n" +
                "        \"real_name\": \"ValveUnhandledExceptionFilter.txt\",\n" +
                "        \"type\": \"text/plain\",\n" +
                "        \"absolute_path\": \"E:/sibyl-system/file/2025-05-04/1918764339091869696.txt\",\n" +
                "        \"relative_path\": \"2025-05-04\\\\1918764339091869696.txt\",\n" +
                "        \"size\": 29,\n" +
                "        \"suffix\": \"txt\",\n" +
                "        \"serial_number\": null,\n" +
                "        \"sha256\": null,\n" +
                "        \"code\": null,\n" +
                "        \"width\": null,\n" +
                "        \"height\": null,\n" +
                "        \"is_deleted\": \"0\",\n" +
                "        \"create_time\": 1746332842430451,\n" +
                "        \"create_id\": 0,\n" +
                "        \"update_time\": null,\n" +
                "        \"update_id\": null,\n" +
                "        \"thumbnail\": null,\n" +
                "        \"click_count\": 0\n" +
                "    }";
        System.err.println("1746251168197081".length());
        System.err.println(String.valueOf(System.currentTimeMillis()).length());
        System.err.println(JSONObject.toJSONString(JSONObject.parseObject(str, BaseFile.class,  JSONReader.Feature.SupportSmartMatch, JSONReader.Feature.SupportClassForName, JSONReader.Feature.NonStringKeyAsString)));

    }

}
