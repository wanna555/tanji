package com.example.xytj.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xytj.Service.UserService;
import com.example.xytj.mapper.UserMapper;
import com.example.xytj.pojo.User;
import org.springframework.stereotype.Service;

/**
 * @title UserServiceImpl
 * @Author: ZKY
 * @CreateTime: 2023-03-07  11:36
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
