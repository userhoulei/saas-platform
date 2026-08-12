package com.cn.saasplatform.entity.edu;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("edu_course")
public class EduCourse extends BaseEntity {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类型 字典course_type
     */
    private String courseType;

    /**
     * 总课时
     */
    private Integer totalHour;

    /**
     * 课程售价
     */
    private BigDecimal price;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1上架 0下架
     */
    private Integer status;
}