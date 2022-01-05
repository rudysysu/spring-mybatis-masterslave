package com.dy.spring.mybatis.masterslave.dao;

import com.dy.spring.mybatis.masterslave.domain.User;

public interface UserMapper {
    int insert(User user);
}