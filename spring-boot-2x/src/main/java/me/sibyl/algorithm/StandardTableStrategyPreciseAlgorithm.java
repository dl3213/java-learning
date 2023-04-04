//package me.sibyl.algorithm;
//
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.math.BigInteger;
//import java.util.Collection;
//
///**
// * @author dyingleaf3213
// * @Classname StandardTableStrategyPreciseAlgorithm
// * @Description TODO
// * @Create 2023/04/05 01:26
// */
//public class StandardTableStrategyPreciseAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
//        String logicTableName = shardingValue.getLogicTableName();
//        String columnName = shardingValue.getColumnName();
//        Long value = shardingValue.getValue();
//        BigInteger bigInteger = BigInteger.valueOf(value);
//        BigInteger add = bigInteger.mod(new BigInteger("2")).add(BigInteger.ONE);
//        String key = logicTableName + "_" + add;
//        if(!availableTargetNames.contains(key)){
//            throw new UnsupportedOperationException("tttttest");
//        }
//        return logicTableName+"_"+ add;
//    }
//}
