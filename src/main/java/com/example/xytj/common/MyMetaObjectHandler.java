package com.example.xytj.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @title MyMetaObjectHandler
 * @Author: ZKY
 * @CreateTime: 2023-03-01  21:06
 * @Description: 自定义元数据对象处理器，完成公共字段的自动填充
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /*
    *插入操作时自动填充
    * */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    /*
    * 更新操作时自动填充
    * */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());
    }
}
