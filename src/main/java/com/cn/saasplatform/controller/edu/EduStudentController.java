package com.cn.saasplatform.controller.edu;

import com.cn.saasplatform.aspect.annotation.OperateLog;
import com.cn.saasplatform.aspect.annotation.RateLimiter;
import com.cn.saasplatform.aspect.annotation.RepeatSubmit;
import com.cn.saasplatform.entity.edu.EduStudent;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.vo.StudentPageVO;
import com.cn.saasplatform.service.edu.IEduStudentService;
import com.cn.saasplatform.service.platform.ITenantPackageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/edu/student")
public class EduStudentController {

    @Resource
    private IEduStudentService studentService;

    @Resource
    private ITenantPackageService packageService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('edu:student:list')")
    @RateLimiter(limit = 20)
    public Result<StudentPageVO> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String studentName
    ) {
        StudentPageVO vo = studentService.pageWithPackageFlag(pageNum, pageSize, classId, studentName);
        return Result.success(vo);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('edu:student:add')")
    @OperateLog("新增学员")
    @RepeatSubmit
    public Result<Void> add(@RequestBody EduStudent student) {
        studentService.save(student);
        return Result.success();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('edu:student:edit')")
    @OperateLog("修改学员")
    @RepeatSubmit
    public Result<Void> update(@RequestBody EduStudent student) {
        studentService.updateById(student);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edu:student:remove')")
    @OperateLog("删除学员")
    public Result<Void> delete(@PathVariable Long id) {
        studentService.removeById(id);
        return Result.success();
    }

    @PutMapping("/transfer/{studentId}")
    public Result<Void> transfer(
            @PathVariable Long studentId,
            @RequestParam Long targetClassId
    ) {
        studentService.transferClass(studentId, targetClassId);
        return Result.success();
    }

    @PutMapping("/status/{studentId}")
    public Result<Void> changeStatus(
            @PathVariable Long studentId,
            @RequestParam String studyStatus
    ) {
        studentService.changeStudyStatus(studentId, studyStatus);
        return Result.success();
    }
}