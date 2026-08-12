package com.cn.saasplatform.service.edu.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.edu.EduClass;
import com.cn.saasplatform.entity.edu.EduCourse;
import com.cn.saasplatform.mapper.edu.EduCourseMapper;
import com.cn.saasplatform.mapper.edu.EduClassMapper;
import com.cn.saasplatform.service.edu.IEduCourseService;
import com.cn.saasplatform.util.TenantContextUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse>
        implements IEduCourseService {

    @Resource
    private EduClassMapper eduClassMapper;

    /**
     * 新增校验：同一租户下课程名称不能重复
     */
    @Override
    public boolean save(EduCourse entity) {
        Long tenantId = TenantContextUtil.getTenantId();
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getCourseName, entity.getCourseName())
                .eq(EduCourse::getTenantId, tenantId);
        long count = count(wrapper);
        if (count > 0) {
            throw new RuntimeException("当前机构已存在同名课程");
        }
        return super.save(entity);
    }

    /**
     * 修改校验：排除自身ID，名称唯一
     */
    @Override
    public boolean updateById(EduCourse entity) {
        Long tenantId = TenantContextUtil.getTenantId();
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getCourseName, entity.getCourseName())
                .eq(EduCourse::getTenantId, tenantId)
                .ne(EduCourse::getId, entity.getId());
        long count = count(wrapper);
        if (count > 0) {
            throw new RuntimeException("当前机构已存在同名课程");
        }
        return super.updateById(entity);
    }

    /**
     * 删除前校验：是否被班级绑定
     */
    public boolean removeById(Long id) {
        LambdaQueryWrapper<EduClass> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.eq(EduClass::getCourseId, id);
        Long usedCount = eduClassMapper.selectCount(classWrapper);
        if (usedCount > 0) {
            throw new RuntimeException("该课程已被班级引用，无法删除");
        }
        return super.removeById(id);
    }

    // 手动上架
    public void online(Long courseId) {
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus(1);
        updateById(course);
    }

    // 手动下架
    public void offline(Long courseId) {
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus(0);
        updateById(course);
    }
}