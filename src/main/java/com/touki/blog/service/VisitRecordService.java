package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.VisitRecord;

import java.util.List;

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

    /**
     * 查询最近的访客记录
     *
     * @param limit 最近天数
     * @return List<VisitRecord>
     */
    List<VisitRecord> visitRecordListLimit(Integer limit);
}
