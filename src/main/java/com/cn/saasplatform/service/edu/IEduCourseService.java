package com.cn.saasplatform.service.edu;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.edu.EduCourse;

public interface IEduCourseService extends IService<EduCourse> {

    void online(Long courseId);

    void offline(Long courseId);
}