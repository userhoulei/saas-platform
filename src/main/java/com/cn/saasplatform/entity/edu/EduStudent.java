package com.cn.saasplatform.entity.edu;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

@Data
@TableName("edu_student")
public class EduStudent extends BaseEntity {

    /**
     * 学员姓名
     */
    private String studentName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 班级id
     */
    private Long classId;

    /**
     * 缴费状态 UNPAID未付 PAID已付
     */
    private String payStatus;

    /**
     * 学习状态 STUDYING在读 SUSPEND休学 FINISH结业
     */
    private String studyStatus;

    // K12套餐专属字段
    private String grade;
    private String parentName;
    private String parentPhone;
}