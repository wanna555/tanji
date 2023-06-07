package com.example.xytj.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.xytj.Service.FabricService;
import com.example.xytj.Service.HistoryInfoService;
import com.example.xytj.Service.UserCarbonCoinService;
import com.example.xytj.common.Result;
import com.example.xytj.mapper.UserCarbonCoinMapper;
import com.example.xytj.pojo.HistoryInfo;
import com.example.xytj.pojo.User;
import com.example.xytj.pojo.UserCarbonCoin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @title FabricController
 * @Author: ZKY
 * @CreateTime: 2023-04-24  17:30
 * @Description: TODO
 */
@RestController
@Slf4j
@Api("区块链相关操作")
@RequestMapping("/fabric")
@CrossOrigin
public class FabricController {

    @Autowired
    private UserCarbonCoinService userCarbonCoinService;

    @Autowired
    private HistoryInfoService historyInfoService;

    @Autowired
    private RedisTemplate<Object,User> template;

    @ApiOperation("用户查询自己的碳积分信息")
    @GetMapping("/queryCoin")
    public Result<UserCarbonCoin> queryCoin(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = template.opsForValue().get(token);
        String id = String.valueOf(user.getId());
        log.info(id);
        JSONObject jsonObject = FabricService.query(id);
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
        Long id1 = null;
        try{
            id1 = Long.parseLong((String) jsonObject1.get("id"));
        }catch (NullPointerException e){
            e.printStackTrace();
            return Result.error("无该用户信息");
        }
        double coin = Double.parseDouble((String) jsonObject1.get("carbon_coin"));

        UserCarbonCoin userCarbonCoin = new UserCarbonCoin();
        userCarbonCoin.setId(id1);
        String name = (String) jsonObject1.get("user_name");

        userCarbonCoin.setUserName(name);
        userCarbonCoin.setCarbonCoin(coin);

        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setId((String) jsonObject.get("txid"));
        historyInfo.setUserId(user.getId());
        historyInfo.setInfo("用户查询本人碳积分信息");
        historyInfo.setCreateTime(LocalDateTime.now());
        historyInfoService.save(historyInfo);
        return Result.success(userCarbonCoin);
    }

    @ApiOperation("删除相关用户信息")
    @PostMapping("/delInfo/{id}")
    public Result<String> delInfo(@PathVariable Long id){
        JSONObject jsonObject = FabricService.delInfo(String.valueOf(id));
        String txid = (String) jsonObject.get("txid");
        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setId(txid);
        historyInfo.setUserId(id);
        historyInfo.setCreateTime(LocalDateTime.now());
        historyInfo.setInfo("删除该用户碳积分信息");
        historyInfoService.save(historyInfo);
        return Result.success(jsonObject.get("data").toString());
    }

    @ApiOperation("将有非法操作用户的碳积分置为0")
    @PutMapping("/toZero")
    public Result<String> toZero(Long id){
        UserCarbonCoin userCarbonCoin = userCarbonCoinService.getById(id);
        userCarbonCoin.setCarbonCoin(0.0);
        userCarbonCoinService.updateById(userCarbonCoin);
        JSONObject jsonObject = FabricService.update(userCarbonCoin,0);
        String txid = (String) jsonObject.get("txid");
        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setId(txid);
        historyInfo.setUserId(userCarbonCoin.getId());
        historyInfo.setCreateTime(LocalDateTime.now());
        historyInfo.setInfo("由于非法操作，用户碳积分已被清零");
        historyInfoService.save(historyInfo);
        return Result.success(jsonObject.get("data").toString());
    }

    @ApiOperation("区块链的溯源操作")
    @GetMapping("/queryHistoryInfo/{id}")
    public String queryHistoryInfo(@PathVariable Long id){
        String ids = id.toString();
        return FabricService.queryHistoryInfoById(ids);
    }
}
