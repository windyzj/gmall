package com.atguigu.gmall.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @param
 * @return
 */
@Controller
public class ManageController {

    @RequestMapping(value = "index" )
    public String index(){
        return "index";
    }
}
