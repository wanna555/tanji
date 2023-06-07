package com.example.xytj.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @title User
 * @Author: ZKY
 * @CreateTime: 2023-03-01  20:42
 * @Description: 管理员
 */
@Data
public class Admin implements Serializable {

    private static final long serialVersionID = 1L;

    private Long id;

    private String name;

    private String username;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //插入时自动填充字段
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    //插入和更新时填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;

}