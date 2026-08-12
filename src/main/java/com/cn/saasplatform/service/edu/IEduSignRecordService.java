package com.cn.saasplatform.service.edu;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.edu.EduSignRecord;
import com.cn.saasplatform.entity.resp.Result;

public interface IEduSignRecordService extends IService<EduSignRecord> {
    /**
     * 执行签到（含分布式锁、重复校验）
     */
    Result<Void> doSign(EduSignRecord signRecord);
}