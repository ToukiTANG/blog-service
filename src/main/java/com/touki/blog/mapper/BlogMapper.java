package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.entity.Blog;
import com.touki.blog.entity.vo.NewBlog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface BlogMapper extends BaseMapper<Blog> {
    /**
     * 查询指定数量的NewBlog
     *
     * @param newBlogSize:
     * @return: java.util.List<com.touki.blog.entity.vo.NewBlog>
     */
    List<NewBlog> getNewBlogs(int newBlogSize);
}
