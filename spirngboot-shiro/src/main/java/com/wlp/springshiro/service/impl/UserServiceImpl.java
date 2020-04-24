package com.wlp.springshiro.service.impl;

import com.wlp.springshiro.dao.UserDao;
import com.wlp.springshiro.pojo.User;
import com.wlp.springshiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao ;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
