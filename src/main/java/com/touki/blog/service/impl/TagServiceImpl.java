package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.RespCode;
import com.touki.blog.exception.MyException;
import com.touki.blog.mapper.TagMapper;
import com.touki.blog.model.dto.NewTag;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.TagsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Touki
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagsService {
    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public PageResult<Tag> tagList(Integer pageNum, Integer pageSize) {
        Page<Tag> tagPage = new Page<>(pageNum, pageSize);
        tagPage = this.page(tagPage);

        PageResult<Tag> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal((int) tagPage.getTotal());
        pageResult.setDataList(tagPage.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTag(NewTag newTag) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(newTag, tag);
        this.save(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long tagId) throws MyException {
        List<Long> blogIds = tagMapper.blogIds(tagId);
        if (!CollectionUtils.isEmpty(blogIds)) {
            throw new MyException(RespCode.PARAMETER_ERROR, "该标签下还有文章，不允许删除！");
        }
        this.removeById(tagId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(Tag tag) {
        tag.setUpdateTime(new Date());
        this.updateById(tag);
    }
}
