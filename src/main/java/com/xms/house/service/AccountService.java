package com.xms.house.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.xms.house.dao.UserDao;
import com.xms.house.model.User;
import com.xms.house.utils.BeanHelper;

/**
 * 用户登录，注册，个人信息服务
 * 
 */
@Service
public class AccountService {

  @Value("${domain.name}")
  private String domainName;
  
  
  @Autowired
  private FileService fileService;
  
  @Autowired
  private UserDao userDao;
  
  public User getUserById(Long id){
    User queryUser = new User();
    queryUser.setId(id);
    List<User> users =  getUserByQuery(queryUser);
    if (!users.isEmpty()) {
      return users.get(0);
    }
    return null;
  }
  
  /**
   * 获取用户列表
   * @param query
   * @return
   */
  public List<User> getUserByQuery(User query){
    List<User> users =  userDao.getUserList(query);
    return users;
  }
  
  /**
   * 添加用户
   * @param account
   * @return
   */
  public boolean addAccount(User account){
    if (account.getAvatarFile() != null) {
        List<String> imags = fileService.getImgPaths(Lists.newArrayList(account.getAvatarFile()));
        account.setAvatar(imags.get(0));
    }
    account.setEnableUrl("http://"+domainName+"/accounts/verify");
    BeanHelper.setDefaultProp(account, User.class);//设置默认属性
    userDao.addUser(account);
    return true;
  }
  
  
  /**
   * 如果email被注册过做提示
   * @param email
   * @return
   */
  public boolean isExist(String email){
    return getUser(email) != null;
  }

  /**
   * 根据emial获取用户详情信息
   * @param email
   * @return
   */
  private User getUser(String email) {
    User queryUser = new User();
    queryUser.setEmail(email);
    List<User> users =  getUserByQuery(queryUser);
    if (!users.isEmpty()) {
       return users.get(0);
    }
    return null;
  }



  
  /**
   * 
   * @param email
   * @param key
   * @return 返回成功与失败
   */
  public boolean enable(String key){
    userDao.enable(key);
    return true;
  }
  
  

  /**
   * 调用重置通知接口
   * @param email
   */
  @Async
  public void remember(String email){
    userDao.resetNotify(email,"http://" + domainName + "/accounts/reset");
  }
  
  /**
   * 重置密码操作
   * @param email
   * @param key
   */
  public User reset(String key,String password){
    return userDao.reset(key,password);
  }
  

  public String getResetEmail(String key) {
    String email = userDao.getEmail(key);
    return email;
  }


  public User updateUser(User user){
    BeanHelper.onUpdate(user);
    return  userDao.updateUser(user);
  }

  public void logout(String token) {
    userDao.logout(token);
  }

  /**
   * 校验用户名密码并返回用户对象
   * @param username
   * @param password
   * @return
   */
  public User auth(String username, String password) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
       return null;
    }
    User user = new User();
    user.setEmail(username);
    user.setPasswd(password);
    try {
      user = userDao.authUser(user);
    } catch (Exception e) {
      return null;
    }
    return user;
  }

 
  
  
}
