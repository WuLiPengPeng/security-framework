package com.wlp.springshiro.config;

import com.wlp.springshiro.pojo.User;
import com.wlp.springshiro.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService ;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权 doGetAuthorizationInfo");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        //这个subject.getPrincipal()获取到的对象就是认证中的SimpleAuthenticationInfo传入的principal
        User currentUser = (User)subject.getPrincipal();
        //根据查询到的权限数据进行权限添加
        authorizationInfo.addStringPermission(currentUser.getPerm());
        return authorizationInfo;
    }

    //先走认证，再走的授权
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证 doGetAuthenticationInfo");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        User user = userService.findUserByUsername(username);
        //查询不到用户
        if(user ==null) return null;

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("loginUser",user);

        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPwd(),"");
        return authenticationInfo;
    }
}
