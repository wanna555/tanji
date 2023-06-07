package com.example.xytj.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xytj.Service.AdminService;
import com.example.xytj.mapper.AdminMapper;
import com.example.xytj.pojo.Admin;
import org.springframework.stereotype.Service;

/**
 * @title AdminServiceImpl
 * @Author: ZKY
 * @CreateTime: 2023-03-02  17:40
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
