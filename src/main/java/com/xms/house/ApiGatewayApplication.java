package com.xms.house;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xms.house.autoconfig.NewRuleConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
//@RibbonClient(name="user",configuration=NewRuleConfig.class)//使用ribbo自动注解 指定为user的微服务 策略为NewRuleConfig的文件
public class ApiGatewayApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	/**
	 * 获取用户调用实例
	 * @return
	 */
	@RequestMapping("index1")
	@ResponseBody
	public List<ServiceInstance> getReister(){
	  return discoveryClient.getInstances("user");
	}
}
