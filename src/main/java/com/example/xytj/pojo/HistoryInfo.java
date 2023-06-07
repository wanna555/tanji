package com.example.xytj.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title HistoryInfo
 * @Author: ZKY
 * @CreateTime: 2023-05-01  12:56
 * @Description: TODO
 */
@Data
public class HistoryInfo {
    private static final long serialVersionUID = 1L;

    String id;

    Long userId;

    String info;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

}
