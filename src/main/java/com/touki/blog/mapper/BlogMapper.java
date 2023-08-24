package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.model.vo.*;
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

    /**
     * 归档年月信息
     *
     * @return: java.util.List<java.lang.String>
     */

    List<String> archiveDate();

    /**
     * 归档详情
     *
     * @return: java.util.List<com.touki.blog.entity.vo.ArchiveBlog>
     */
    List<ArchiveBlog> archiveDetail();

    /**
     * 根据年月信息归档blog
     *
     * @param blogPage:分页
     * @param yearMonth:年月信息，格式为"yyyy年MM月"
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.touki.blog.entity.Blog>
     */
    Page<Blog> archiveYearMonth(Page<Blog> blogPage, String yearMonth);

    /**
     * 分页查询BlogInfo
     *
     * @param blogPage: 分页
     * @return: java.util.List<com.touki.blog.entity.vo.BlogInfo>
     */
    Page<BlogInfo> getBlogInfos(Page<BlogInfo> blogPage);
}
