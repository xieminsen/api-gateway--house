package com.xms.house.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xms.house.common.RestResponse;
import com.xms.house.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("test/getusername")
	public RestResponse<String> getusername(Long id) {
		
		return RestResponse.success(userService.getusername(id));
	}
}
