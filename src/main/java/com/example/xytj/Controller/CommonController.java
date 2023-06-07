package com.example.xytj.Controller;

import com.example.xytj.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @title CommonController
 * @Author: ZKY
 * @CreateTime: 2023-03-09  21:16
 * @Description: 进行文件的上传与下载
 */
@RequestMapping("/common")
@RestController
@Slf4j
@CrossOrigin
public class CommonController {
    @Value("${XYTJ.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Result.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


















