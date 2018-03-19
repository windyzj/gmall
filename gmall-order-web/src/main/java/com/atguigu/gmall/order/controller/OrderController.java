package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.service.UserManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @param
 * @return
 */

@Controller
public class OrderController {

    @Reference
    UserManageService userManageService;

    @ResponseBody
    @RequestMapping(value = "initOrder")
    public String initOrder(HttpServletRequest request){
        String userId = request.getParameter("userId");
        List<UserAddress> userAddressList = userManageService.getUserAddressList(userId);
        String jsonString = JSON.toJSONString(userAddressList);
        return  jsonString;

    }
}
