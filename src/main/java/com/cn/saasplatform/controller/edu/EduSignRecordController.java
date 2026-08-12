package com.cn.saasplatform.controller.edu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.aspect.annotation.OperateLog;
import com.cn.saasplatform.aspect.annotation.RateLimiter;
import com.cn.saasplatform.aspect.annotation.RepeatSubmit;
import com.cn.saasplatform.entity.edu.EduSignRecord;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.service.edu.IEduSignRecordService;
import com.cn.saasplatform.util.RedissonLockUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/edu/sign")
public class EduSignRecordController {

    @Resource
    private IEduSignRecordService signRecordService;

    @Resource
    private RedissonLockUtil redissonLockUtil;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('edu:sign:list')")
    @RateLimiter(limit = 20)
    public Result<Page<EduSignRecord>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Long scheduleId
    ) {
        Page<EduSignRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EduSignRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(scheduleId != null, EduSignRecord::getScheduleId, scheduleId);
        signRecordService.page(page, wrapper);
        return Result.success(page);
    }

    /**
     * 学员签到（分布式锁防止重复签到）
     */
    @PostMapping
    @PreAuthorize("hasAuthority('edu:sign:add')")
    @OperateLog("学员签到打卡")
    @RepeatSubmit
    public Result<Void> sign(@RequestBody EduSignRecord signRecord) {
        // 只做参数接收，全部交给Service处理业务
        return signRecordService.doSign(signRecord);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('edu:sign:edit')")
    @OperateLog("修改签到记录")
    public Result<Void> update(@RequestBody EduSignRecord signRecord) {
        signRecordService.updateById(signRecord);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edu:sign:remove')")
    @OperateLog("删除签到记录")
    public Result<Void> delete(@PathVariable Long id) {
        signRecordService.removeById(id);
        return Result.success();
    }
}