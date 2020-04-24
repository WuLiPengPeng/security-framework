package com.wlp.security.config.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * 当通过数据库获取用户信息，未通过认证，将会调用此类的方法
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("认证失败处理器==========================================");
        //去登陆页面
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/toLogin");
        request.getSession().setAttribute("msg","用户名或密码错误");
        requestDispatcher.forward(request,response);
    }
}
