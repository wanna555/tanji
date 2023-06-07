package com.example.xytj.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @title WechatUtil
 * @Author: ZKY
 * @CreateTime: 2023-05-02  12:02
 * @Description: TODO
 */
public class WechatUtil {
    public static JSONObject getSessionKeyOrOpenId(String code){
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> requestUrlParam = new LinkedHashMap<>();
        requestUrlParam.put("appid","wx19ebb2675c40e586");
        requestUrlParam.put("secret","01494e3f4d128eca6a7db1975916ed20");
        requestUrlParam.put("js_code",code);
        requestUrlParam.put("grant_type", "authorization_code");
        JSONObject jsonObject = JSON.parseObject(HttpClientUtil.doPost(requestUrl,requestUrlParam));
        return jsonObject;
    }
}
