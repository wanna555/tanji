package com.example.xytj.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xytj.Service.HistoryInfoService;
import com.example.xytj.common.Result;
import com.example.xytj.mapper.HistoryInfoMapper;
import com.example.xytj.pojo.HistoryInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title HistoryInfoController
 * @Author: ZKY
 * @CreateTime: 2023-05-01  16:23
 * @Description: TODO
 */
@RequestMapping("/historyInfo")
@RestController
@Slf4j
@CrossOrigin
public class HistoryInfoController {
    @Autowired
    HistoryInfoService historyInfoService;

    @Autowired
    HistoryInfoMapper historyInfoMapper;

    @ApiOperation("某个用户id的所有操作溯源分页展示")
    @GetMapping("/historyPage/{id}")
    public Result<Page> page(int page, int pageSize, @PathVariable Long id){
        log.info("page={},pageSize={}",page,pageSize);
        Page Info = new Page(page,pageSize);

        LambdaQueryWrapper<HistoryInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HistoryInfo::getUserId,id);
        List<HistoryInfo> list = historyInfoMapper.selectList(queryWrapper);
        if (list.size() == 0){
            return Result.error("未查询到该用户信息，请确认后重试");
        }
        queryWrapper.orderByDesc(HistoryInfo::getCreateTime);
        historyInfoService.page(Info,queryWrapper);
        return Result.success(Info);
    }
}
