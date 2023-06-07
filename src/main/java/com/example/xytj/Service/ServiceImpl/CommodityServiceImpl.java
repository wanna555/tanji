package com.example.xytj.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xytj.Service.CommodityService;
import com.example.xytj.mapper.CommodityMapper;
import com.example.xytj.pojo.Commodity;
import org.springframework.stereotype.Service;

/**
 * @title CommodityServiceImpl
 * @Author: ZKY
 * @CreateTime: 2023-03-13  11:53
 * @Description: TODO
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {
}
