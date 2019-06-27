package com.xms.house.autoconfig;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({HttpClient.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

	private final HttpClientProperties properties;
	
	public HttpClientAutoConfiguration(HttpClientProperties properties){
		this.properties = properties;
	}
	
	/**
	 * httpclient bean 的定义
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(HttpClient.class)
	public HttpClient httpClient(){
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(properties.getConnectTimeOut())//连接超时
				.setSocketTimeout(properties.getSocketTimeOut())//读超时
				.build();//构建requestConfig
		HttpClient client = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setUserAgent(properties.getAgent())
				.setMaxConnPerRoute(properties.getMaxConnPerRoute())//每一个服务节点最大的连接数
				.setMaxConnTotal(properties.getMaxConnTotaol())//服务节点最大总连接数
				//.setConnectionReuseStrategy(new NoConnectionReuseStrategy())//设置连接重用的策略
				.build();
		return client;
	}
}
