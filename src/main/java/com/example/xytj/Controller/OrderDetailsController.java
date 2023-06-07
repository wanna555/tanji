package com.example.xytj.Controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xytj.Service.*;
import com.example.xytj.Utils.AreaUtils;
import com.example.xytj.Utils.CarbonCoinUtils;
import com.example.xytj.Utils.JwtTokenUtils;
import com.example.xytj.Utils.SplitTimeUtils;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @title OrderDetailsController
 * @Author: ZKY
 * @CreateTime: 2023-03-15  21:46
 * @Description: TODO
 */
@RestController
@RequestMapping("/orderdetails")
@Slf4j
@CrossOrigin
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private ProvinceNumService provinceNumService;

    @Autowired
    private CityService cityService;

    @Autowired
    private TimeNumService timeNumService;

    @Autowired
    private DistanceNumService distanceNumService;

    @Autowired
    private UserCarbonCoinService userCarbonCoinService;

    @Autowired
    private HistoryInfoService historyInfoService;

    @Autowired
    private RedisTemplate<Object,User> template;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("订单开始，将用户id，订单id，开始时间，开始时经纬度存入数据库")
    @PostMapping("/start")
    public Result<String> start(@RequestBody OrderDetails orderDetails, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Long userId = template.opsForValue().get(token).getId();
        log.info(orderDetails.toString());
        orderDetails.setUserId(userId);
        orderDetails.setStartTime(LocalDateTime.now());
        orderDetailsService.save(orderDetails);


        String orderId = orderDetails.getId().toString();
        redisTemplate.opsForValue().set("orderDetails"+userId,orderId, Duration.ofDays(1L));

        //2023-03-23T15:05:48
        String time = String.valueOf(orderDetails.getStartTime());
        //String time = "2023-03-23T15:05:48";
        log.info(time);
        String[] strings = new String[2];
        strings = time.split("T");
        String[] strings1 = new String[3];
        strings1 = strings[1].split(":");
        int hour = Integer.parseInt(strings1[0]);
        log.info(Integer.toString(hour));
        TimeNum timeNum = new TimeNum();

        for (int i = 0; i < 12; i++) {

            if (hour >= i*2 && hour < (i*2+2)){
                String id = i*2 + "-" + (i*2+2);
                timeNum.setId(id);
                timeNum = timeNumService.getById(id);
                timeNum.setNum(timeNum.getNum() + 1);
                break;
            }
        }
        timeNumService.updateById(timeNum);


        return Result.success("骑行开始");
    }

    @ApiOperation("订单结束，将结束时间、经纬度存入数据库中,并更行相应的表")
    @PostMapping("/end")
    public Result<String> end(@RequestBody OrderDetails orderDetails,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Claims claims = JwtTokenUtils.JwtTokenParse(token);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid,claims.get("openid"));
        User user = userService.getOne(queryWrapper);

        log.info(orderDetails.toString());
        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1 = orderDetailsService.getById(redisTemplate.opsForValue().get("orderDetails"+user.getId()));
        log.info(orderDetails1.toString());
        orderDetails1.setEndTime(orderDetails.getEndTime());
        orderDetails1.setEndLongitude(orderDetails.getEndLongitude());
        orderDetails1.setEndLatitude(orderDetails.getEndLatitude());
        log.info(orderDetails1.toString());

        orderDetails1.setDistance(orderDetails.getDistance()/1000);
        log.info("本次骑行距离="+orderDetails1.getDistance().toString() + "KM");
        orderDetails1.setEndTime(LocalDateTime.now());
        orderDetailsService.updateById(orderDetails1);

        String lng = String.valueOf(orderDetails1.getEndLongitude());
        String lat = String.valueOf(orderDetails1.getEndLatitude());
        Map map = AreaUtils.findArea(lat,lng);
        String province = (String) map.get("province");
        String city = (String) map.get("city");
        if (city.equals("成都市")){
            String district = (String) map.get("district");
            log.info(district);
            City city1 = cityService.getById(district);
            city1.setNum(city1.getNum() + 1);
            cityService.updateById(city1);
        }
        //String district = AreaUtils.findDistrict(lng,lat);
        ProvinceNum provinceNum = provinceNumService.getById(province);
        provinceNum.setNum(provinceNum.getNum() + 1);
        provinceNumService.updateById(provinceNum);

        /*String time = "2023-03-23T15:05:48";*/
        String time = orderDetails1.getEndTime().toString();
//        String time = "2023-03-28";
        String date = SplitTimeUtils.split(time).get("YMD");
//        String[] strings = time.split("T");
//        String date = strings[0];
        DistanceNum distanceNum = distanceNumService.getById(date);
        if (distanceNum == null){
            distanceNum = new DistanceNum();
            distanceNum.setId(LocalDate.parse(date));
            distanceNum.setDistance(orderDetails1.getDistance());
            distanceNumService.save(distanceNum);
        }else {
            distanceNum.setDistance(distanceNum.getDistance() + orderDetails1.getDistance());
            distanceNumService.updateById(distanceNum);
        }

        UserCarbonCoin ucc = userCarbonCoinService.getById(user.getId());
        double generate = CarbonCoinUtils.generateCarbonCoin(orderDetails1);
        double base = CarbonCoinUtils.BaseCarbonCoin(orderDetails1.getDistance()).get(1);
        log.info("generate={},base={}",generate,base);
        double nowCoin = generate + base;
        log.info(String.valueOf(CarbonCoinUtils.generateCarbonCoin(orderDetails1)));
        log.info(String.valueOf(CarbonCoinUtils.BaseCarbonCoin(orderDetails1.getDistance()).get(1)));

        ucc.setCarbonCoin(ucc.getCarbonCoin() + nowCoin);
        userCarbonCoinService.updateById(ucc);
        JSONObject jsonObject1 = FabricService.update(ucc,0.0);
        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setId((String) jsonObject1.get("txid"));
        historyInfo.setCreateTime(LocalDateTime.now());
        historyInfo.setUserId(user.getId());
        historyInfo.setInfo("骑行距离为"+orderDetails1.getDistance()+"，本次骑行碳积分为"+nowCoin);
        historyInfoService.save(historyInfo);


        return Result.success("骑行结束");
    }



    @ApiOperation("订单的分页查询")
    @GetMapping("/page")
    public Result<Page<OrderDetails>> page(int page, int pageSize){
        log.info("page={},pageSize={}",page,pageSize);
        Page<OrderDetails> Info = new Page<OrderDetails>(page,pageSize);

        LambdaQueryWrapper<OrderDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(OrderDetails::getStartTime);
        orderDetailsService.page(Info,queryWrapper);
        return Result.success(Info);
    }



    @ApiOperation("根据订单id查询订单信息")
    @GetMapping("/{id}")
    public Result<OrderDetails> getById(@PathVariable Long id){
        OrderDetails orderDetails = orderDetailsService.getById(id);
        if (orderDetails != null){
            return Result.success(orderDetails);
        }
        return Result.error("没有查询到相关信息");
    }

}
