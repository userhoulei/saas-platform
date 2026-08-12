package com.cn.saasplatform.controller.edu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.aspect.annotation.OperateLog;
import com.cn.saasplatform.aspect.annotation.RateLimiter;
import com.cn.saasplatform.aspect.annotation.RepeatSubmit;
import com.cn.saasplatform.entity.edu.EduSchedule;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.service.edu.IEduScheduleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/edu/schedule")
public class EduScheduleController {

    @Resource
    private IEduScheduleService scheduleService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('edu:schedule:list')")
    @RateLimiter(limit = 20)
    public Result<Page<EduSchedule>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Long classId
    ) {
        Page<EduSchedule> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EduSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(classId != null, EduSchedule::getClassId, classId);
        scheduleService.page(page, wrapper);
        return Result.success(page);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('edu:schedule:add')")
    @OperateLog("新增排课")
    @RepeatSubmit
    public Result<Void> add(@RequestBody EduSchedule schedule) {
        scheduleService.save(schedule);
        return Result.success();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('edu:schedule:edit')")
    @OperateLog("修改排课")
    @RepeatSubmit
    public Result<Void> update(@RequestBody EduSchedule schedule) {
        scheduleService.updateById(schedule);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edu:schedule:remove')")
    @OperateLog("删除排课")
    public Result<Void> delete(@PathVariable Long id) {
        scheduleService.removeById(id);
        return Result.success();
    }

    /**
     * 根据班级+日期查询排课（日历视图）
     */
    @GetMapping("/class/{classId}/date")
    public Result<List<EduSchedule>> getByClassAndDate(
            @PathVariable Long classId,
            @RequestParam LocalDate date
    ) {
        LambdaQueryWrapper<EduSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduSchedule::getClassId, classId)
                .eq(EduSchedule::getCourseDate, date);
        return Result.success(scheduleService.list(wrapper));
    }
}