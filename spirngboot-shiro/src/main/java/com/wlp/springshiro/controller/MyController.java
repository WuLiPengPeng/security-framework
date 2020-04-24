package com.wlp.springshiro.controller;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {

    @RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @RequestMapping("user/add")
    public String add(){
        return "user/add";
    }
    @RequestMapping("user/update")
    public String update(){
        return "user/update";
    }

    @RequestMapping("toLogin")
    public String toLogin(){

        return "login";
    }
    @RequestMapping("toNoAuth")
    public String noAuth(){

        return "noAuth";
    }

    @RequestMapping("login")
    public String toLogin(String username, String password, Model model){

        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
        try {
            //没有抛出异常，说明登录成功
            subject.login(usernamePasswordToken);
            return "index";
        }catch (UnknownAccountException e){//用户名不存在
            model.addAttribute("msg","用户名错误");
        }catch (IncorrectCredentialsException e){//密码不存在
            model.addAttribute("msg","密码错误");
        }
        return "login";
    }
}
