/**
 * 
 */
package com.xms.house.inteceptor;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.google.common.base.Joiner;
import com.xms.house.common.CommonConstants;
import com.xms.house.common.UserContext;
import com.xms.house.dao.UserDao;
import com.xms.house.model.User;

/**
 * 鉴权安全拦截
 * 假如需要访问受保护的页面 查询是否存在token 不存在直接转到登录页
 * 存在直接跳转
 * @author xie
 *
 */
@Component//注册为bean
public class AuthInterceptor implements HandlerInterceptor {
  
  private static final String TOKEN_COOKIE = "token";
  
  
  @Autowired
  private UserDao userDao;

//执行之前拦截
  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
          throws Exception {
	//使用GetMap获取前端传来的所有数据
    Map<String, String[]> map = req.getParameterMap();
    map.forEach((k,v) ->req.setAttribute(k, Joiner.on(",").join(v)));
    
  //获取是否为静态资源请求或者error请求
    String requestURI = req.getRequestURI();
    if (requestURI.startsWith("/static") || requestURI.startsWith("/error")) {
      return true;
    }
    //获取tokenCookie
    Cookie cookie = WebUtils.getCookie(req, TOKEN_COOKIE);
    if (cookie != null && StringUtils.isNoneBlank(cookie.getValue())) {
        User user = userDao.getUserByToken(cookie.getValue());
        if (user != null) {
          req.setAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE, user);
//          req.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
          UserContext.setUser(user);
        }
    }
    return true;
  }
  
  //方法执行之后执行
  @Override
  public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler,
          ModelAndView modelAndView) throws Exception {
    String requestURI = req.getRequestURI();
    if (requestURI.startsWith("/static") || requestURI.startsWith("/error")) {
      return ;
    }
    //登录之后 在线程池中获取用户
    User user = UserContext.getUser();
    if (user != null && StringUtils.isNoneBlank(user.getToken())) {
    	//登出之后token以空串来代替
       String token = requestURI.startsWith("logout")? "" : user.getToken();
       Cookie cookie = new Cookie(TOKEN_COOKIE, token);
       cookie.setPath("/");
       cookie.setHttpOnly(false);
       res.addCookie(cookie);//添加cookie
    }
    
  }
  
  

  @Override
  public void afterCompletion(HttpServletRequest req, HttpServletResponse response, Object handler, Exception ex)
          throws Exception {
    UserContext.remove();
  }
}
