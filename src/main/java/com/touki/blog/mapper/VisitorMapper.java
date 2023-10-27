package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.entity.Visitor;
import org.springframework.stereotype.Repository;

/**
 * @author Touki
 */
@Repository
public interface VisitorMapper extends BaseMapper<Visitor> {
    /**
     * 数据库是否有访客标识
     *
     * @param identification 访客标识
     * @return 是否存在
     */
    int hasUUID(String identification);
}
