package com.xms.house.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

/*
 * ribbon配置类
 */
public class NewRuleConfig {
	
	@Autowired
	private IClientConfig ribbonClientConfig;
	
	/*
	 * 每隔10秒发送一次ping命令
	 */
	@Bean
	public IPing ribbonPing(IClientConfig config){
		return new PingUrl(false,"/health");
	}
	
	/**
	 * 负载均衡规则
	 * @param config
	 * @return
	 */
	@Bean
	public IRule ribbonRule(IClientConfig config){
//		return new RandomRule();
		return new AvailabilityFilteringRule();//每次访问做记录  然后根据策略 再次访问会着重访问访问成功的服务器
	}

}
