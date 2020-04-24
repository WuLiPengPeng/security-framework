package com.wlp.security.service.impl;

import com.wlp.security.dao.UserDao;
import com.wlp.security.pojo.User;
import com.wlp.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao ;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public List<String> selectGrantedAuthorityByUsername(String username) {
        return userDao.selectGrantedAuthorityByUsername(username);
    }

    @Override
    public Map<String, String> selectPermsRoleByUsername(String username) {
        return userDao.selectPermsRoleByUsername(username);
    }

}
