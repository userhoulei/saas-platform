package com.cn.saasplatform.service.edu.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.edu.EduClass;
import com.cn.saasplatform.entity.edu.EduSchedule;
import com.cn.saasplatform.entity.edu.EduStudent;
import com.cn.saasplatform.mapper.edu.EduClassMapper;
import com.cn.saasplatform.mapper.edu.EduScheduleMapper;
import com.cn.saasplatform.mapper.edu.EduStudentMapper;
import com.cn.saasplatform.service.edu.IEduClassService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDate;

@Service
public class EduClassServiceImpl extends ServiceImpl<EduClassMapper, EduClass>
        implements IEduClassService {

    @Resource
    private EduStudentMapper studentMapper;

    @Resource
    private EduScheduleMapper scheduleMapper;

    /**
     * 班级结业
     */
    @Override
    public void finishClass(Long classId) {
        EduClass eduClass = new EduClass();
        eduClass.setId(classId);
        eduClass.setStatus(0);
        updateById(eduClass);
    }

    /**
     * 新增校验：开班时间 < 结课时间
     */
    @Override
    public boolean save(EduClass entity) {
        LocalDate start = entity.getStartDate();
        LocalDate end = entity.getEndDate();
        if (start != null && end != null && start.isAfter(end)) {
            throw new RuntimeException("开班日期不能晚于结课日期");
        }
        return super.save(entity);
    }

    /**
     * 修改同样校验日期
     */
    @Override
    public boolean updateById(EduClass entity) {
        LocalDate start = entity.getStartDate();
        LocalDate end = entity.getEndDate();
        if (start != null && end != null && start.isAfter(end)) {
            throw new RuntimeException("开班日期不能晚于结课日期");
        }
        return super.updateById(entity);
    }

    /**
     * 删除校验：有学员 或 有排课 不能删
     */
    public boolean removeById(Long id) {
        // 1. 是否有学员
        LambdaQueryWrapper<EduStudent> stuWrapper = new LambdaQueryWrapper<>();
        stuWrapper.eq(EduStudent::getClassId, id);
        long stuCount = studentMapper.selectCount(stuWrapper);
        if (stuCount > 0) {
            throw new RuntimeException("班级下存在学员，无法删除");
        }

        // 2. 是否有排课记录
        LambdaQueryWrapper<EduSchedule> schWrapper = new LambdaQueryWrapper<>();
        schWrapper.eq(EduSchedule::getClassId, id);
        long schCount = scheduleMapper.selectCount(schWrapper);
        if (schCount > 0) {
            throw new RuntimeException("班级存在排课记录，无法删除");
        }

        return super.removeById(id);
    }

    /**
     * 判断班级是否已结业
     */
    public boolean isClassFinished(Long classId) {
        EduClass eduClass = getById(classId);
        if (eduClass == null) {
            throw new RuntimeException("班级不存在");
        }
        return eduClass.getStatus() == 0;
    }
}