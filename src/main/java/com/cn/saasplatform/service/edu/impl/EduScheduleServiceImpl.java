package com.cn.saasplatform.service.edu.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.edu.EduSchedule;
import com.cn.saasplatform.mapper.edu.EduScheduleMapper;
import com.cn.saasplatform.service.edu.IEduClassService;
import com.cn.saasplatform.service.edu.IEduScheduleService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EduScheduleServiceImpl extends ServiceImpl<EduScheduleMapper, EduSchedule>
        implements IEduScheduleService {

    @Resource
    private IEduClassService classService;

    /**
     * 新增排课校验：同一老师同一天时间段不能重叠
     */
    @Override
    public boolean save(EduSchedule entity) {
        Long teacherId = entity.getTeacherId();
        LocalDate date = entity.getCourseDate();
        LocalTime sTime = entity.getStartTime();
        LocalTime eTime = entity.getEndTime();

        // 校验班级是否结业
        if (classService.isClassFinished(entity.getClassId())) {
            throw new RuntimeException("班级已结业，禁止排课");
        }

        // 查询该老师当天已有课时
        LambdaQueryWrapper<EduSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduSchedule::getTeacherId, teacherId)
                .eq(EduSchedule::getCourseDate, date);

        for (EduSchedule sch : list(wrapper)) {
            LocalTime existS = sch.getStartTime();
            LocalTime existE = sch.getEndTime();
            // 时间段重叠判断
            boolean overlap = !(eTime.isBefore(existS) || sTime.isAfter(existE));
            if (overlap) {
                throw new RuntimeException("该教师当天此时间段已有课程，排课冲突");
            }
        }

        return super.save(entity);
    }
}