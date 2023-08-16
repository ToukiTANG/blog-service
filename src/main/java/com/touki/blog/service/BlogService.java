package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.entity.Blog;
import com.touki.blog.entity.vo.BlogInfo;
import com.touki.blog.entity.vo.PageResult;

/**
 * @author Touki
 */
public interface BlogService extends IService<Blog> {
    /**
     * 分页查询BlogInfo
     *
     * @param pageNum:  第几页，默认1
     * @param pageSize: 分页大小，默认5
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    PageResult<BlogInfo> getBlogInfos(Integer pageNum, Integer pageSize);
}
