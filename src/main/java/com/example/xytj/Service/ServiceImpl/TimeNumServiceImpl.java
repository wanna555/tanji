package com.example.xytj.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xytj.Service.TimeNumService;
import com.example.xytj.mapper.TimeNumMapper;
import com.example.xytj.pojo.TimeNum;
import org.springframework.stereotype.Service;

/**
 * @title TimeNumServiceImpl
 * @Author: ZKY
 * @CreateTime: 2023-03-25  11:51
 * @Description: TODO
 */
@Service
public class TimeNumServiceImpl extends ServiceImpl<TimeNumMapper, TimeNum> implements TimeNumService {
}
