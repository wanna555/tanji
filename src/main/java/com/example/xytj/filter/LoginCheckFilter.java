package com.example.xytj.filter;

import com.alibaba.fastjson.JSON;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @title LoginCheckFilter
 * @Author: ZKY
 * @CreateTime: 2023-04-25  22:08
 * @Description: TODO
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    @Autowired
    RedisTemplate<Object, User> template;

    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    public boolean URLCheck(String[] urls, String uri){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, uri);
                if(match){
                    return true;
                }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String URI = request.getRequestURI();

        log.info("拦截到请求：{}",URI);


        //不需要拦截的请求路径
        String[] urls = {
                "/admin/login",
                "/admin/logout",
                "/backend/**",
                "/user/login",
                "/commodity/page",
                "/front/**"
        };

        //判断是否需要进行拦截
        boolean check = URLCheck(urls,URI);

        //不需要处理
        if(check){
            log.info("本次请求不需要处理:"+URI);
            filterChain.doFilter(request,response);
            return;
        }

        //判断后台管理员是否登录

        if (request.getSession().getAttribute("admin") != null){

            log.info("管理员ID为{}，已登录",request.getSession().getAttribute("admin"));
            filterChain.doFilter(request,response);
            return;
        }
        String token = request.getHeader("Authorization");
        //判断前台用户是否登录
        if (token != null){
            if (template.opsForValue().get(token) != null){
                filterChain.doFilter(request,response);
                return;
            }

        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }
}
