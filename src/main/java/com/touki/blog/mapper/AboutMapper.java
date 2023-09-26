package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.entity.About;
import org.springframework.stereotype.Repository;

/**
 * @author Touki
 */
@Repository
public interface AboutMapper extends BaseMapper<About> {
    /**
     * 更新about
     *
     * @param key   nameEn
     * @param value value
     */
    void updateAbout(String key, Object value);
}
