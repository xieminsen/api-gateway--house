package com.xms.house.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xms.house.common.PageData;
import com.xms.house.common.PageParams;
import com.xms.house.dao.UserDao;
import com.xms.house.model.Agency;
import com.xms.house.model.ListResponse;
import com.xms.house.model.User;


@Service
public class AgencyService {
  
  @Autowired
  private UserDao userDao;
   
  
  public List<Agency> getAllAgency(){
    return userDao.getAllAgency();
  }

  public Agency getAgency(Integer id){
    return userDao.getAgencyById(id);
  }

  public void add(Agency agency) {
      userDao.addAgency(agency);
  }

  public PageData<User> getAllAgent(PageParams pageParams) {
    ListResponse<User> result =  userDao.getAgentList(pageParams.getLimit(),pageParams.getOffset());
                  Long  count  =  result.getCount();
    return PageData.<User>buildPage(result.getList(), count, pageParams.getPageSize(), pageParams.getPageNum());
  }

  
  
  
  
  
  
}
