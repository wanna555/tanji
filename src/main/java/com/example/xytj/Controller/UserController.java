package com.example.xytj.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xytj.Service.*;
import com.example.xytj.Utils.*;
import com.example.xytj.common.Result;
import com.example.xytj.form.LogInForm;
import com.example.xytj.pojo.*;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @title UserController
 * @Author: ZKY
 * @CreateTime: 2023-03-07  17:27
 */
@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserCarbonCoinService userCarbonCoinService;

    @Autowired
    private HistoryInfoService historyInfoService;

    @Autowired
    private RedisTemplate<Object,User> redisTemplate;

    @Autowired
    private CommodityExchangeService commodityExchangeService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @ApiOperation("根据用户id修改用户信息(通过修改状态位来封禁该用户)")
    @PutMapping("/update")
    public Result<String> update(@RequestBody User user){
        userService.updateById(user);
        return Result.success("用户信息修改成功");
    }

    @ApiOperation("用户信息的分页查询展示")
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,String name){
        log.info("page = {}, pageSize = {}",page,pageSize);
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), User::getNickName,name);
        queryWrapper.orderByDesc(User::getUpdateTime);

        userService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }


    @ApiOperation("发送验证码")
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code={}",code);
            //调用阿里云提供的短信服务api完成发送信息
            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            //将生成的验证码保存到Session
            session.setAttribute(phone,code);
            return Result.success("短信发送成功");
        }
        return Result.error("短信发送失败");
    }

    @ApiOperation("小程序端用户登录")
    @PostMapping("/login")
    public Result<LogInForm> login(@RequestParam(value = "code",required = false)String code,
                              @RequestParam(value = "rawData",required = false) String rawData,
                              @RequestParam(value = "signature",required = false)String signature){

        JSONObject rawDataJson = JSON.parseObject(rawData);
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);

        String openid = SessionKeyOpenId.getString("openid");
//        String openid = "oiWR25NgWG9WoO7j2T1SS5EJuQDA";
        String sessionKey = SessionKeyOpenId.getString("session_key");

        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        log.info(signature);
        log.info(signature2);
        if (/*!signature.equals(signature2)*/ 1 != 1){
            return Result.error("签名校验失败");
        } else {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getOpenid,openid);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                String nickName = rawDataJson.getString("nickName");
                String avatarUrl = rawDataJson.getString("avatarUrl");

                user = new User();
                user.setOpenid(openid);
                user.setAvatarUrl(avatarUrl);
                user.setNickName(nickName);
                userService.save(user);

                //首次登录，信息上链
                UserCarbonCoin userCarbonCoin = new UserCarbonCoin();
                userCarbonCoin.setId(user.getId());
                userCarbonCoin.setUserName(user.getNickName());
                userCarbonCoin.setCarbonCoin(0.0);
                JSONObject jsonObject = FabricService.addUser(userCarbonCoin);

                //生成首次登陆的溯源信息
                HistoryInfo historyInfo = new HistoryInfo();
                historyInfo.setId(jsonObject.get("txid").toString());
                historyInfo.setUserId(userCarbonCoin.getId());
                historyInfo.setInfo("用户首次登录，初始数据上链");
                historyInfo.setCreateTime(LocalDateTime.now());
                historyInfoService.save(historyInfo);
                userCarbonCoinService.save(userCarbonCoin);
            }
            LogInForm logInForm = new LogInForm();
            String jwtToken = JwtTokenUtils.generateJwtToken(user);
            redisTemplate.opsForValue().set(jwtToken,user, Duration.ofMinutes(120L));
            logInForm.setNickName(user.getNickName());
            logInForm.setPicUrl(user.getAvatarUrl());
            logInForm.setJwtToken(jwtToken);
            logInForm.setId(String.valueOf(user.getId()));
            return Result.success(logInForm);
        }
    }

    @ApiOperation("更改昵称")
    @PostMapping("/nickName")
    @Transactional
    public Result<String> updateNickName(HttpServletRequest request,String nickName){
        String token = request.getHeader("Authorization");
        User user = redisTemplate.opsForValue().get(token);
        log.info(user.getId().toString());
        user.setNickName(nickName);
        userService.updateById(user);
        UserCarbonCoin userCarbonCoin = new UserCarbonCoin();
        userCarbonCoin = userCarbonCoinService.getById(user.getId());
        userCarbonCoin.setUserName(user.getNickName());
        userCarbonCoinService.updateById(userCarbonCoin);

//        JSONObject jsonObject = FabricService.update(userCarbonCoin,0.0);
//        HistoryInfo historyInfo = new HistoryInfo();
//        historyInfo.setUserId(user.getId());
//        historyInfo.setId((String) jsonObject.get("txid"));
//        historyInfo.setInfo("修改用户名");
//        historyInfoService.save(historyInfo);
        return Result.success("修改成功");

    }

    @ApiOperation("获取该用户剩余碳积分")
    @PostMapping("/restCoin")
    public Result<String> getRestCoinNum(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = redisTemplate.opsForValue().get(token);
        String restCoin = String.valueOf(userCarbonCoinService.getById(user.getId()).getCarbonCoin());
        return Result.success(restCoin);
    }

    @ApiOperation("获取该用户已经兑换的碳积分")
    @PostMapping("/coin")
    public Result<String> getUsedCoinNum(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = redisTemplate.opsForValue().get(token);
        QueryWrapper<CommodityExchange> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.ge("price",0);
        queryWrapper.select("sum(price) as price");
        CommodityExchange commodityExchange = commodityExchangeService.getOne(queryWrapper);
        String usedCoin = String.valueOf(commodityExchange.getPrice());
        return Result.success(usedCoin);
    }

    @ApiOperation("获取用户骑行总减排量")
    @PostMapping("/carbon")
    public Result<String> getCarbon(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = redisTemplate.opsForValue().get(token);
        QueryWrapper<OrderDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.ge("distance",0);
        queryWrapper.select("sum(distance) as distance");
        OrderDetails orderDetails = orderDetailsService.getOne(queryWrapper);
        String carbon = CarbonCoinUtils.BaseCarbonCoin(orderDetails.getDistance()).get(0).toString();
        return Result.success(carbon);
    }
}

//        String code = map.get("code").toString();
//        String codeInSession = (String) session.getAttribute(phone);
//        String codeInSession = "6348";

//        if (codeInSession != null && codeInSession.equals(code)){
//            //比对成功
//            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(User::getWeChatId,);
//            User user = userService.getOne(queryWrapper);
//            if (user == null){
//                //判断当前手机号是否为新用户，如果是新用户则为其自动注册
//                user = new User();
//                user.setPhone(phone);
//                user.setUserName(phone+"@user");
//                userService.save(user);
//
//                //首次登录，信息上链
//                UserCarbonCoin userCarbonCoin = new UserCarbonCoin();
//                userCarbonCoin.setId(user.getId());
//                userCarbonCoin.setUserName(user.getUserName());
//                userCarbonCoin.setCarbonCoin(0.0);
//                JSONObject jsonObject = FabricService.addUser(userCarbonCoin);
//
//                HistoryInfo historyInfo = new HistoryInfo();
//                historyInfo.setId(jsonObject.get("txid").toString());
//                historyInfo.setUserId(userCarbonCoin.getId());
//                historyInfo.setInfo("用户首次登录，初始数据上链");
//                historyInfo.setCreateTime(LocalDateTime.now());
//                historyInfoService.save(historyInfo);
//                userCarbonCoinService.save(userCarbonCoin);
//            }
//            session.setAttribute("userId",user.getId());
//            log.info(String.valueOf(user.getId()));
//            session.setAttribute("userName",user.getUserName());
//            log.info((String) session.getAttribute("userName"));
//            return Result.success(user);
//        }
