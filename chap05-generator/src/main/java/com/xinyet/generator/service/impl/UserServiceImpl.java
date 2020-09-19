package com.xinyet.generator.service.impl;

import com.xinyet.generator.entity.User;
import com.xinyet.generator.mapper.UserMapper;
import com.xinyet.generator.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author msw
 * @since 2020-09-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
