package com.wlp.security.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证成功处理器
 *
 * 当通过数据库获取用户信息，通过认证，将会调用此类的方法
 *
 *
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("认证成功处理器=======================================");
        //去主页
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index");
        HttpSession session = request.getSession();
        session.removeAttribute("msg");

        String requestToken = request.getParameter("requestToken");
        System.out.println(requestToken);

        /*//取出存储在Session中的token
        String requestToken = request.getParameter("toKen");
        Object sessionToken = session.getAttribute("toKen");
        if(sessionToken == null){
            session.setAttribute("sessionToken",requestToken);
        }
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //表单数据中没有token，表单重复提交
        if(requestToken == null){
            out.write("表单重复提交");
            return;
            //Session中的Token与表单提交的Token不同，表单重复提交
        }else if(!requestToken.equals(sessionToken)){
            out.write("表单重复提交");
            return;
        }*/
        requestDispatcher.forward(request,response);
    }
}
