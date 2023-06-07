package com.example.xytj.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xytj.Service.OrderDetailsService;
import com.example.xytj.mapper.OrderDetailsMapper;
import com.example.xytj.pojo.OrderDetails;
import org.springframework.stereotype.Service;


/**
 * @title OrderDetailsServiceImpl
 * @Author: ZKY
 * @CreateTime: 2023-03-15  21:44
 * @Description: TODO
 */
@Service
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements OrderDetailsService {
}
