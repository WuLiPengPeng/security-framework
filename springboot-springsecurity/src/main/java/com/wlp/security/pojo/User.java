package com.wlp.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    //用户id
    private Integer id ;
    //用户名
    private String username;
    //密码
    private String password ;
    //账户是否过期
    private Boolean accountNonExpired;
    //账户是否锁定
    private Boolean accountNonLocked;
    //密码是否需过期
    private Boolean credentialsNonExpired;
    //账户是否启用
    private Boolean enabled ;
    //授权权限
    private List<GrantedAuthority> grantedAuthority = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}