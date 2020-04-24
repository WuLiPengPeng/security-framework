package com.wlp.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {

    @RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @RequestMapping("/user/add")
    public String add(){
        return "/user/add";
    }
    @RequestMapping("/user/update")
    public String update(){
        return "/user/update";
    }
    @RequestMapping("/user/admin")
    public String admin(){
        return "/user/admin";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    //不要和springSecurity默认的登出请求冲突
//    @RequestMapping("/toLogout")
    @PostMapping("/toLogout")
    public String toLogout(){
        return "logout";
    }
    @RequestMapping("/toNoAuth")
    public String noAuth(){

        return "noAuth";
    }
}
