package com.xms.house.inteceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.netflix.governator.annotations.binding.Region;
/**
 * 是用来调整上边两个拦截
 * @author xie
 *
 */
@Configuration//添加为springbean
public class WebMvcConf extends WebMvcConfigurerAdapter {

  @Autowired
  private AuthInterceptor authInterceptor;
  
  @Autowired
  private AuthActionInterceptor authActionInterceptor;
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
    registry
        .addInterceptor(authActionInterceptor)
         .addPathPatterns("/house/toAdd")
        .addPathPatterns("/accounts/profile").addPathPatterns("/accounts/profileSubmit")
        .addPathPatterns("/house/bookmarked").addPathPatterns("/house/del")
        .addPathPatterns("/house/ownlist").addPathPatterns("/house/add")
        .addPathPatterns("/house/toAdd").addPathPatterns("/agency/agentMsg")
        .addPathPatterns("/comment/leaveComment").addPathPatterns("/comment/leaveBlogComment");
    
    super.addInterceptors(registry);
  }

  /**
   * cors跨域 全局设置
   * @param coreRegistry
   */
/*  @Autowired
  public void addCorsMappers(CorsRegistry coreRegistry) {
	  coreRegistry.addMapping("/**")//拦截那些url 全部拦截
	  			   .allowedOrigins("*") //哪些放行 所有域放行
	  			   .allowCredentials(true)//是否运行发送cookie信息
	  			   .allowedMethods("GET","POST","PUT","DELETE")//放行哪些HTTP方法
	  			   .allowedHeaders("*")//用于预检请求 放行哪些header
	  			   .exposedHeaders("header1","header2");//暴露哪些头部信息
  } */
  
}
