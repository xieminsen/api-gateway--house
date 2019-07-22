package com.xms.house.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xms.house.common.CommonConstants;
import com.xms.house.common.PageData;
import com.xms.house.common.PageParams;
import com.xms.house.model.Blog;
import com.xms.house.model.Comment;
import com.xms.house.model.House;
import com.xms.house.service.CommentService;
import com.xms.house.service.HouseService;

/**
 * 博客Controller
 * @author xie
 *
 */
@Controller
public class BlogController {
  
  
  @Autowired
  private CommentService commentService;
  
  @Autowired
  private HouseService houseService; 
  
  /**
   * 获取博客列表
   * @param pageSize
   * @param pageNum
   * @param query
   * @param modelMap
   * @return
   */
  @RequestMapping(value="blog/list",method={RequestMethod.POST,RequestMethod.GET})
  public String list(Integer pageSize,Integer pageNum,Blog query,ModelMap modelMap){
	//获取博客列表
    PageData<Blog> ps = commentService.queryBlogs(query,PageParams.build(pageSize, pageNum));
    //获取热门房产列表
    List<House> houses =  houseService.getHotHouse(CommonConstants.RECOM_SIZE);
    modelMap.put("recomHouses", houses);
    modelMap.put("ps", ps);
    return "/blog/listing";
  }
  
  /**
   * 获取博客详情
   * @param id
   * @param modelMap
   * @return
   */
  @RequestMapping(value="blog/detail",method={RequestMethod.POST,RequestMethod.GET})
  public String blogDetail(int id,ModelMap modelMap){
    Blog blog = commentService.queryOneBlog(id);
    List<Comment> comments = commentService.getBlogComments(id);
    List<House> houses =  houseService.getHotHouse(CommonConstants.RECOM_SIZE);
    modelMap.put("recomHouses", houses);
    modelMap.put("blog", blog);
    modelMap.put("commentList", comments);
    return "/blog/detail";
  }
}
