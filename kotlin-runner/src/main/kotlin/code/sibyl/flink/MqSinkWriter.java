package code.sibyl.flink;

import code.sibyl.common.SpringUtil;
import code.sibyl.mq.rabbit.MessageProducer;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.connector.sink2.SinkWriter;

import java.io.IOException;

@Slf4j
public class MqSinkWriter implements SinkWriter {
    @Override
    public void write(Object element, Context context) throws IOException, InterruptedException {
        log.info("Sinking element: {}", element);
//        String string = (String) element;
//        JSONObject jsonObject = JSONObject.parseObject(string);
//        JSONObject source = jsonObject.getJSONObject("source");
//        String db = source.getString("db");
//        String table = source.getString("table");
//        String op = jsonObject.getString("op");
//        MessageProducer.getBean().send(STR."flink_cdc.\{db}.\{table}.\{op}", string);

    }

    @Override
    public void flush(boolean endOfInput) throws IOException, InterruptedException {
        log.info("Flushing sink...");
    }

    @Override
    public void close() throws Exception {
        log.info("Closing sink writer...");
    }
}
