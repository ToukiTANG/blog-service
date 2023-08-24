package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.TagMapper;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.service.TagsService;
import org.springframework.stereotype.Service;

/**
 * @author Touki
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagsService {
}
