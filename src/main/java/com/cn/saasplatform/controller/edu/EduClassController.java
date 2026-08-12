package com.cn.saasplatform.controller.edu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.aspect.annotation.OperateLog;
import com.cn.saasplatform.aspect.annotation.RateLimiter;
import com.cn.saasplatform.aspect.annotation.RepeatSubmit;
import com.cn.saasplatform.entity.edu.EduClass;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.service.edu.IEduClassService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/edu/class")
public class EduClassController {

    @Resource
    private IEduClassService eduClassService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('edu:class:list')")
    @RateLimiter(limit = 30)
    public Result<Page<EduClass>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Long courseId
    ) {
        Page<EduClass> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EduClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(courseId != null, EduClass::getCourseId, courseId);
        eduClassService.page(page, wrapper);
        return Result.success(page);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('edu:class:add')")
    @OperateLog("新增班级")
    @RepeatSubmit
    public Result<Void> add(@RequestBody EduClass eduClass) {
        eduClassService.save(eduClass);
        return Result.success();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('edu:class:edit')")
    @OperateLog("修改班级")
    @RepeatSubmit
    public Result<Void> update(@RequestBody EduClass eduClass) {
        eduClassService.updateById(eduClass);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edu:class:remove')")
    @OperateLog("删除班级")
    public Result<Void> delete(@PathVariable Long id) {
        eduClassService.removeById(id);
        return Result.success();
    }

    @PutMapping("/finish/{id}")
    @PreAuthorize("hasAuthority('edu:class:edit')")
    @OperateLog("班级毕业")
    public Result<Void> finish(@PathVariable Long id) {
        eduClassService.finishClass(id);
        return Result.success();
    }

    /**
     * 获取正常班级下拉
     */
    @GetMapping("/normal/list")
    public Result<?> getNormalClass() {
        LambdaQueryWrapper<EduClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduClass::getStatus, 1);
        return Result.success(eduClassService.list(wrapper));
    }
}