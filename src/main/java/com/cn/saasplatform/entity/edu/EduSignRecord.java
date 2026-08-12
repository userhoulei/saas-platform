package com.cn.saasplatform.entity.edu;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("edu_sign_record")
public class EduSignRecord extends BaseEntity {

    /**
     * 排课id
     */
    private Long scheduleId;

    /**
     * 学员id
     */
    private Long studentId;

    /**
     * 签到类型 正常/迟到/旷课/请假
     */
    private String signType;

    /**
     * 签到时间
     */
    private LocalDateTime signTime;

    /**
     * 备注
     */
    private String remark;
}