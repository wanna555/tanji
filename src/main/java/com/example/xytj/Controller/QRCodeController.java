package com.example.xytj.Controller;


import com.example.xytj.Utils.QRCodeUtil;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.User;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @title QRCodeController
 * @Author: ZKY
 * @CreateTime: 2023-06-10  20:19
 * @Description: TODO
 */
@RequestMapping("/qrCode")
@RestController
@Slf4j
public class QRCodeController {
    @Autowired
    private RedisTemplate<Object, User> redisTemplate;

    @ApiOperation("生成二维码并将其返回给前端调用者")
    @GetMapping("/generate")
    public Result<String> generate(HttpServletResponse servletResponse, HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            QRCodeUtil.createCodeToOutputStream(token,servletResponse.getOutputStream());

        }catch (Exception e){
            return Result.error("出现错误");
        }
        return Result.success("二维码生成成功");
    }

    @ApiOperation("解析二维码")
    @GetMapping("/parsing")
    public Result<String> parsing(String token){
        User user = redisTemplate.opsForValue().get(token);
        if (user == null){
            return Result.error("二维码已过期");
        }else {
            return Result.success(user.getId().toString());
        }
    }
}
