package com.cn.saasplatform.service.edu.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.edu.EduClass;
import com.cn.saasplatform.entity.edu.EduStudent;
import com.cn.saasplatform.entity.vo.StudentPageVO;
import com.cn.saasplatform.mapper.edu.EduStudentMapper;
import com.cn.saasplatform.service.edu.IEduClassService;
import com.cn.saasplatform.service.edu.IEduStudentService;
import com.cn.saasplatform.service.platform.ITenantPackageService;
import com.cn.saasplatform.util.TenantContextUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class EduStudentServiceImpl extends ServiceImpl<EduStudentMapper, EduStudent>
        implements IEduStudentService {

    @Resource
    private ITenantPackageService packageService;
    @Resource
    private IEduClassService classService;

    @Override
    public StudentPageVO pageWithPackageFlag(Long pageNum, Long pageSize, Long classId, String studentName) {
        // 分页查询学员
        Page<EduStudent> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EduStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(classId != null, EduStudent::getClassId, classId);
        wrapper.like(studentName != null, EduStudent::getStudentName, studentName);
        this.page(page, wrapper);

        // 封装VO返回
        StudentPageVO vo = new StudentPageVO();
        vo.setRecords(page.getRecords());
        vo.setTotal(page.getTotal());
        vo.setCurrent(page.getCurrent());
        vo.setSize(page.getSize());
        return vo;
    }
    /**
     * 新增学员全套校验
     */
    @Override
    public boolean save(EduStudent entity) {
        Long classId = entity.getClassId();
        Long tenantId = TenantContextUtil.getTenantId();

        // 1. 班级是否结业
        if (classService.isClassFinished(classId)) {
            throw new RuntimeException("该班级已结业，无法添加学员");
        }

        // 2. 当前班级已有人数
        LambdaQueryWrapper<EduStudent> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(EduStudent::getClassId, classId);
        long currentNum = count(countWrapper);

        // 3. 获取班级最大容量
        EduClass eduClass = classService.getById(classId);
        if (currentNum >= eduClass.getMaxNum()) {
            throw new RuntimeException("班级人数已满，无法继续添加");
        }

        // 4. 同一班级内 姓名+手机号 唯一
        LambdaQueryWrapper<EduStudent> uniqueWrapper = new LambdaQueryWrapper<>();
        uniqueWrapper.eq(EduStudent::getClassId, classId)
                .eq(EduStudent::getStudentName, entity.getStudentName())
                .eq(EduStudent::getPhone, entity.getPhone());
        long repeat = count(uniqueWrapper);
        if (repeat > 0) {
            throw new RuntimeException("该学员已在本班建档，请勿重复添加");
        }

        return super.save(entity);
    }

    /**
     * 修改（调班逻辑）
     */
    @Override
    public boolean updateById(EduStudent entity) {
        Long newClassId = entity.getClassId();
        Long studentId = entity.getId();

        // 新班级是否结业
        if (classService.isClassFinished(newClassId)) {
            throw new RuntimeException("目标班级已结业，无法调入");
        }

        // 统计新班级人数（排除自己）
        LambdaQueryWrapper<EduStudent> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(EduStudent::getClassId, newClassId)
                .ne(EduStudent::getId, studentId);
        long currentNum = count(countWrapper);

        EduClass eduClass = classService.getById(newClassId);
        if (currentNum >= eduClass.getMaxNum()) {
            throw new RuntimeException("目标班级人数已满，无法调入");
        }

        return super.updateById(entity);
    }

    /**
     * 学员调班单独封装方法（方便前端调用）
     */
    public void transferClass(Long studentId, Long targetClassId) {
        EduStudent student = getById(studentId);
        if (student == null) {
            throw new RuntimeException("学员不存在");
        }
        student.setClassId(targetClassId);
        updateById(student);
    }

    /**
     * 修改学习状态：在读/休学/结业
     */
    public void changeStudyStatus(Long studentId, String studyStatus) {
        EduStudent student = new EduStudent();
        student.setId(studentId);
        student.setStudyStatus(studyStatus);
        updateById(student);
    }
}