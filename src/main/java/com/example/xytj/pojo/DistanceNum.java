package com.example.xytj.pojo;

import lombok.Data;
import java.time.LocalDate;


/**
 * @title DistanceNum
 * @Author: ZKY
 * @CreateTime: 2023-03-27  11:16
 * @Description: TODO
 */
@Data
public class DistanceNum {
    private static final long serialVersionUID = 1L;

    LocalDate id;

    Double distance;
}
