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

    /**
     * 删除文章和标签的关联关系
     *
     * @param blogId       文章id
     * @param deleteTagIds 需要删除的标签id集合
     */
    void deleteBlogTagConnect(Long blogId, List<Long> deleteTagIds);

    /**
     * 批量插入文章标签关系
     *
     * @param blogId    文章id
     * @param newTagIds 标签id集合
     */
    void insertBlogTag(Long blogId, List<Long> newTagIds);

    /**
     * 标签下的文章id集合
     *
     * @param tagId 标签id
     * @return List<Long>
     */
    List<Long> blogIds(Long tagId);
}
