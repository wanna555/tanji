package com.example.xytj.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.xytj.Fabric.ChaincodeManager;
import com.example.xytj.Fabric.Manager.FabricManager;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.UserCarbonCoin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @title FabricService
 * @Author: ZKY
 * @CreateTime: 2023-04-24  17:46
 * @Description: TODO
 */
@Slf4j
@Service
@Component
public class FabricService {

    private static ChaincodeManager chaincodeManager;



    static{
        try {
            chaincodeManager = FabricManager.obtain().getManager(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户首次注册时将信息上链
    public static JSONObject addUser(UserCarbonCoin userCarbonCoin){
        String[] args = {String.valueOf(userCarbonCoin.getId()),userCarbonCoin.getUserName(), String.valueOf(userCarbonCoin.getCarbonCoin())};
        JSONObject jsonObject = null;
        try {
            jsonObject = chaincodeManager.invoke("addUser",args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("信息上链失败");
            return jsonObject;
        }
        return jsonObject;
    }

    //用户根据查询自己的碳积分信息
    public static JSONObject query(String id){
        String[] args = {id};
        JSONObject jsonObject = null;
        try {
            jsonObject = chaincodeManager.invoke("queryById",args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败");
            return jsonObject;
        }
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
        return jsonObject;
    }

    //区块链溯源操作，查询历史操作记录
    public static String queryHistoryInfoById(String id){
        String[] args = {id};
        JSONObject jsonObject;
        try {
            jsonObject = chaincodeManager.invoke("queryHistoryIndoById",args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("溯源失败");
            return "溯源失败，出现错误";
        }
        String s = jsonObject.get("data").toString();
        return s;
    }

    //链上信息删除操作
    public static JSONObject delInfo(String id){
        String[] args = {id};
        JSONObject jsonObject = null;
        try{
            jsonObject = chaincodeManager.invoke("delInfo",args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("信息删除失败");
            return jsonObject;
        }
        return jsonObject;
    }


    //碳积分更新操作
    public static JSONObject update(UserCarbonCoin userCarbonCoin, double value){
        JSONObject jsonObject = null;
        double oldCoin = userCarbonCoin.getCarbonCoin();
        double newCoin = oldCoin + value;
        String[] args = {String.valueOf(userCarbonCoin.getId()), userCarbonCoin.getUserName(), String.valueOf(newCoin)};
        log.info(userCarbonCoin.getId().toString());
        try {
            jsonObject = chaincodeManager.invoke("updateCoin",args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("信息更新失败");
            return jsonObject;
        }

//        return Result.success(jsonObject.get("data").toString());
        return jsonObject;
    }
}
