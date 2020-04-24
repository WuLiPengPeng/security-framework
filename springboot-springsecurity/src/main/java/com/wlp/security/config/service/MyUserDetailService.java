package com.wlp.security.config.service;

import com.wlp.security.dao.UserDao;
import com.wlp.security.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("myUserDetailService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(username);

        if(null == user){
            throw new UsernameNotFoundException("user not exist");
        }
        //从数据库中查询
        //String permission = getPermissionByUsername(username);
        String permission = getPerimssionAndRoleByusername(username);
        System.out.println("拥有权限、角色："+permission);

        user.setGrantedAuthority(AuthorityUtils.commaSeparatedStringToAuthorityList(permission));
        return user;
    }

    /**
     * 根据用户名获取用户的权限
     * @param username
     * @return
     */
    private String getPermissionByUsername(String username){
        List<String> grantedAuthority = userDao.selectGrantedAuthorityByUsername(username);
        StringBuilder sbuiler = new StringBuilder();
        for(int i = 0 ; i < grantedAuthority.size() ; i++){
            sbuiler.append(grantedAuthority.get(i));
            if(i+1 < grantedAuthority.size()-1){
                sbuiler.append(",");
            }
        }
        return sbuiler.toString();
    }
    /**
     * 根据用户名获取用户的权限和角色
     *
     * @param username
     * @return
     */
    private  String getPerimssionAndRoleByusername(String username){
        Map<String, String> permsRoleMap = userDao.selectPermsRoleByUsername(username);
        StringBuilder sbuiler = new StringBuilder();
        int count = 0 ;
        //如果数据库的角色名称未添加"ROLE_"前缀，那么需要在这里添加角色信息时候进行拼接
        //返回的单个key所对应的数据已经在数据库中执行SQL使用逗号“,”拼接好了
        //角色一个字符串，如：ROLE_ADMIN,ROLE_USER
        //权限一个字符串，如：ADD,DELETE,UPDATE,LIST
        for(String value : permsRoleMap.values()){
            sbuiler.append(value);
            count++;
            if(count < permsRoleMap.size()){
                sbuiler.append(",");
            }
        }
        return  sbuiler.toString();
    }
}
