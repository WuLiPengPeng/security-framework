package com.wlp.springshiro.service;


import com.wlp.springshiro.pojo.User;

public interface UserService {

    User findUserByUsername(String username);
}
