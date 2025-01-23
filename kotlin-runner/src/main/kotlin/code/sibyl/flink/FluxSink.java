package code.sibyl.flink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.util.PrintSinkOutputWriter;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.sink2.SinkWriter;
import org.apache.flink.api.connector.sink2.WriterInitContext;

import java.io.IOException;

//todo
//@Component
@Slf4j
public class FluxSink implements Sink<String> {

//    @Override
//    public void invoke(String value, Context context) throws Exception {
//        JSONObject jsonObject = JSONObject.parseObject(value);
//        System.err.println(STR."in sink -> \{jsonObject}");
//
//    }


    @Override
    public SinkWriter<String> createWriter(WriterInitContext context) throws IOException {

        return Sink.super.createWriter(context);
    }

    @Override
    public SinkWriter<String> createWriter(InitContext context) throws IOException {
        System.err.println(context.asSerializationSchemaInitializationContext());
//        context.get
        PrintSinkOutputWriter<String> writer = new PrintSinkOutputWriter<>();

        return writer;
    }
}
