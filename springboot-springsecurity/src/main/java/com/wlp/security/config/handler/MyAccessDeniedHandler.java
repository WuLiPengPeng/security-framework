package com.wlp.security.config.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
   关于Spring Security 中的异常处理

   自定义配置类入口 WebSecurityConfigurerAdapter 一文中提到 HttpSecurity 提供的 exceptionHandling() 方法用来提供异常处理。
   该方法构造出 ExceptionHandlingConfigurer 异常处理配置类。该配置类提供了两个实用接口：

    AuthenticationEntryPoint 该类用来统一处理 AuthenticationException 异常
    AccessDeniedHandler 该类用来统一处理 AccessDeniedException 异常

   401 未授权状态
    未授权(Unauthorized) 一般来说该错误消息表明您首先需要登录（输入有效的用户名和密码）。
    如果你刚刚输入这些信息，立刻就看到一个 401 错误，就意味着，无论出于何种原因您的用户名和密码其中之一或两者都无效（输入有误，用户名暂时停用，账户被锁定，凭证失效等） 。
    总之就是认证失败了。其实正好对应我们上面的 AuthenticationException 。

   403 被拒绝状态
    被禁止(Forbidden) 出现该错误表明您在访问受限资源时没有得到许可。
    服务器理解了本次请求但是拒绝执行该任务，该请求不该重发给服务器。
    并且服务器想让客户端知道为什么没有权限访问特定的资源，服务器应该在返回的信息中描述拒绝的理由。
    一般实践中我们会比较模糊的表明原因。 该错误对应了我们上面的 AccessDeniedException 。
 */


/**
 * 此类用来统一处理AccessDeniedException异常
 *
 * 未授权
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("未授权====================================");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/toNoAuth");
        requestDispatcher.forward(request,response);
    }
}
