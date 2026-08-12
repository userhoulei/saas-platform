package com.cn.saasplatform.service.edu;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.edu.EduStudent;
import com.cn.saasplatform.entity.vo.StudentPageVO;

public interface IEduStudentService extends IService<EduStudent> {

    /**
     * 学员分页
     */
    StudentPageVO pageWithPackageFlag(Long pageNum, Long pageSize, Long classId, String studentName);

    void transferClass(Long studentId, Long targetClassId);

    void changeStudyStatus(Long studentId, String studyStatus);
}