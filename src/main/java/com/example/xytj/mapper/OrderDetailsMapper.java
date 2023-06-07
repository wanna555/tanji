package com.example.xytj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xytj.pojo.OrderDetails;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.apache.ibatis.annotations.Mapper;

/**
 * @title OrderDetailsMapper
 * @Author: ZKY
 * @CreateTime: 2023-03-15  21:38
 * @Description: TODO
 */
@Mapper
public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {
}
