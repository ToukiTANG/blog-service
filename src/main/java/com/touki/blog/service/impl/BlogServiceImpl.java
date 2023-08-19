package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.entity.Blog;
import com.touki.blog.entity.Category;
import com.touki.blog.entity.Content;
import com.touki.blog.entity.Tag;
import com.touki.blog.entity.vo.*;
import com.touki.blog.mapper.BlogMapper;
import com.touki.blog.mapper.CategoryMapper;
import com.touki.blog.mapper.ContentMapper;
import com.touki.blog.mapper.TagMapper;
import com.touki.blog.service.BlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Touki
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    private final BlogMapper blogMapper;
    private final ContentMapper contentMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    public BlogServiceImpl(BlogMapper blogMapper, ContentMapper contentMapper, CategoryMapper categoryMapper,
                           TagMapper tagMapper) {
        this.blogMapper = blogMapper;
        this.contentMapper = contentMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
    }

    /**
     * 分页查询BlogInfo
     *
     * @param pageNum  :  第几页，默认1
     * @param pageSize : 分页大小，默认5
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    @Override
    public PageResult<BlogInfo> getBlogInfos(Integer pageNum, Integer pageSize) {
        Page<Blog> blogPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Blog::getTop).orderByDesc(Blog::getUpdateTime);
        Page<Blog> blogPageResult = this.page(blogPage, queryWrapper);
        ArrayList<BlogInfo> blogInfos = getBlogInfos(blogPageResult);
        PageResult<BlogInfo> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal((int) blogPageResult.getTotal());
        pageResult.setDataList(blogInfos);
        return pageResult;
    }

    private ArrayList<BlogInfo> getBlogInfos(Page<Blog> blogPageResult) {
        ArrayList<BlogInfo> blogInfos = new ArrayList<>();
        List<Blog> blogs = blogPageResult.getRecords();
        blogs.forEach(blog -> {
            BlogInfo blogInfo = new BlogInfo();
            BeanUtils.copyProperties(blog, blogInfo);
            Category category = categoryMapper.selectById(blog.getCategoryId());
            blogInfo.setCategory(category);
            List<Tag> tags = tagMapper.findTagsByBlogId(blog.getBlogId());
            blogInfo.setTags(tags);
            blogInfos.add(blogInfo);
        });
        return blogInfos;
    }

    /**
     * 查找size的最新文章NewBlog
     *
     * @param size: size
     * @return: java.util.List<com.touki.blog.entity.vo.NewBlog>
     */
    @Override
    public List<NewBlog> getNewBlogs(int size) {
        return blogMapper.getNewBlogs(size);
    }

    /**
     * 查找size的随机文章RandomBlog
     *
     * @param size : size
     * @return: java.util.List<com.touki.blog.entity.vo.RandomBlog>
     */
    @Override
    public List<RandomBlog> getRandomBlogs(int size) {
        return blogMapper.getRandomBlogs(size);
    }

    /**
     * 查询文章详情
     *
     * @param blogId :
     * @return: com.touki.blog.entity.vo.BlogDetail
     */
    @Override
    public BlogDetail getBlogDetailById(Long blogId) {
        Blog blog = this.getById(blogId);
        BlogDetail blogDetail = new BlogDetail();
        BeanUtils.copyProperties(blog, blogDetail);
        Content content = contentMapper.selectById(blog.getContentId());
        blogDetail.setContent(content);
        Category category = categoryMapper.selectById(blog.getCategoryId());
        blogDetail.setCategory(category);
        List<Tag> tags = tagMapper.findTagsByBlogId(blogId);
        blogDetail.setTags(tags);
        return blogDetail;
    }

    /**
     * 通过分类id查询分页BlogInfo
     *
     * @param categoryId : 分类id
     * @param pageNum    :      第几页，从1开始
     * @param pageSize   :     分页小
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    @Override
    public PageResult<BlogInfo> getBlogInfosByCategoryId(Long categoryId, int pageNum, int pageSize) {
        LambdaQueryWrapper<Blog> blogQuery = new LambdaQueryWrapper<>();
        blogQuery.eq(Blog::getCategoryId, categoryId).orderByDesc(Blog::getTop).orderByDesc(Blog::getUpdateTime);
        Page<Blog> blogPage = new Page<>(pageNum, pageSize);
        Page<Blog> page = this.page(blogPage, blogQuery);
        return getBlogInfoPageResult(pageSize, page);
    }

    /**
     * 通过标签id查询分页BlogInfo
     *
     * @param tagId    :标签id
     * @param pageNum  :第几页，从1开始
     * @param pageSize :分页大小
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    @Override
    public PageResult<BlogInfo> getBlogInfosByTagId(Long tagId, int pageNum, int pageSize) {
        Page<Blog> blogPage = new Page<>(pageNum, pageSize);
        Page<Blog> page = blogMapper.getBlogPageByTagId(blogPage, tagId);
        return getBlogInfoPageResult(pageSize, page);
    }

    private PageResult<BlogInfo> getBlogInfoPageResult(int pageSize, Page<Blog> page) {
        ArrayList<BlogInfo> blogInfos = getBlogInfos(page);
        PageResult<BlogInfo> infoPageResult = new PageResult<>();
        infoPageResult.setDataList(blogInfos);
        infoPageResult.setPageSize(pageSize);
        infoPageResult.setTotal((int) page.getTotal());
        return infoPageResult;
    }

    /**
     * 模糊查询标题与正文内容
     *
     * @param queryString : 查询字符串
     * @return: java.util.List<com.touki.blog.entity.vo.SearchBlog>
     */
    @Override
    public List<SearchBlog> searchBlog(String queryString) {
        List<SearchBlog> searchBlogs = blogMapper.searchBlog(queryString);
        // 正文太多了，做一下处理，以搜索字符串为中心返回一共20个字
        searchBlogs.forEach(searchBlog -> {
            String contentText = searchBlog.getContentText();
            int contentLength = contentText.length();
            int index = contentText.indexOf(queryString) - 10;
            index = Math.max(index, 0);
            int end = index + 20;
            end = Math.min(end, contentLength - 1);
            searchBlog.setContentText(contentText.substring(index, end));
        });
        return searchBlogs;
    }
}
