package com.xms.house.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.xms.house.autoconfig.GenericRest;
import com.xms.house.common.RestResponse;
import com.xms.house.model.Agency;
import com.xms.house.model.ListResponse;
import com.xms.house.model.User;
import com.xms.house.utils.Rests;

@Repository
//断路器配置 userDao的最低连接池
@DefaultProperties(groupKey="userDao",threadPoolKey="userDao")
public class UserDao {

	@Autowired
	private GenericRest rest;
	
	 @Value("${user.service.name}")
	 private String userServiceName;
	 
	public String getusername(Long id) {
		//String url = "direct://http://127.0.0.1:8083/getusername?id="+id;//使用直连bean
		String url = "http://user/getusername?id="+id;//使用load负载均衡bean
		return rest.get(url, new ParameterizedTypeReference<String>(){}).getBody();
		
	}

	/**
	 * 获取用户列表
	 * @param query
	 * @return
	 */
	  @HystrixCommand
	  public List<User> getUserList(User query) {
	    ResponseEntity<RestResponse<List<User>>> resultEntity = rest.post("http://"+ userServiceName + "/user/getList",query, new ParameterizedTypeReference<RestResponse<List<User>>>() {});
	    RestResponse<List<User>> restResponse  = resultEntity.getBody();
	    if (restResponse.getCode() == 0) {
	        return restResponse.getResult();
	    }else {
	        return null;
	    }
	  }

	  /**
	   * 添加用户
	   * @param account
	   * @return
	   */
	  @HystrixCommand
	  public User addUser(User account) {
	    String url = "http://" + userServiceName + "/user/add";
	    ResponseEntity<RestResponse<User>> responseEntity = rest.post(url,account, new ParameterizedTypeReference<RestResponse<User>>() {});
	    RestResponse<User> response = responseEntity.getBody();
	    if (response.getCode() == 0) {
	       return response.getResult();
	    }{
	       throw new IllegalStateException("Can not add user");
	    }
	    
	  }

	  @HystrixCommand
	  public User authUser(User user) {
	    String url = "http://" + userServiceName + "/user/auth";
	    ResponseEntity<RestResponse<User>> responseEntity =  rest.post(url, user, new ParameterizedTypeReference<RestResponse<User>>() {});
	    RestResponse<User> response = responseEntity.getBody();
	    if (response.getCode() == 0) {
	      return response.getResult();
	   }{
	      throw new IllegalStateException("Can not add user");
	   }
	  }

	  @HystrixCommand
	  public void logout(String token) {
	    String url = "http://" + userServiceName + "/user/logout?token=" + token;
	    rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {});
	  }

	  public User getUserByTokenFb(String token){
	    return new User();
	  }
	  
	  /**
	   * 调用鉴权服务
	   * @param token
	   * @return
	   */
	  @HystrixCommand(fallbackMethod="getUserByTokenFb")
	  public User getUserByToken(String token) {
	    String url = "http://" + userServiceName + "/user/get?token=" + token;
	    ResponseEntity<RestResponse<User>> responseEntity = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {});
	    RestResponse<User> response = responseEntity.getBody();
	    if (response == null || response.getCode() != 0) {
	       return null;
	    }
	    return response.getResult();
	  }

	  @HystrixCommand
	  public List<Agency> getAllAgency() {
	    return Rests.exc(() ->{
	      String url = Rests.toUrl(userServiceName, "/agency/list");
	      ResponseEntity<RestResponse<List<Agency>>> responseEntity =
	          rest.get(url, new ParameterizedTypeReference<RestResponse<List<Agency>>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  } 
	  
	  
	  @HystrixCommand
	  public User updateUser(User user) {
	    return Rests.exc(() ->{
	      String url = Rests.toUrl(userServiceName, "/user/update");
	      ResponseEntity<RestResponse<User>> responseEntity =
	          rest.post(url, user, new ParameterizedTypeReference<RestResponse<User>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  }

	  @HystrixCommand
	  public User getAgentById(Long id) {
	   return Rests.exc(() ->{
	      String url = Rests.toUrl(userServiceName, "/agency/agentDetail?id=" +id);
	      ResponseEntity<RestResponse<User>> responseEntity =
	          rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  }
	  
	  /**
	   * 用户激活
	   * @param key
	   */
	  @HystrixCommand
	  public boolean enable(String key) {
	    Rests.exc(() ->{
	        String url = Rests.toUrl(userServiceName, "/user/enable?key=" + key);
	        ResponseEntity<RestResponse<Object>> responseEntity =
	            rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {});
	        return responseEntity.getBody();
	      }
	    );
	    return true;
	  }
	  
	  
	  @HystrixCommand
	  public Agency getAgencyById(Integer id) {
	    return Rests.exc(() -> {
	      String url = Rests.toUrl(userServiceName, "/agency/agencyDetail?id=" + id);
	      ResponseEntity<RestResponse<Agency>> responseEntity =
	          rest.get(url, new ParameterizedTypeReference<RestResponse<Agency>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  }
	  
	  
	  @HystrixCommand
	  public void addAgency(Agency agency) {
	     Rests.exc(() -> {
	      String url = Rests.toUrl(userServiceName, "/agency/add");
	      ResponseEntity<RestResponse<Object>> responseEntity =
	          rest.post(url, agency,new ParameterizedTypeReference<RestResponse<Object>>() {});
	      return responseEntity.getBody();
	    });
	  }
	  
	  
	  @HystrixCommand
	  public ListResponse<User> getAgentList(Integer limit, Integer offset) {
	    return Rests.exc(() -> {
	      String url = Rests.toUrl(userServiceName, "/agency/agentList?limit=" + limit + "&offset="+offset);
	      ResponseEntity<RestResponse<ListResponse<User>>> responseEntity =
	          rest.get(url,new ParameterizedTypeReference<RestResponse<ListResponse<User>>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  }
	  
	  
	  @HystrixCommand
	  public String getEmail(String key) {
	    return Rests.exc(() -> {
	      String url = Rests.toUrl(userServiceName, "/user/getKeyEmail?key=" + key);
	      ResponseEntity<RestResponse<String>> responseEntity =
	          rest.get(url,new ParameterizedTypeReference<RestResponse<String>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  }
	  
	  
	  @HystrixCommand
	  public User reset(String key, String password) {
	    return Rests.exc(() -> {
	      String url = Rests.toUrl(userServiceName, "/user/reset?key=" + key + "&password="+password);
	      ResponseEntity<RestResponse<User>> responseEntity =
	          rest.get(url,new ParameterizedTypeReference<RestResponse<User>>() {});
	      return responseEntity.getBody();
	    }).getResult();
	  }
	  
	  
	  @HystrixCommand
	  public void resetNotify(String email, String url) {
	     Rests.exc(() -> {
	          String sendUrl = Rests.toUrl(userServiceName, "/user/resetNotify?email=" + email + "&url="+url);
	          rest.get(sendUrl,new ParameterizedTypeReference<RestResponse<Object>>() {});
	          return new Object();
	    });
	  }
}
