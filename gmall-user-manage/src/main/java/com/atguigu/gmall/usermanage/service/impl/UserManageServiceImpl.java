package com.atguigu.gmall.usermanage.service.impl;



import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserManageService;
import com.atguigu.gmall.usermanage.mapper.UserAddressMapper;
import com.atguigu.gmall.usermanage.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


import java.util.List;

/**
 * @param
 * @return
 */

@Service
public class UserManageServiceImpl implements UserManageService {



    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserAddressMapper userAddressMapper;


    //查询所有
    public List<UserInfo> getUserInfoList(UserInfo userInfoQuery){
        List<UserInfo> userInfos=null;
        //查询所有
        //userInfos = userInfoMapper.selectAll();
        //条件匹配查询
        //userInfos =userInfoMapper.select(userInfoQuery);
        //特殊条件匹配查询 比如：按姓氏匹配
          Example example=new Example(UserInfo.class);
          example.createCriteria().andLike("loginName","%"+userInfoQuery.getLoginName()+"%");
          userInfos = userInfoMapper.selectByExample(example);
        return userInfos;

    }

    //查询单表
    public UserInfo getUserInfo(UserInfo userInfoQuery){
        UserInfo userInfo=null;
        //按主键查找
        // userInfo = userInfoMapper.selectByPrimaryKey(userInfoQuery.getId());

        //按所有非空值查询   必须只有一行 否则报错
         userInfo = userInfoMapper.selectOne(userInfoQuery );

          return userInfo;
    }

    //增加用户
    public void addUserInfo(UserInfo userInfo){
        //会覆盖数据默认值
        userInfoMapper.insert(userInfo);

       // 不会覆盖数据库默认值
        userInfoMapper.insertSelective(userInfo);
    }


    public void updateUserInfo(UserInfo userInfo){
        //修改用户  依靠主键去查询 ，然后更新其他值，如果某个值为空，那么原值被清空
 //       userInfoMapper.updateByPrimaryKey(userInfo);
        //修改用户  依靠主键去查询 ，然后更新其他不为空的值.
 //        userInfoMapper.updateByPrimaryKeySelective(userInfo);

        //修改用户  依靠自定义条件去修改
        Example example=new Example(UserInfo.class);
        example.createCriteria().andLike("loginName","%"+userInfo.getLoginName()+"%");
        userInfo.setLoginName(null);
//        userInfoMapper.updateByExample( userInfo,example );
        userInfoMapper.updateByExampleSelective( userInfo,example );
        //
    }

    public void delete(UserInfo userInfoQuery){
        userInfoMapper.deleteByPrimaryKey(userInfoQuery.getId());

        //按非空值匹配删除
      //  userInfoMapper.delete(userInfoQuery);
        //按条件匹配删除
       //  userInfoMapper.deleteByExample(new Example(UserInfo.class));
    }


    public List<UserAddress> getUserAddressList(String userId) {
        List<UserAddress> addressList = null;
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        addressList = userAddressMapper.select(userAddress);
        return addressList;
    }


}
