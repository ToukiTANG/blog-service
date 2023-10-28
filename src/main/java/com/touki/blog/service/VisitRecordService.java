package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.VisitRecord;

/**
 * @author Touki
 */
public interface VisitRecordService extends IService<VisitRecord> {
    /**
     * 保存访问统计记录
     *
     * @param visitRecord VisitRecord
     */
    void saveRecord(VisitRecord visitRecord);
}
