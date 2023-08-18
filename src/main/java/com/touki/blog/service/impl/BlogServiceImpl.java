package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.entity.Blog;
import com.touki.blog.entity.Category;
import com.touki.blog.entity.Content;
import com.touki.blog.entity.Tag;
import com.touki.blog.entity.vo.BlogInfo;
import com.touki.blog.entity.vo.NewBlog;
import com.touki.blog.entity.vo.PageResult;
import com.touki.blog.entity.vo.RandomBlog;
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

    public BlogServiceImpl(BlogMapper blogMapper, ContentMapper contentMapper, CategoryMapper categoryMapper, TagMapper tagMapper) {
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
        queryWrapper.orderByDesc(Blog::getTop);
        Page<Blog> blogPageResult = this.page(blogPage, queryWrapper);
        ArrayList<BlogInfo> blogInfos = getBlogInfos(blogPageResult);
        PageResult<BlogInfo> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(blogInfos.size());
        pageResult.setDataList(blogInfos);
        return pageResult;
    }

    private ArrayList<BlogInfo> getBlogInfos(Page<Blog> blogPageResult) {
        ArrayList<BlogInfo> blogInfos = new ArrayList<>();
        List<Blog> blogs = blogPageResult.getRecords();
        blogs.forEach(blog -> {
            BlogInfo blogInfo = new BlogInfo();
            BeanUtils.copyProperties(blog, blogInfo);
            Content content = contentMapper.selectById(blog.getContentId());
            blogInfo.setContent(content);
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
}
