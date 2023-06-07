package com.example.xytj.Utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @title AreaUtils
 * @Author: ZKY
 * @CreateTime: 2023-03-22  20:18
 * @Description: TODO
 */
public class AreaUtils {
    //传入经纬度, 返回查询的地区, lat: 纬度, lng: 经度
    public static Map findArea(String lat, String lng) {
        try {
            //移除坐标前后的 空格
            /* lat = lat.trim();
            lat = lat.trim();*/

            CloseableHttpClient httpClient = HttpClients.createDefault();
            // url中的ak值要替换成自己的:
            String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=FjvDUY1zE7UtOwfPSGaNsfoUdavnpjxO&output=json&coordtype=wgs84ll&location=" + lat + "," + lng;
            //System.out.println(url);
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = httpClient.execute(httpGet);

            HttpEntity httpEntity = response.getEntity();

            String json = EntityUtils.toString(httpEntity);

            Map<String, Object> result = JSONObject.parseObject(json, Map.class);
            if (result.get("status").equals(0)) {
                Map<String, Object> resultMap = (Map<String, Object>) result.get("result");
                resultMap = (Map<String, Object>) resultMap.get("addressComponent");
                String province = (String) resultMap.get("province");
                String city = (String) resultMap.get("city");
                String district = (String) resultMap.get("district");
                Map map = new TreeMap<>();
                map.put("province",province);
                map.put("city",city);
                map.put("district",district);
                return map;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
