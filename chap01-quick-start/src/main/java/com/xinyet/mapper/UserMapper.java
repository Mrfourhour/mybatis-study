package com.xinyet.mapper;

import com.xinyet.pojo.User;

import java.util.List;

public interface UserMapper {
    /**
     * 获取所有的用户
     *
     * @return
     */
    List<User> getAllUser();
}
