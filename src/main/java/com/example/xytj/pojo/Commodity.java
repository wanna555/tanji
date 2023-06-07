package com.example.xytj.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title commodity
 * @Author: ZKY
 * @CreateTime: 2023-03-13  11:36
 * @Description: TODO
 */
@Data
public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    private String name;

    //兑换所需要的碳积分
    private double carbonIntegral;


    private BigDecimal price;


    //图片
    private String image;


    //描述信息
    private String description;


    //0 停售 1 起售
    private Integer status;



    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //是否删除
    private Integer isDeleted;

}