/**
 * 
 */
package com.xms.house.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xms.house.Helper.UserHelper;
import com.xms.house.common.ResultMsg;
import com.xms.house.model.User;
import com.xms.house.service.AccountService;
import com.xms.house.service.AgencyService;
/**
 * @author xie
 *
 */
@Controller
public class UserController {
  
  @Autowired
  private AgencyService agencyService;
  
  @Autowired
  private AccountService accountService;

//----------------------------注册流程-------------------------------------------
  @RequestMapping("test")
  public String test() {
	  
	  return "/user/accounts/registerSubmit";
  }
  /**
   * 注册
   * @param account
   * @param modelMap
   * @return
   */
  @RequestMapping(value="accounts/register",method={RequestMethod.POST,RequestMethod.GET})
  public String accountsSubmit(User account,ModelMap modelMap){
    if (account == null || account.getName() == null) {
      modelMap.put("agencyList",  agencyService.getAllAgency());
      return "/user/accounts/register";
    }
    ResultMsg retMsg =  UserHelper.validate(account);
   
    if (retMsg.isSuccess() ) {
      boolean exist = accountService.isExist(account.getEmail());
      if (!exist) {
         accountService.addAccount(account);
         modelMap.put("success_email", account.getEmail());
         return "/user/accounts/registerSubmit";
      }else {
        return "redirect:/accounts/register?" + ResultMsg.errorMsg("邮箱已被占用").asUrlParams();
      }
    }else {
      return "redirect:/accounts/register?" + ResultMsg.errorMsg("参数错误").asUrlParams();
    }
  }
  
  /**
   * 用户激活
   * @param key
   * @return
   */
  @RequestMapping("accounts/verify")
  public String verify(String key){
   boolean result =  accountService.enable(key);
   if (result) {
     return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
   }else {
     return "redirect:/accounts/signin?" + ResultMsg.errorMsg("激活失败,请确认链接是否过期").asUrlParams();
   }
  }
  
  
}
