package com.xms.house.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Objects;
import com.xms.house.common.CommonConstants;
import com.xms.house.common.PageData;
import com.xms.house.common.PageParams;
import com.xms.house.common.ResultMsg;
import com.xms.house.common.UserContext;
import com.xms.house.model.Comment;
import com.xms.house.model.House;
import com.xms.house.model.User;
import com.xms.house.model.UserMsg;
import com.xms.house.service.AgencyService;
import com.xms.house.service.CommentService;
import com.xms.house.service.HouseService;

@Controller
public class HouseController {
  
  @Autowired
  private HouseService houseService;
  
  @Autowired
  private AgencyService agencyService;
  
  @Autowired
  private CommentService commentService;
  
  

  /**
   *  查询房产列表
   * @param pageSize
   * @param pageNum
   * @param query
   * @param modelMap
   * @return
   */
  @RequestMapping(value="house/list",method={RequestMethod.POST,RequestMethod.GET})
  public String houseList(Integer pageSize,Integer pageNum,House query,ModelMap modelMap){
    PageData<House> ps = houseService.queryHouse(query,PageParams.build(pageSize, pageNum));
    List<House> rcHouses =  houseService.getHotHouse(CommonConstants.RECOM_SIZE);
    modelMap.put("recomHouses", rcHouses);
    modelMap.put("vo", query);
    modelMap.put("ps", ps);
    return "/house/listing";
  }
  
  /**
   * 1.查询房屋详情
   * @param id
   * @param modelMap
   * @return
   */
  @RequestMapping(value="house/detail",method={RequestMethod.POST,RequestMethod.GET})
  public String houseDetail(long id,ModelMap modelMap){
    House house = houseService.queryOneHouse(id);
    List<Comment> comments = commentService.getHouseComments(id);//获取评论列表
    List<House> rcHouses =  houseService.getHotHouse(CommonConstants.RECOM_SIZE);
    if (house.getUserId() != null) {
      if (!Objects.equal(0L, house.getUserId())) {
        modelMap.put("agent", agencyService.getAgentDetail(house.getUserId()));
      }
    }
    modelMap.put("house", house);
    modelMap.put("recomHouses", rcHouses);
    modelMap.put("commentList", comments);
    return "/house/detail";
  }
  
  @RequestMapping(value="house/leaveMsg",method={RequestMethod.POST,RequestMethod.GET})
  public String houseMsg(UserMsg userMsg){
    houseService.addUserMsg(userMsg);
    return "redirect:/house/detail?id=" + userMsg.getHouseId() + "&" +ResultMsg.successMsg("留言成功").asUrlParams();
  }
  
  @ResponseBody
  @RequestMapping(value="house/rating",method={RequestMethod.POST,RequestMethod.GET})
  public ResultMsg houseRate(Double rating,Long id){
    houseService.updateRating(id,rating);
    return ResultMsg.success();
  }
  
  
  
  @RequestMapping(value="house/toAdd",method={RequestMethod.POST,RequestMethod.GET})
  public String toAdd(ModelMap modelMap){
    modelMap.put("citys", houseService.getAllCitys());
    modelMap.put("communitys", houseService.getAllCommunitys());
    return "/house/add";
  }
  
  @RequestMapping(value="house/add",method={RequestMethod.POST,RequestMethod.GET})
  public String doAdd(House house){
    User user = UserContext.getUser();
    houseService.addHouse(house,user);
    return "redirect:/house/ownlist?" +ResultMsg.successMsg("添加成功").asUrlParams();
  }
  
  @RequestMapping(value="house/ownlist",method={RequestMethod.POST,RequestMethod.GET})
  public String ownlist(House house,PageParams pageParams,ModelMap modelMap){
    User user = UserContext.getUser();
    house.setUserId(user.getId());
    house.setBookmarked(false);
    modelMap.put("ps", houseService.queryHouse(house, pageParams)) ;
    modelMap.put("pageType", "own") ;
    return "/house/ownlist";
  }
  
  @RequestMapping(value="house/del",method={RequestMethod.POST,RequestMethod.GET})
  public String delsale(Long id,String pageType){
    User user = UserContext.getUser();
    houseService.unbindUser2House(id,user.getId(),pageType.equals("own")?false:true);
    return "redirect:/house/ownlist";
  }
  
  
  @RequestMapping(value="house/bookmarked",method={RequestMethod.POST,RequestMethod.GET})
  public String bookmarked(House house,PageParams pageParams,ModelMap modelMap){
    User user = UserContext.getUser();
    house.setBookmarked(true);
    house.setUserId(user.getId());
    modelMap.put("ps", houseService.queryHouse(house, pageParams)) ;
    modelMap.put("pageType", "book") ;
    return "/house/ownlist";
  }
  
  /**
   *  1房产收藏
   * @param id
   * @param modelMap
   * @return
   */
  @RequestMapping(value="house/bookmark",method={RequestMethod.POST,RequestMethod.GET})
  @ResponseBody
  //@CrossOrigin(origins="http")//定义局部跨域请求 内部可以定义一些我们刚刚自己的规则
  public ResultMsg bookmark(Long id,ModelMap modelMap){
    User user = UserContext.getUser();
    houseService.bindUser2House(id, user.getId(), true);
    return ResultMsg.success();
  }
  
  /**
   * 1取消收藏
   * @param id
   * @param modelMap
   * @return
   */
  @RequestMapping(value="house/unbookmark",method={RequestMethod.POST,RequestMethod.GET})
  @ResponseBody
  public ResultMsg unbookmark(Long id,ModelMap modelMap){
    User user = UserContext.getUser();
    houseService.unbindUser2House(id, user.getId(), true);
    return ResultMsg.success();
  }
  
}
