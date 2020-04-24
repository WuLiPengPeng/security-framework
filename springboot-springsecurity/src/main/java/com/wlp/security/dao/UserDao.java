package com.wlp.security.dao;

import com.wlp.security.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserDao {

    //根据用户名查询用户
    User findUserByUsername(String username);

    //根据用户名查询用户权限信息
    List<String> selectGrantedAuthorityByUsername(String username);

    //查询用户的权限和角色
    Map<String,String> selectPermsRoleByUsername(String username);
}
