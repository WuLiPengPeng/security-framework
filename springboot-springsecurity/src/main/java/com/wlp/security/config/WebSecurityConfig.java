package com.wlp.security.config;

import com.wlp.security.config.filter.ValidateCodeFilter;
import com.wlp.security.config.handler.MyAccessDeniedHandler;
import com.wlp.security.config.handler.MyAuthenticationFailureHandler;
import com.wlp.security.config.handler.MyAuthenticationSuccessHandler;
import com.wlp.security.config.handler.MyLogoutSuccessHandler;
import com.wlp.security.config.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
//开启WebSecurity模式
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private MyUserDetailService myUserDetailService ;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler ;
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    private ValidateCodeFilter validateCodeFilter ;

    /**
     * logout 属性详解
     * logout-url LogoutFilter要读取的url,也就是指定spring security拦截的注销url
     * logout-success-url 用户退出后要被重定向的url
     * invalidate-session 默认为true,用户在退出后Http session失效
     * success-handler-ref 对一个LogoutSuccessHandler的引用,用来自定义退出成功后的操作
     * 这里需要注意的一点是,spring security 3.x默认的注销拦截url为/j_spring_security_logout,而4.x则默认使用/logout
     *
     *
     * spring security退出功能相关类
     * spring security实现注销功能涉及的三个核心类为LogoutFilter,LogoutHandler,LogoutSuccessHandler
     * LoginFilter是实现注销功能的过滤器,默认拦截/logout或者logout属性logout-url指定的url
     * LogoutHandler接口定义了退出登录操作的方法
     *
     *
     * spring security退出功能实现流程
     * spring security在实现注销功能时,大致流程如下
     * 1. 使得HTTP session失效(如果invalidate-session属性被设置为true);
     * 2. 清除SecurityContex(真正使得用户退出)
     * 3. 将页面重定向至logout-success-url指明的URL。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
         * 第一部分是formLogin配置段，用于配置登录验证逻辑相关的信息。如：登录页面、登录成功页面、登录请求处理路径等。
         * 第二部分是authorizeRequests配置端，用于配置资源的访问权限。
         */
//csrf().disable()
            http.formLogin()
                    .loginPage("/toLogin")//用户未登录时，访问任何资源都转跳到该路径，即登录页面
                    .loginProcessingUrl("/login")//登录表单form中action的地址，也就是处理认证请求的路径
                    .usernameParameter("username")///登录表单form中用户名输入框input的name名，不修改的话默认是username
                    .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password
                    .successHandler(myAuthenticationSuccessHandler)
                    .failureHandler(myAuthenticationFailureHandler)
                    .and()
                    .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)//授权相关异常处理
                .and()
                    .authorizeRequests()
                    .antMatchers("/","/index","/login","/toLogin","/logout","/toLogout")//不需要通过登录验证就可以被访问的资源路径
                    .permitAll()
                    .antMatchers("/user/add").hasAuthority("ADD")
                    .antMatchers("/user/update").hasAuthority("UPDATE")
                    //这里判断角色时就不需要前缀了
                    .antMatchers("/user/admin").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    //默认登出路径,不配置也可以
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(myLogoutSuccessHandler)
                    .permitAll();

        //TODO 图形验证码（尚未测试）
//        validateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        /**
         * 向HttpSecurity的Filter链上插入自定义的Filter,插入到UsernamePasswordAuthenticationFilter的位置上。
         * 插入方法有addFilterBefore,addFilterAt,addFilterAfter。这个地方需要注意使用addFilterAt并不是说能替换掉原有的Filter,
         * 事实上框架原有的Filter在启动HttpSecurity配置的过程中，都由框架完成了其一定程度上固定的配置，是不允许更改替换的。
         * 根据测试结果来看，调用addFilterAt方法插入的Filter，会在这个位置上的原有Filter之前执行。
         */


        /**
         * 关于登出时(logout)的CSRF配置问题
         * https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-csrf-configure-custom-repository
         * 登出时如果没按照正确方式配置，将会报错
         * 1、禁用CSRF（不推荐）
         *
         * 2、官方文档关于logging out 翻译如下：
         * 要求CSRF注销请求以防止伪造注销尝试是很重要的。如果启用了CSRF保护(默认)，Spring Security将只处理HTTP POST。
         * 这可以确保注销需要CSRF令牌，并且恶意用户不能强制注销您的users.LogoutFilter
         * 最简单的方法是使用表单注销。如果你真的想要一个链接，你可以使用JavaScript让链接执行一个POST(例如，可能在一个隐藏的表单上)。
         * 对于禁用了JavaScript的浏览器，您可以选择让链接将用户带到将执行POST的注销确认页面。
         * 如果你真的想在注销时使用HTTP GET，你可以这样做，但记住，这通常是不推荐的。
         * 例如，下面的Java配置将使用任何HTTP方法请求的URL执行注销:/logout
         * logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
         *
         * 3、官方推荐使用post请求注销，表单方式，添加隐藏域，一并提交到注销请求
         * <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
         *
         * 4、3、官方推荐使用post请求注销，meta元数据方式
         *
         * Ajax and JSON Requests
         * HTML中：
         * <head>
         *     <meta name="_csrf" content="4bfd1575-3ad1-4d21-96c7-4ef2d9f86721"/>
         *     <meta name="_csrf_header" content="X-CSRF-TOKEN"/>
         * </head>
         * //使用ajax发送CSRF Token
         * $(function () {
         *     var token = $("meta[name='_csrf']").attr("content");
         *     var header = $("meta[name='_csrf_header']").attr("content");
         *     $(document).ajaxSend(function(e, xhr, options) {
         *         xhr.setRequestHeader(header, token);
         *     });
         * });
         *
         * //JSP 中
         * <head>
         *     <meta name="_csrf" content="${_csrf.token}"/>
         *     <meta name="_csrf_header" content="${_csrf.headerName}"/>
         * </head>
         *
         * 详细使用见官方文档
         */
    }




    @Override
    public void configure(WebSecurity web) throws Exception {
        //将项目中静态资源路径开放出来
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //数据库认证提供者
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(@Qualifier("bCryptPasswordEncoder") BCryptPasswordEncoder encoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        //设置密码加密
        daoAuthenticationProvider.setPasswordEncoder(encoder);
        return daoAuthenticationProvider ;
    }

    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler();
    }

    @Bean
    public MyAuthenticationFailureHandler myAuthenticationFailureHandler(){
        return new MyAuthenticationFailureHandler();
    }

    @Bean
    public MyAccessDeniedHandler myAccessDeniedHandler(){
        return new MyAccessDeniedHandler();
    }

    @Bean
    public MyLogoutSuccessHandler myLogoutSuccessHandler(){
        return new MyLogoutSuccessHandler();
    }

    /*@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/
}
