package com.java.yxt.config;

import com.java.yxt.util.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @Description redisson配置
 * @author lfl
 * @date 2020年10月16日
 */
@Configuration
public class RedissonConfig {


	@Value("${redis.address.username}")
	private String username;
	@Value("${redis.address.password}")
	private String password;
	@Value("${redis.address.clusterAddress}")
	private String clusterAddress;
	@Value("${redis.address.singleAddress}")
	private String singleAddress;
	@Value("${redis.address.masterAddress}")
	private String masterAddress;
	@Value("${redis.address.slaveAddress}")
	private String slaveAddress;

	@Bean
	@ConditionalOnProperty(prefix = "redis.address", value = "enable", havingValue = "true")
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.setCodec(JsonJacksonCodec.INSTANCE);
		if (StringUtil.isNotBlank(singleAddress)) {
			// 单节点模式
			SingleServerConfig singleConfig = config.useSingleServer();
			singleConfig.setAddress(singleAddress);
			// 最小空闲连接数
			singleConfig.setConnectionMinimumIdleSize(10);

			if (StringUtil.isNotBlank(username)) {
				singleConfig.setClientName(username);
			}
			if (StringUtil.isNotBlank(password)) {
				singleConfig.setPassword(password);
			}
		} else if (StringUtil.isNotBlank(clusterAddress)) {
			// 集群模式
			ClusterServersConfig clusterServersConfig = config.useClusterServers();
			clusterServersConfig.setScanInterval(2000);

			// 可以用"rediss://"来启用SSL连接
			List<String> list = Arrays.asList(clusterAddress.split(","));
			list.stream().forEach(a -> clusterServersConfig.addNodeAddress(a));

			if (StringUtil.isNotBlank(username)) {
				clusterServersConfig.setClientName(username);
			}
			if (StringUtil.isNotBlank(password)) {
				clusterServersConfig.setPassword(password);
			}
		} else if (StringUtil.isNotBlank(masterAddress)) {
			// 主从模式
			MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
			masterSlaveServersConfig.setMasterAddress(masterAddress);
			// 可以用"rediss://"来启用SSL连接
			List<String> list = Arrays.asList(slaveAddress.split(","));
			list.stream().forEach(a -> masterSlaveServersConfig.addSlaveAddress(a));

			if (StringUtil.isNotBlank(username)) {
				masterSlaveServersConfig.setClientName(username);
			}
			if (StringUtil.isNotBlank(password)) {
				masterSlaveServersConfig.setPassword(password);
			}
		}

		return Redisson.create(config);

	}

}