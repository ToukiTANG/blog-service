package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.touki.blog.entity.Blog;
import com.touki.blog.entity.vo.NewBlog;
import com.touki.blog.entity.vo.RandomBlog;
import com.touki.blog.entity.vo.SearchBlog;
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
     * @param size: size
     * @return: java.util.List<com.touki.blog.entity.vo.NewBlog>
     */
    List<NewBlog> getNewBlogs(int size);

    /**
     * 查找size的随机文章RandomBlog
     *
     * @param size: size
     * @return: java.util.List<com.touki.blog.entity.vo.RandomBlog>
     */
    List<RandomBlog> getRandomBlogs(int size);

    /**
     * 通过tagId查询分页Blog
     *
     * @param blogPage: 分页
     * @param tagId:    tagId
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.touki.blog.entity.Blog>
     */
    Page<Blog> getBlogPageByTagId(Page<Blog> blogPage, Long tagId);

    /**
     * 模糊查询标题与正文
     *
     * @param queryString: 查询字符串
     * @return: java.util.List<com.touki.blog.entity.vo.SearchBlog>
     */
    List<SearchBlog> searchBlog(String queryString);
}
