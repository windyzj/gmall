package com.atguigu.gmall.usermanage.controller;


import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserManageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @param
 * @return
 */

@RestController
public class UserManageController {


    UserManageService userManageService;

    @RequestMapping("/users")
    public ResponseEntity<List<UserInfo>> getUserList(UserInfo userInfo){
        List<UserInfo> userInfoList = userManageService.getUserInfoList(userInfo);
        return ResponseEntity.ok().body(userInfoList);
    }


    @RequestMapping(value = "/user" ,method = RequestMethod.POST)
    public    ResponseEntity<Void> add(UserInfo userInfo){ ;

        userManageService.addUserInfo(userInfo);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user" ,method = RequestMethod.PUT)
    public    ResponseEntity<Void> update(UserInfo userInfo){
        userManageService.updateUserInfo(userInfo);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user" ,method = RequestMethod.DELETE)
    public    ResponseEntity<Void> delete(UserInfo userInfo){
        userManageService.delete(userInfo);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user" ,method = RequestMethod.GET)
    public    ResponseEntity<UserInfo> getUserInfo(UserInfo userInfoQuery){
        UserInfo userInfo = userManageService.getUserInfo(userInfoQuery);
        return ResponseEntity.ok().body(userInfo);

    }




}
