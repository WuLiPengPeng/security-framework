package com.wlp.springshiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {


    //ShiroFilterFactoryBean
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        /*
            anon :无参，开放权限，可以理解为匿名用户或游客
            authc :无参，需要认证
            logout :无参，注销，执行后会直接跳转到shiroFilterFactoryBean.setLoginUrl(); 设置的 url
            authcBasic :无参，表示 httpBasic 认证
            user :无参，表示必须存在用户，当登入操作时不做检查
            ssl :无参，表示安全的URL请求，协议为 https
            perms[user] :参数可写多个，表示需要某个或某些权限才能通过，多个参数时写
            perms["user, admin"] :，当有多个参数时必须每个参数都通过才算通过
            roles[admin] :参数可写多个，表示是某个或某些角色才能通过，多个参数时写
            roles["admin，user"] :，当有多个参数时必须每个参数都通过才算通过
            rest[user] :根据请求的方法，相当于 perms[user:method]，其中 method 为 post，get，delete 等
            port[8081] :当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString 其中 schmal 是协议 http 或 https 等等，serverName 是你访问的 Host，8081 是 Port 端口，queryString 是你访问的 URL 里的 ? 后面的参数

            常用的主要就是 anon，authc，user，roles，perms 等

         */

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //授权，正常情况下，没有授权会跳转到未授权页面
        filterChainDefinitionMap.put("/user/add","perms[user:add]");
        filterChainDefinitionMap.put("/user/update","perms[user:update]");

        //注意：路径添加重复会报错,路径不匹配将无法拦截到请求
        filterChainDefinitionMap.put("/user/*","authc");

        //设置登录请求
        bean.setLoginUrl("/toLogin");
        //设置未授权时的页面请求
        bean.setUnauthorizedUrl("/toNoAuth");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean ;
    }


    //DefaultWebSecurityManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){

        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联Realm
        defaultWebSecurityManager.setRealm(userRealm);

        return defaultWebSecurityManager ;
    }


    //创建自定义的UserRealm
    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }


    //用来整合thymeleaf 和 shiro
    @Bean("shiroDialect")
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

}
