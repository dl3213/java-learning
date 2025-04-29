package code.sibyl.flink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.data.RowData;

public class MultiOutputSink extends RichSinkFunction<RowData> {
//    private ElasticsearchClient elasticsearchClient;
//    private HdfsClient hdfsClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
//        elasticsearchClient = new ElasticsearchClient(); // 初始化 Elasticsearch 客户端
//        hdfsClient = new HdfsClient(); // 初始化 HDFS 客户端
    }

    @Override
    public void invoke(RowData value, Context context) throws Exception {
//        elasticsearchClient.write(value); // 写入 Elasticsearch
//        hdfsClient.write(value); // 写入 HDFS
    }
}
