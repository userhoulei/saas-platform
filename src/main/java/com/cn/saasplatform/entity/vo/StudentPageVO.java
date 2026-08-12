package com.cn.saasplatform.entity.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.entity.edu.EduStudent;
import lombok.Data;
import java.util.List;

@Data
public class StudentPageVO {
    private List<EduStudent> records;
    private Long total;
    private Long current;
    private Long size;
    // 是否展示K12家长、年级字段
    private Boolean showK12Field;
}
