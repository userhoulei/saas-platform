package com.cn.saasplatform.service.edu.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.edu.EduSignRecord;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.mapper.edu.EduSignRecordMapper;
import com.cn.saasplatform.service.edu.IEduSignRecordService;
import com.cn.saasplatform.util.RedissonLockUtil;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class EduSignRecordServiceImpl extends ServiceImpl<EduSignRecordMapper, EduSignRecord>
        implements IEduSignRecordService {

    @Resource
    private RedissonLockUtil redissonLockUtil;

    @Override
    public Result<Void> doSign(EduSignRecord signRecord) {
        Long scheduleId = signRecord.getScheduleId();
        Long studentId = signRecord.getStudentId();

        // 1.获取分布式锁
        RLock lock = redissonLockUtil.getSignLock(scheduleId, studentId);
        if (lock == null) {
            return Result.fail(1,"请勿重复签到，操作过于频繁");
        }
        try {
            // 2.数据库校验是否已签到
            LambdaQueryWrapper<EduSignRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(EduSignRecord::getScheduleId, scheduleId)
                    .eq(EduSignRecord::getStudentId, studentId);
            long count = this.count(wrapper);
            if (count > 0) {
                return Result.fail(2,"该学员已完成本次课时签到");
            }
            // 3.填充签到时间并保存
            signRecord.setSignTime(LocalDateTime.now());
            this.save(signRecord);
            return Result.success();
        } finally {
            // 4.释放锁（finally保证必执行）
            redissonLockUtil.unLock(lock);
        }
    }
}