package com.example.xytj.pojo;

import lombok.Data;

/**
 * @title UserCarbonCoin
 * @Author: ZKY
 * @CreateTime: 2023-03-27  19:42
 * @Description: TODO
 */
@Data
public class UserCarbonCoin {
    private static final long serialVersionID = 1L;

    private Long id;

    private String userName;

    private double carbonCoin;
}
