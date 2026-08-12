package com.cn.saasplatform.entity.edu;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@TableName("edu_schedule")
public class EduSchedule extends BaseEntity {

    /**
     * 班级id
     */
    private Long classId;

    /**
     * 老师id
     */
    private Long teacherId;

    /**
     * 上课日期
     */
    private LocalDate courseDate;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 授课内容
     */
    private String content;
}