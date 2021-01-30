package com.java.yxt.config;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 分库策略
 * @author zanglei
 */
public class DbShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<Long> shardingValue) {
//        for (String each : databaseNames) {
//
//            if(each.endsWith(0+"")){
//                if (shardingValue.getValue()==1 || shardingValue.getValue()==2) {
//                    return each;
//                }
//            }
//
//            if(each.endsWith(1+"")){
//                if (shardingValue.getValue()==3 || shardingValue.getValue()==4) {
//                    return each;
//                }
//            }
//
//        }
        for (String each : databaseNames) {
            return each;
        }
        throw new UnsupportedOperationException();
    }


//    @Override
//    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<Long> shardingValue) {
//        for (String each : databaseNames) {
//            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
//                return each;
//            }
//        }
//        return null;
//    }

}