package com.cn.saasplatform.service.edu;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.edu.EduClass;

public interface IEduClassService extends IService<EduClass> {

    void finishClass(Long classId);

    boolean isClassFinished(Long classId);
}