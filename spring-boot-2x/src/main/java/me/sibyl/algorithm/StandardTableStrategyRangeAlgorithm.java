//package me.sibyl.algorithm;
//
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
//
//import java.util.Arrays;
//import java.util.Collection;
//
///**
// * @author dyingleaf3213
// * @Classname StandardTableStrategyRangeAlgorithm
// * @Description TODO
// * @Create 2023/04/05 01:26
// */
//
//public class StandardTableStrategyRangeAlgorithm implements RangeShardingAlgorithm<Long> {
//    @Override
//    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
//        Long upperEndpoint = shardingValue.getValueRange().upperEndpoint();
//        Long lowerEndpoint = shardingValue.getValueRange().lowerEndpoint();
//        String logicTableName = shardingValue.getLogicTableName();
//        return Arrays.asList("warn_record_1","warn_record_2");
//    }
//}
