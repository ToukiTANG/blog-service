package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.BlogMapper;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.model.vo.*;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.RedisService;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    private final BlogMapper blogMapper;
    private final RedisService redisService;

    public BlogServiceImpl(BlogMapper blogMapper, RedisService redisService) {
        this.blogMapper = blogMapper;
        this.redisService = redisService;
    }

    /**
     * 分页查询BlogInfo
     *
     * @param pageNum  第几页，默认1
     * @param pageSize 分页大小，默认5
     * @return PageResult<BlogInfo>
     */
    @Override
    @SuppressWarnings("unchecked")
    public PageResult<BlogInfo> getBlogInfos(Integer pageNum, Integer pageSize) {
        PageResult<BlogInfo> pageResult = (PageResult<BlogInfo>) redisService.getHash(RedisKeyConstant.HOME_BLOG_INFO
                , pageNum);
        if (pageResult != null) {
            getBlogInfoViews(pageResult);
            return pageResult;
        }
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        blogPage = blogMapper.getBlogInfos(blogPage, null, null);
        pageResult = getPageResult(pageSize, blogPage);
        redisService.setHash(RedisKeyConstant.HOME_BLOG_INFO, pageNum,
                pageResult);
        return pageResult;
    }

    /**
     * 查找size的最新文章NewBlog
     *
     * @param size size
     * @return: List<NewBlog>
     */
    @Override
    public List<NewBlog> getNewBlogs(int size) {
        return blogMapper.getNewBlogs(size);
    }

    /**
     * 查找size的随机文章RandomBlog
     *
     * @param size size
     * @return: List<RandomBlog>
     */
    @Override
    public List<RandomBlog> getRandomBlogs(int size) {
        return blogMapper.getRandomBlogs(size);
    }

    /**
     * 查询文章详情
     *
     * @param blogId 文章id
     * @return BlogDetail
     */
    @Override
    public BlogDetail getBlogDetailById(Long blogId) {
        BlogDetail blogDetail = (BlogDetail) redisService.getHash(RedisKeyConstant.BLOG_DETAIL, blogId);
        if (blogDetail != null) {
            setBlogViews(blogId, blogDetail);
            return blogDetail;
        }
        blogDetail = blogMapper.getBlogDetail(blogId);
        ContentInfo content = blogDetail.getContent();
        String description = blogDetail.getDescription();
        blogDetail.setDescription(MarkdownUtil.markdownToHtmlExtensions(description));
        content.setText(MarkdownUtil.markdownToHtmlExtensions(content.getText()));
        blogDetail.setContent(content);
        setBlogViews(blogId, blogDetail);
        redisService.setHash(RedisKeyConstant.BLOG_DETAIL, blogId, blogDetail);
        return blogDetail;
    }


    /**
     * 通过分类id查询分页BlogInfo
     *
     * @param categoryId 分类id
     * @param pageNum    第几页，从1开始
     * @param pageSize   分页小
     * @return PageResult<BlogInfo>
     */
    @Override
    @SuppressWarnings("unchecked")
    public PageResult<BlogInfo> getBlogInfosByCategoryId(Long categoryId, Integer pageNum, Integer pageSize) {
        PageResult<BlogInfo> pageResult =
                (PageResult<BlogInfo>) redisService.getHash(RedisKeyConstant.CATEGORY_BLOG_INFO + categoryId, pageNum);
        if (pageResult != null) {
            getBlogInfoViews(Objects.requireNonNull(pageResult));
            return pageResult;
        }
        LambdaQueryWrapper<Blog> blogQuery = new LambdaQueryWrapper<>();
        blogQuery.eq(Blog::getCategoryId, categoryId).orderByDesc(Blog::getTop).orderByDesc(Blog::getUpdateTime);
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        blogPage = blogMapper.getBlogInfos(blogPage, categoryId, null);
        pageResult = getPageResult(pageSize, blogPage);
        getBlogInfoViews(pageResult);
        redisService.setHash(RedisKeyConstant.CATEGORY_BLOG_INFO + categoryId, pageNum, pageResult);
        return pageResult;
    }

    /**
     * 通过标签id查询分页BlogInfo
     *
     * @param tagId    标签id
     * @param pageNum  第几页，从1开始
     * @param pageSize 分页大小
     * @return PageResult<BlogInfo>
     */
    @Override
    @SuppressWarnings("unchecked")
    public PageResult<BlogInfo> getBlogInfosByTagId(Long tagId, Integer pageNum, Integer pageSize) {
        PageResult<BlogInfo> pageResult =
                (PageResult<BlogInfo>) redisService.getHash(RedisKeyConstant.TAG_BLOG_INFO + tagId, pageNum);
        if (pageResult != null) {
            getBlogInfoViews(Objects.requireNonNull(pageResult));
            return pageResult;
        }
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        blogPage = blogMapper.getBlogInfos(blogPage, null, tagId);
        pageResult = getPageResult(pageSize, blogPage);
        getBlogInfoViews(pageResult);
        redisService.setHash(RedisKeyConstant.TAG_BLOG_INFO + tagId, pageNum, pageResult);
        return pageResult;
    }

    /**
     * 模糊查询标题与正文内容
     *
     * @param queryString 查询字符串
     * @return List<SearchBlog>
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

    /**
     * 查询归档信息
     *
     * @return: com.touki.blog.entity.vo.ArchiveResult
     */
    @Override
    public ArchiveResult getArchive() {
        ArchiveResult archiveResult = (ArchiveResult) redisService.getValue(RedisKeyConstant.ARCHIVE_INFO);
        if (archiveResult != null) {
            return archiveResult;
        }
        archiveResult = new ArchiveResult();
        long count = this.count();
        archiveResult.setCount((int) count);
        List<String> archiveYearMoth = blogMapper.archiveDate();
        List<ArchiveBlog> archiveBlogs = blogMapper.archiveDetail();
        // 这里需要保持顺序，因此使用LinkedHashMap
        HashMap<String, List<ArchiveBlog>> hashMap = new LinkedHashMap<>(4);
        archiveYearMoth.forEach(ar -> {
            List<ArchiveBlog> collect =
                    archiveBlogs.stream().filter(blog -> blog.getYearMonth().equals(ar)).collect(Collectors.toList());
            hashMap.put(ar, collect);
        });
        archiveResult.setResultMap(hashMap);
        redisService.setValue(RedisKeyConstant.ARCHIVE_INFO, archiveResult);
        return archiveResult;
    }

    /**
     * 通过年月字符串查询分页BlogInfo
     *
     * @param pageNum   第几页，从1开始
     * @param pageSize  分页大小
     * @param yearMonth 年月，格式为"yyyy-MM"
     * @return PageResult<BlogInfo>
     */
    @Override
    @SuppressWarnings("unchecked")
    public PageResult<BlogInfo> getBlogInfosByYearMonth(Long pageNum, Integer pageSize, String yearMonth) {
        PageResult<BlogInfo> pageResult =
                (PageResult<BlogInfo>) redisService.getHash(RedisKeyConstant.BLOG_INFO_YEAR_MONTH, yearMonth);
        if (pageResult != null) {
            getBlogInfoViews(Objects.requireNonNull(pageResult));
            return pageResult;
        }
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        blogPage = blogMapper.archiveYearMonth(blogPage, yearMonth);
        pageResult = getPageResult(pageSize, blogPage);
        redisService.setHash(RedisKeyConstant.BLOG_INFO_YEAR_MONTH, yearMonth, pageResult);
        return pageResult;
    }

    private void getBlogInfoViews(PageResult<BlogInfo> pageResult) {
        pageResult.getDataList().forEach(blogInfo -> {
            Integer views = (Integer) redisService.getHash(RedisKeyConstant.BLOG_VIEWS, blogInfo.getBlogId());
            // redis里没有就去则去数据库查
            if (views == null) {
                LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(Blog::getViews).eq(Blog::getBlogId, blogInfo.getBlogId());
                Blog blog = blogMapper.selectOne(queryWrapper);
                views = blog.getViews();
                redisService.setHash(RedisKeyConstant.BLOG_VIEWS, blogInfo.getBlogId(), views);
            }
            blogInfo.setViews(views);
        });
    }

    /**
     * 把Page<BlogInfo>转换为PageResult<BlogInfo>，包括将description转化为markdown并设置views
     *
     * @param pageSize     分页大小
     * @param blogInfoPage blogInfoPage
     * @return PageResult<BlogInfo>
     */
    private PageResult<BlogInfo> getPageResult(Integer pageSize, Page<BlogInfo> blogInfoPage) {
        List<BlogInfo> records = blogInfoPage.getRecords();
        records.forEach(blogInfo -> blogInfo.setDescription(MarkdownUtil.markdownToHtmlExtensions(blogInfo.getDescription())));
        PageResult<BlogInfo> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal((int) blogInfoPage.getTotal());
        pageResult.setDataList(records);
        getBlogInfoViews(pageResult);
        return pageResult;
    }


    private void setBlogViews(Long blogId, BlogDetail blogDetail) {
        Integer views = (Integer) redisService.getHash(RedisKeyConstant.BLOG_VIEWS, blogId);
        if (views != null) {
            redisService.incrementHash(RedisKeyConstant.BLOG_VIEWS, blogId, 1);
            blogDetail.setViews(views + 1);
        } else {
            LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Blog::getViews).eq(Blog::getBlogId, blogId);
            Blog blog = blogMapper.selectOne(queryWrapper);
            views = blog.getViews() + 1;
            redisService.setHash(RedisKeyConstant.BLOG_VIEWS, blogId, views);
            blogDetail.setViews(views);
        }
    }
}
