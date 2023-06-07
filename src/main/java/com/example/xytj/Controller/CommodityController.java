package com.example.xytj.Controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xytj.Service.*;
import com.example.xytj.common.Result;
import com.example.xytj.mapper.CommodityMapper;
import com.example.xytj.pojo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @title CommodityController
 * @Author: ZKY
 * @CreateTime: 2023-03-13  11:55
 * @Description: TODO
 */
@RestController
@Slf4j
@RequestMapping("/commodity")
@CrossOrigin
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    @Autowired
    private RedisTemplate<Object,User> template;

    @Autowired
    private CommodityExchangeService commodityExchangeService;

    @Autowired
    private UserCarbonCoinService userCarbonCoinService;

    @Autowired
    private HistoryInfoService historyInfoService;

    @ApiOperation("新增商品")
    @PostMapping("/save")
    public Result<String> save(@RequestBody Commodity commodity){
        log.info(commodity.toString());
        commodityService.save(commodity);
        return Result.success("新增商品成功");
    }


    @ApiOperation("商品信息的分页查询")
    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Commodity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name),Commodity::getName,name);

        queryWrapper.orderByDesc(Commodity::getUpdateTime);

        commodityService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    @ApiOperation("根据id查询商品信息")
    @GetMapping("/{id}")
    public Result<Commodity> getById(@PathVariable Long id){
        Commodity commodity = commodityService.getById(id);
        if (commodity != null){
            return Result.success(commodity);
        }
        return Result.error("没有查询到相关信息");
    }

    @ApiOperation("根据id修改商品信息")
    @PutMapping("/update")
    public Result<String> update(@RequestBody Commodity commodity){
        commodityService.updateById(commodity);
        return Result.success("信息修改成功");
    }

    @ApiOperation("商品兑换")
    @PostMapping("/exchange")
    @Transactional
    public Result<String> exchange(Long id, HttpServletRequest request){
        String token  = request.getHeader("Authorization");
        User user = template.opsForValue().get(token);
        log.info(user.toString());
        Commodity commodity = new Commodity();
        commodity = commodityService.getById(id);
        UserCarbonCoin userCarbonCoin = userCarbonCoinService.getById(user.getId());
        log.info(userCarbonCoin.toString());
        if (userCarbonCoin.getCarbonCoin() <= commodity.getCarbonIntegral() ){
            return Result.error("碳积分不足，兑换失败，请继续努力吧");
        } else {
            userCarbonCoin.setCarbonCoin(userCarbonCoin.getCarbonCoin() - commodity.getCarbonIntegral());
            userCarbonCoinService.updateById(userCarbonCoin);
        }
        commodity = commodityService.getById(id);

        CommodityExchange commodityExchange = new CommodityExchange();
        commodityExchange.setCommodityId(commodity.getId());
        commodityExchange.setUserId(user.getId());
        commodityExchange.setInfo(commodity.getName()+" 使用了"+commodity.getCarbonIntegral()+"个碳积分");
        commodityExchange.setPicUrl(commodity.getImage());
        commodityExchange.setPrice(commodity.getCarbonIntegral());
        commodityExchangeService.save(commodityExchange);

        JSONObject jsonObject = FabricService.update(userCarbonCoin,0.0);
        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setId((String) jsonObject.get("txid"));
        historyInfo.setCreateTime(LocalDateTime.now());
        historyInfo.setUserId(user.getId());
        historyInfo.setInfo("使用"+commodity.getCarbonIntegral()+"个碳积分，兑换商品："+commodity.getName());
        historyInfoService.save(historyInfo);

        return Result.success("兑换成功");
    }
}
