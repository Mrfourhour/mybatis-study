package com.xinyet.crud.mapper;


import com.xinyet.crud.pojo.User;

import java.util.List;

public interface UserMapper {
    /**
     * 获取所有的用户
     *
     * @return
     */
    List<User> getAllUser();

    /**
     * 新增用户
     *
     * @param user
     */
    void insertUser(User user);

    /**
     * 删除用户
     *
     * @param id
     */
    void deleteUserById(Integer id);

    /**
     * 修改用户
     *
     * @param user
     */
    void updateUser(User user);
}
