package com.wlp.security.service;

import com.wlp.security.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findUserByUsername(String username);

    List<String> selectGrantedAuthorityByUsername(String username);

    Map<String,String> selectPermsRoleByUsername(String username);
}
