package com.example.xytj.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xytj.Service.AdminService;
import com.example.xytj.Service.ServiceImpl.AdminServiceImpl;
import com.example.xytj.common.Result;
import com.example.xytj.pojo.Admin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @title AdminController
 * @Author: ZKY
 * @CreateTime: 2023-03-01  21:19
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@Api("后台管理员模块")
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService = new AdminServiceImpl() ;

    @ApiOperation("后台登录")
    @PostMapping("/login")
    public Result<Admin> login(HttpServletRequest request, @RequestBody Admin admin){
        log.info(admin.toString());
        //将页面提交的password进行md5加密
        String password = admin.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提供的username查数据库
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,admin.getUsername());
        Admin admin1 = adminService.getOne(queryWrapper);

        //如果没有该用户名则返回登陆失败结果
        if (admin1 == null){
            return Result.error("登陆错误");
        }

        //如果有该用户则比对密码，不一致则返回登陆失败结果
        if (!admin1.getPassword().equals(password)){
            return Result.error("登陆失败");
        }

        //如果该用户被封禁则不允许登录并返回相应结果
        if (admin1.getStatus() == 0){
            return Result.error("您已被封禁");
        }

        //登陆成功，将用户的id存入session并返回登陆结果
        request.getSession().setAttribute("admin",admin1.getId());
        return Result.success(admin1);
    }

    @ApiOperation("后台退出")
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("admin");
        return Result.success("退出成功");
    }

    @ApiOperation("后台添加管理员")
    @PostMapping("/save")
    public Result<String> save(@RequestBody Admin admin){
        log.info(admin.toString());
        //设置默认密码，并进行MD5加密
        admin.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));
        adminService.save(admin);
        return Result.success("新增管理员成功");
    }

    @ApiOperation("根据ID查询管理员信息")
    @GetMapping("/{id}")
    public Result<Admin> getById(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        if (admin != null){
            return Result.success(admin);
        }
        return Result.error(" 没有查询到相关信息");
    }

    @ApiOperation("根据id修改管理员的信息")
    @PutMapping("/update")
    public Result<String> update(@RequestBody Admin admin){
        log.info(admin.toString());
        if (admin.getPassword()!=null){
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
        }
        adminService.updateById(admin);
        return Result.success("信息修改成功");
    }


    @ApiOperation("进行分页查询展示")
    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize,String name){
        log.info("page = {}, pageSize = {}",page,pageSize);
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Admin::getName,name);
        queryWrapper.orderByDesc(Admin::getUpdateTime);


        adminService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

}
