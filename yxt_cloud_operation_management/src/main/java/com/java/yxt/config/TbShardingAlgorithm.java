package com.java.yxt.config;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 *  
 * @author kelvin
 *
 */
public class TbShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<String> shardingValue) {
        String tablename = shardingValue.getLogicTableName();
        String key = String.valueOf(shardingValue.getValue()).replace("-","").substring(0,6);
        return tablename+"_"+key;
    }

}  

