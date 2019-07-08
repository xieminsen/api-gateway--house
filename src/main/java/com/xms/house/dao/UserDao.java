package com.xms.house.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

import com.xms.house.autoconfig.GenericRest;

@Repository
public class UserDao {

	@Autowired
	private GenericRest rest;
	
	public String getusername(Long id) {
		String url = "direct://http://127.0.0.1:8083/getusername?id="+id;//使用直连bean
/*		String url = "http://user/getusername?id="+id;//使用load负载均衡bean
*/		return rest.get(url, new ParameterizedTypeReference<String>(){}).getBody();
		
	}
	
}
