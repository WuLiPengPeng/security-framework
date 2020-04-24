package com.wlp.springshiro.dao;

import com.wlp.springshiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {
    //根据用户名查询用户
    User findUserByUsername(String username);
}
