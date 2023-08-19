package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.entity.Blog;
import com.touki.blog.entity.vo.*;

import java.util.List;

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

    /**
     * 查找size的最新文章NewBlog
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
     * 查询文章详情
     *
     * @param blogId: 文章id
     * @return: com.touki.blog.entity.vo.BlogDetail
     */
    BlogDetail getBlogDetailById(Long blogId);

    /**
     * 通过分类id查询分页BlogInfo
     *
     * @param categoryId: 分类id
     * @param pageNum:    第几页，从1开始
     * @param pageSize:   分页大小
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    PageResult<BlogInfo> getBlogInfosByCategoryId(Long categoryId, int pageNum, int pageSize);

    /**
     * 通过标签id查询分页BlogInfo
     *
     * @param tagId:标签id
     * @param pageNum:第几页，从1开始
     * @param pageSize:分页大小
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    PageResult<BlogInfo> getBlogInfosByTagId(Long tagId, int pageNum, int pageSize);

    /**
     * 模糊查询标题与正文内容
     *
     * @param queryString: 查询字符串
     * @return: java.util.List<com.touki.blog.entity.vo.SearchBlog>
     */
    List<SearchBlog> searchBlog(String queryString);

    /**
     * 查询归档信息
     *
     * @return: com.touki.blog.entity.vo.ArchiveResult
     */
    ArchiveResult getArchive();

    /**
     * 通过年月字符串查询分页BlogInfo
     *
     * @param pageNum:   第几页，从1开始
     * @param pageSize:  分页大小
     * @param yearMonth: 年月，格式为"yyyy年MM月"
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    PageResult<BlogInfo> getBlogInfosByYearMonth(Long pageNum, int pageSize, String yearMonth);
}
