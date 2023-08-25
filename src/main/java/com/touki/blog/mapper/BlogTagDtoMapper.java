package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.dto.BlogTagDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface BlogTagDtoMapper extends BaseMapper<BlogTagDto> {
    /**
     * 通过blogId批量查询BlogTagDto
     *
     * @param blogIds blogIds
     * @return List<BlogTagDto>
     */
    List<BlogTagDto> getListByBlogIds(List<Long> blogIds);
}
