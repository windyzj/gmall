package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;

import java.util.List;

/**
 * @param
 * @return
 */
public interface UserManageService {



    public List<UserInfo> getUserInfoList(UserInfo userInfoQuery);

    public UserInfo getUserInfo(UserInfo userInfoQuery);

    public void delete(UserInfo userInfoQuery);

    public void addUserInfo(UserInfo userInfo);

    public void updateUserInfo(UserInfo userInfo);

    public List<UserAddress> getUserAddressList(String userId);



}
