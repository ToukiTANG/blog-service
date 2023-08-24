package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 查询blog的tag
     *
     * @param blogId:
     * @return: java.util.List<com.touki.blog.entity.Tag>
     */
    List<Tag> findTagsByBlogId(Long blogId);
}
