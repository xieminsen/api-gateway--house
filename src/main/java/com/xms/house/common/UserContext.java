package com.xms.house.common;

import com.xms.house.model.User;

public class UserContext {
  private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();
  
  public static void setUser(User user){
    USER_HOLDER.set(user);
  }
  
  public static void remove() {
    USER_HOLDER.remove();
  }
  
  public static User getUser() {
    return USER_HOLDER.get();
  }

}
