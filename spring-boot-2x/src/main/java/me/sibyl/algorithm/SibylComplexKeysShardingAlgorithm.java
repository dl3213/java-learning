//package me.sibyl.algorithm;
//
//import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @author dyingleaf3213
// * @Classname SibylComplexKeysShardingAlgorithm
// * @Description 入库策略
// * @Create 2023/04/05 01:38
// */
//
//public class SibylComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<LocalDateTime> {
//    @Override
//    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
//        Collection<String> table_suffix = new ArrayList<>();
//        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
//
//        String logicTableName = shardingValue.getLogicTableName();
//        Map<String, Collection> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
//        columnNameAndShardingValuesMap.entrySet().stream().forEach(entry ->{
//            for (Object item : entry.getValue()) {
//                LocalDateTime createTime = (LocalDateTime) item;
//                String format = yyyyMM.format(createTime);
//                String tableName = String.valueOf(logicTableName + "_" + format);
//                table_suffix.add(tableName);
//            }
//        });
//
//        return table_suffix;
//    }
//}
