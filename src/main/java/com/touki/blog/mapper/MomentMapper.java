package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.entity.Moment;
import org.springframework.stereotype.Repository;

/**
 * @author Touki
 */
@Repository
public interface MomentMapper extends BaseMapper<Moment> {
    /**
     * 点赞动态
     *
     * @param momentId:动态id
     * @return: void
     */
    void likeMoment(Long momentId);
}
