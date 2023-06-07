package com.example.xytj.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.xytj.Service.DistanceNumService;
import com.example.xytj.Utils.CarbonCoinUtils;
import com.example.xytj.common.Result;
import com.example.xytj.mapper.*;
import com.example.xytj.pojo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title AnalyseController
 * @Author: ZKY
 * @CreateTime: 2023-03-23  15:09
 * @Description: TODO
 */
@RestController
@Slf4j
@RequestMapping("/carbonAnalyse")
@CrossOrigin
public class AnalyseController {

    @Autowired
    private ProvinceNumMapper provinceNumMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private TimeNumMapper timeNumMapper;

    @Autowired
    private DistanceNumMapper distanceNumMapper;

    @Autowired
    private RevenueMapper revenueMapper;

    @Autowired
    private UserCarbonCoinMapper userCarbonCoinMapper;

    @Autowired
    private DistanceNumService distanceNumService;

    @ApiOperation("进行省份骑行信息的数据可视化")
    @PostMapping("/Province")
    public Result<List<ProvinceNum>> checkProvince(){
        List<ProvinceNum> list = provinceNumMapper.selectList(null);
        return Result.success(list);
    }

    @ApiOperation("进行成都市各地区骑行信息的数据可视化")
    @PostMapping("/ChenDu")
    public Result<List<City>> checkChenDu(){
        List<City> list = cityMapper.selectList(null);
        return Result.success(list);
    }

    @ApiOperation("分析各个时间段的订单数")
    @PostMapping("/TimeNum")
    public Result<List<TimeNum>> checkTimeNum(){
        List<TimeNum> list = timeNumMapper.selectList(null);
        return Result.success(list);
    }

    @ApiOperation("分析7天内的营收情况")
    @PostMapping("/Revenue")
    public Result<List<Revenue>> checkRevenue(){
        LambdaQueryWrapper<Revenue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Revenue::getId);
        queryWrapper.last("limit 0,7");
        List<Revenue> list = revenueMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @ApiOperation("分析7天内的骑行里程数据")
    @PostMapping("/DistanceNum")
    public Result<List<DistanceNum>> checkDistanceNum(){
        LambdaQueryWrapper<DistanceNum> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(DistanceNum::getId);
        queryWrapper.last("limit 0,7");
        List<DistanceNum> list = distanceNumMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @ApiOperation("展示碳积分最多的前七位用户")
    @PostMapping("/UserCarbonCoin")
    public Result<List<UserCarbonCoin>> checkUserCarbonCoin(){
        LambdaQueryWrapper<UserCarbonCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserCarbonCoin::getCarbonCoin);
        queryWrapper.last("limit 0,7");
        List<UserCarbonCoin> list = userCarbonCoinMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @ApiOperation("展示每公里基本碳积分")
    @PostMapping("/CarbonCoin")
    public Result<Double> checkCarbonCoin(){
        //1km
        List<Double> list = CarbonCoinUtils.BaseCarbonCoin(1.0);
        log.info(list.toString());
        double coin = list.get(1);
        String c = String.format("%.1f",coin);
        coin = Double.parseDouble(c);
        log.info(String.valueOf(coin));
        return Result.success(coin);
    }

    @ApiOperation("展示当前时间段实时增益碳积分")
    @PostMapping("/GenerateCarbonCoin")
    public Result<Double> checkGenerateCarbonCoin(){
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setStartTime(LocalDateTime.now());
        orderDetails.setStartLatitude(22.0);
        orderDetails.setStartLongitude(113.0);
        orderDetails.setEndLatitude(23.0);
        orderDetails.setEndLongitude(113.0);
        double a = CarbonCoinUtils.generateCarbonCoin(orderDetails);
        a = Double.parseDouble(String.format("%.2f",a))/111;
        return Result.success(a);
    }

    @ApiOperation("展示近七天节省的总碳排放")
    @PostMapping("/CarbonDioxide")
    public Result<Double> checkCarbonDioxide(){
        QueryWrapper<DistanceNum> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("distance",0);
        queryWrapper.last("limit 0,7");
        queryWrapper.select("sum(distance) as distance");
        DistanceNum distanceNum = distanceNumService.getOne(queryWrapper);
        double total = distanceNum.getDistance();
        return Result.success(CarbonCoinUtils.BaseCarbonCoin(total).get(0));
    }

}
