package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.VisitRecordMapper;
import com.touki.blog.model.entity.VisitRecord;
import com.touki.blog.service.VisitRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Touki
 */
@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord> implements VisitRecordService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(VisitRecord visitRecord) {
        this.save(visitRecord);
    }

    @Override
    public List<VisitRecord> visitRecordListLimit(Integer limit) {
        return this.baseMapper.visitRecordListLimit(limit);
    }
}
