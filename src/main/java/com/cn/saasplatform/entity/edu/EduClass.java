package com.cn.saasplatform.entity.edu;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("edu_class")
public class EduClass extends BaseEntity {

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 班级类型 class_type字典
     */
    private String classType;

    /**
     * 授课老师id
     */
    private Long teacherId;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 开班日期
     */
    private LocalDate startDate;

    /**
     * 结课日期
     */
    private LocalDate endDate;

    /**
     * 1正常 0结业
     */
    private Integer status;
}