package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.exception.MyException;
import com.touki.blog.model.dto.NewTag;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.vo.PageResult;

/**
 * @author Touki
 */
public interface TagsService extends IService<Tag> {
    /**
     * 分页查询标签列表
     *
     * @param pageNum  分页索引
     * @param pageSize 分页大小
     * @return PageResult<Tag>
     */
    PageResult<Tag> tagList(Integer pageNum, Integer pageSize);

    /**
     * 新建标签
     *
     * @param newTag NewTag
     */
    void saveTag(NewTag newTag);

    /**
     * 删除标签
     *
     * @param tagId 标签id
     */
    void deleteTag(Long tagId) throws MyException;

    /**
     * 更新标签
     *
     * @param tag Tag
     */
    void updateTag(Tag tag);
}
