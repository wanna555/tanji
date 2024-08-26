package com.example.xytj.Controller;


import com.example.xytj.Utils.QRCodeUtil;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.User;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;


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
    public Result<String> generate(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            BufferedImage image = QRCodeUtil.getBufferedImage(token);
            try {
                ImageIO.write(image,"png",os);
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (Exception e){
            return Result.error("出现错误");
        }
        String base64 = "data:image/png;base64,";
        String encode = Base64.getEncoder().encodeToString(os.toByteArray());
        return Result.success(base64 + encode);
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
