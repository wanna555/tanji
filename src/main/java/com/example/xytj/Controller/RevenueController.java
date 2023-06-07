package com.example.xytj.Controller;

import com.example.xytj.common.Result;
import com.example.xytj.mapper.RevenueMapper;
import com.example.xytj.pojo.Revenue;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title RevenueController
 * @Author: ZKY
 * @CreateTime: 2023-03-23  21:25
 * @Description: TODO
 */
@RestController
@Slf4j
@RequestMapping("/revenue")
@CrossOrigin
public class RevenueController {
    @Autowired
    private RevenueMapper revenueMapper;

    @ApiOperation("营收的数据可视化")
    @PostMapping("/check")
    public Result<Revenue> check(){
        List<Revenue> list = revenueMapper.selectList(null);
        Revenue revenue = list.get(0);
        return Result.success(revenue);
    }
}
