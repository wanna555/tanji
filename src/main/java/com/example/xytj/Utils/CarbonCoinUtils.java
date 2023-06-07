package com.example.xytj.Utils;

import com.example.xytj.pojo.OrderDetails;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @title CarbonCoin
 * @Author: ZKY
 * @CreateTime: 2023-03-27  19:17
 * @Description: TODO
 */
@Slf4j
public class CarbonCoinUtils {

    //传入骑行距离（m）
    public static List<Double> BaseCarbonCoin(double distance){
        String line = null;
        List<Double> list = new ArrayList<>();
        double a = 0;
        double b = 0;
        try{
//            String[] args = new String[] { "E:\\ADT\\python\\python3.10.4\\python.exe", "E:\\Projetc\\PyCharmProject\\demo\\main.py"};
            String[] args = new String[]{"/usr/bin/python","/home/xytj/python/demo/main.py"};
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while((line = in.readLine())!= null){
                list.add(Double.valueOf(line));
            }
            System.out.println(list);
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Double> value = new ArrayList<>();
        //二氧化碳减排量/g
        value.add(list.get(0)*distance);
        //相应社会价值 1元 = 3碳积分
        value.add(list.get(1)*distance/3.0);
        log.info(list +"   "+ value);
        return value;
    }

    public static double generateCarbonCoin(OrderDetails orderDetails){
        Map<String,String> map = SplitTimeUtils.split(orderDetails.getStartTime().toString());
        String a = String.format("START_TIME:%s,START_LAT:%s,START_LNG:%s,END_LAT:%s,END_LNG:%s",map.get("hour"),
                orderDetails.getStartLatitude(),orderDetails.getStartLongitude(),orderDetails.getEndLatitude(),orderDetails.getEndLongitude());
        String line = null;
        double coin = 0;
        log.info(a);
        try {
//            String[] args1 = new String[] { "E:\\ADT\\python\\python3.10.4\\python.exe", "E:\\Projetc\\PyCharmProject\\XYTJ\\api.py",a};
            String[] args1 = new String[] { "/usr/bin/python", "/home/xytj/python/XYTJ/api.py",a};
            Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((line = in.readLine()) != null) {
                coin = Double.valueOf(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return coin;
    }
}
