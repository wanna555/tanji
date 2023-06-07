package com.example.xytj.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xytj.Service.CommodityExchangeService;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.CommodityExchange;
import com.example.xytj.pojo.User;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @title CommodityExchangeController
 * @Author: ZKY
 * @CreateTime: 2023-05-03  17:06
 * @Description: TODO
 */
@RestController
@Slf4j
@RequestMapping("/CommodityExchangeHistory")
@CrossOrigin
public class CommodityExchangeController {
    @Autowired
    CommodityExchangeService commodityExchangeService;

    @Autowired
    RedisTemplate<Object, User> template;

    @ApiOperation("商品兑换历史记录查询")
    @GetMapping("/getHistoryInfo")
    public Result<Page> page(int page, int pageSize, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = template.opsForValue().get(token);

        Page Info = new Page(page,pageSize);

        LambdaQueryWrapper<CommodityExchange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommodityExchange::getUserId,user.getId());
        queryWrapper.orderByDesc(CommodityExchange::getCreateTime);

        commodityExchangeService.page(Info,queryWrapper);
        return Result.success(Info);
    }
}
