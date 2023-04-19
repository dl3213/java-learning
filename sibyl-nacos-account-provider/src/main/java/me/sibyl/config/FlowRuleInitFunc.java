//package me.sibyl.config;
//
//import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
//import com.alibaba.cloud.sentinel.datasource.factorybean.NacosDataSourceFactoryBean;
//import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
//import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
//import com.alibaba.csp.sentinel.init.InitFunc;
//import com.alibaba.csp.sentinel.slots.block.RuleConstant;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author dyingleaf3213
// * @Classname FlowRuleInitFunc
// * @Description TODO
// * @Create 2023/04/19 22:14
// */
//
//public class FlowRuleInitFunc implements InitFunc {
//
//    private final String nacosAddress ="http://localhost:8848";
//    private final String groupId ="sentinel-group";
//    private final String dataId ="springboot-sentinel";
//
//    @Override
//    public void init() throws Exception {
//
//        ReadableDataSource<String,List<FlowRule>> dataSource =
//                new NacosDataSource<List<FlowRule>>(
//                        nacosAddress,
//                        groupId,
//                        dataId,
//                        source-> JSONObject.parseObject(source, new TypeReference<List<FlowRule>>(){})
//                );
//        FlowRuleManager.register2Property(dataSource.getProperty());
//    }
//}
