package com.cn.saasplatform.controller.edu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.aspect.annotation.OperateLog;
import com.cn.saasplatform.aspect.annotation.RateLimiter;
import com.cn.saasplatform.aspect.annotation.RepeatSubmit;
import com.cn.saasplatform.entity.edu.EduCourse;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.service.edu.IEduCourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/edu/course")
public class EduCourseController {

    @Resource
    private IEduCourseService eduCourseService;

    /**
     * 分页列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('edu:course:list')")
    @RateLimiter(limit = 30, second = 60)
    public Result<Page<EduCourse>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String courseType
    ) {
        Page<EduCourse> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(courseName != null, EduCourse::getCourseName, courseName);
        wrapper.eq(courseType != null, EduCourse::getCourseType, courseType);
        eduCourseService.page(page, wrapper);
        return Result.success(page);
    }

    /**
     * 新增课程
     */
    @PostMapping
    @PreAuthorize("hasAuthority('edu:course:add')")
    @OperateLog("新增课程")
    @RepeatSubmit
    public Result<Void> add(@RequestBody EduCourse eduCourse) {
        eduCourseService.save(eduCourse);
        return Result.success();
    }

    /**
     * 修改课程
     */
    @PutMapping
    @PreAuthorize("hasAuthority('edu:course:edit')")
    @OperateLog("修改课程")
    @RepeatSubmit
    public Result<Void> update(@RequestBody EduCourse eduCourse) {
        eduCourseService.updateById(eduCourse);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edu:course:remove')")
    @OperateLog("删除课程")
    public Result<Void> delete(@PathVariable Long id) {
        eduCourseService.removeById(id);
        return Result.success();
    }

    /**
     * 上架下拉列表（班级选择用）
     */
    @GetMapping("/list/enable")
    public Result<?> getEnableCourse() {
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getStatus, 1);
        return Result.success(eduCourseService.list(wrapper));
    }

    @PutMapping("/online/{id}")
    public Result<Void> online(@PathVariable Long id) {
        eduCourseService.online(id);
        return Result.success();
    }

    @PutMapping("/offline/{id}")
    public Result<Void> offline(@PathVariable Long id) {
        eduCourseService.offline(id);
        return Result.success();
    }
}