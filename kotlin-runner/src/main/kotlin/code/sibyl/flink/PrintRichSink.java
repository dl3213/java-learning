package code.sibyl.flink;

import com.alibaba.fastjson2.JSONObject;
import com.ververica.cdc.common.event.Event;
import com.ververica.cdc.common.sink.FlinkSinkFunctionProvider;
import com.ververica.cdc.common.sink.FlinkSinkProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import java.util.function.LongConsumer;

//todo
//@Component
@Slf4j
public class PrintRichSink extends RichSinkFunction<String> {

    @Override
    public void invoke(String value, Context context) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(value);
        System.err.println(STR."in sink -> \{jsonObject}");

    }

}
