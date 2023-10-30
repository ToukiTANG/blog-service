package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.entity.VisitRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface VisitRecordMapper extends BaseMapper<VisitRecord> {
    /**
     * 查询最近的访客记录
     *
     * @param limit 最近天数
     * @return List<VisitRecord>
     */
    List<VisitRecord> visitRecordListLimit(Integer limit);
}
