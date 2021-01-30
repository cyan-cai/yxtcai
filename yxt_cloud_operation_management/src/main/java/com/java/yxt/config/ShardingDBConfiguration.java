package com.java.yxt.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.collect.Lists;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ShardingDBConfiguration
 *
 * @author zanglei
 * @version V1.0
 * @description shardingjdbc配置
 * @Package com.java.yxt.com.java.yxt.config
 * @date 2020/7/14
 */
@Configuration
@MapperScan(basePackages = {"com.java.yxt.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class ShardingDBConfiguration {


    @Autowired
    private PaginationInterceptor paginationInterceptor;

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Value("${spring.shardingsphere.datasource.mgt.publickey}")
    private String publickey;

    private static final String DS_0_MASTER = "ds0Master";
    private static final String DATA_SOURCE_NAME = "mgt";

    private static final String USER_SHARDING_DATA_SOURCE = "mgtSharding";

    @Bean(DS_0_MASTER)
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.mgt")
    public DataSource dsUser1(){
        DataSource dataSource =  DataSourceBuilder.create().type(dataSourceType).build();
        return dataSource;
    }

    @Bean(USER_SHARDING_DATA_SOURCE)
    public DataSource ds(@Qualifier(DS_0_MASTER) DataSource ds0) throws Exception {
        DruidDataSource druidDataSource = (DruidDataSource)ds0;
        /**
         * 密码加密配置
         */
        Properties properties=new Properties();
        properties.setProperty("druid.filters","config");
        properties.setProperty("druid.filter.config.enabled","true");
        properties.setProperty("config.decrypt","true");
        properties.setProperty("config.decrypt.key",publickey);

        druidDataSource.setConnectProperties(properties);
        Map<String, DataSource> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put(DATA_SOURCE_NAME, druidDataSource);
        ShardingRuleConfiguration userRule = getUserRule();
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, userRule,properties);
    }


    /**
     * 配置分片规则
     * @return
     */
    private ShardingRuleConfiguration getUserRule(){
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultKeyGeneratorConfig(getKeyGeneratorConfiguration());
        shardingRuleConfig.setDefaultDataSourceName(DATA_SOURCE_NAME);
        shardingRuleConfig.setTableRuleConfigs(getTableRuleConfiguration());
        return shardingRuleConfig;
    }

    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
       KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        return result;
    }

    Collection<TableRuleConfiguration> getTableRuleConfiguration() {
        Collection<TableRuleConfiguration> configurations = Lists.newArrayList();
        TableRuleConfiguration result = new TableRuleConfiguration("mgt_api_strategy");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_terminal");
        configurations.add(result);
//        result = new TableRuleConfiguration("mgt_customer");
//        configurations.add(result);
        result = new TableRuleConfiguration("mgt_api");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_api_relation_catalog");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_terminal_callback_service");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_service");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_terminal_white");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_terminal_black");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_production");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_production_api_relation");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_ip_white");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_terminal_black");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_service_strategy");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_api_catalog");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_customer_tenant_relation");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_terminal_device");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_device_audit_detail");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_device_info");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_secret_key");
        configurations.add(result);
        result = new TableRuleConfiguration("mgt_key_history");
        configurations.add(result);
        return configurations;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory userSqlSessionFactory(@Qualifier(USER_SHARDING_DATA_SOURCE) DataSource dataSource) throws Exception{
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //关键代码 设置分页插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{paginationInterceptor});
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("transaction")
    public DataSourceTransactionManager userTransactionManager(@Qualifier(USER_SHARDING_DATA_SOURCE) DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
