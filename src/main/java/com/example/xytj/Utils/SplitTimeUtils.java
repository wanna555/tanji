package com.example.xytj.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @title SplitUtils
 * @Author: ZKY
 * @CreateTime: 2023-03-31  21:37
 * @Description: TODO
 */
public class SplitTimeUtils {
    public static Map<String, String> split(String line){
        String[] strings = line.split("T");
        //年月日
        String YMD = strings[0];
        //时分秒
        String HMS = strings[1];

        String[] strings1 = HMS.split(":");
        Map<String,String> map = new LinkedHashMap<>();
        map.put("YMD",YMD);
        map.put("HMS",HMS);
        map.put("hour",strings1[0]);
        map.put("minute",strings1[1]);
        map.put("second",strings1[2]);
        return map;
    }
}
