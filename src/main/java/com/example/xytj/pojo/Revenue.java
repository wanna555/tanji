package com.example.xytj.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @title Revenue
 * @Author: ZKY
 * @CreateTime: 2023-03-23  21:16
 * @Description: TODO
 */
@Data
public class Revenue {
    private static final long serialVersionID = 1L;

    LocalDate id;

    BigDecimal money;

    Double carbonCoin;
}
