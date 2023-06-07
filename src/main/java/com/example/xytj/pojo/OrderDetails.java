package com.example.xytj.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @title OrderDetails
 * @Author: ZKY
 * @CreateTime: 2023-03-15  21:05
 * @Description: TODO
 */
@Data
public class OrderDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    //开始时经度
    private Double startLongitude;

    //开始时纬度
    private Double startLatitude;

    //结束时经度
    private Double endLongitude;

    //结束时纬度
    private Double endLatitude;

    private Double distance;


    private LocalDateTime startTime;


    private LocalDateTime endTime;
}
