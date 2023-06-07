package com.example.xytj.Controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @title HelloController
 * @Author: ZKY
 * @CreateTime: 2023-05-25  19:51
 * @Description: TODO
 */
@RestController
@Slf4j
@RequestMapping("/hello")
@CrossOrigin
public class HelloController {
    @GetMapping
    public void hello(){
        System.out.println("hello world");
    }
}
