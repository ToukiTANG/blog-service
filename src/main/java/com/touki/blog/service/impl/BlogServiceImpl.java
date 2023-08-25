package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.*;
import com.touki.blog.model.dto.BlogTagDto;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Content;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.vo.*;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.RedisService;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    private final BlogMapper blogMapper;
    private final ContentMapper contentMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final RedisService redisService;
    private final BlogTagDtoMapper blogTagDtoMapper;

    public BlogServiceImpl(BlogMapper blogMapper, ContentMapper contentMapper, CategoryMapper categoryMapper,
                           TagMapper tagMapper, RedisService redisService, BlogTagDtoMapper blogTagDtoMapper) {
        this.blogMapper = blogMapper;
        this.contentMapper = contentMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.redisService = redisService;
        this.blogTagDtoMapper = blogTagDtoMapper;
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
        String jsonString = redisService.getHash(RedisKeyConstant.HOME_BLOG_INFO, pageNum.toString());
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, new TypeReference<PageResult<BlogInfo>>() {
            });
        }
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        Page<BlogInfo> blogInfos = blogMapper.getBlogInfos(blogPage, null);
        PageResult<BlogInfo> pageResult = getPageResult(pageSize, blogInfos);
        redisService.setHash(RedisKeyConstant.HOME_BLOG_INFO, pageNum.toString(),
                JsonUtil.writeValueAsString(pageResult));
        return pageResult;
    }

    private ArrayList<BlogInfo> getBlogInfos(Page<Blog> blogPageResult) {
        ArrayList<BlogInfo> blogInfos = new ArrayList<>();
        List<Blog> blogs = blogPageResult.getRecords();
        blogs.forEach(blog -> {
            BlogInfo blogInfo = new BlogInfo();
            BeanUtils.copyProperties(blog, blogInfo);
            blogInfo.setDescription(MarkdownUtil.markdownToHtmlExtensions(blogInfo.getDescription()));
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
        String jsonString = redisService.getHash(RedisKeyConstant.BLOG_DETAIL, blogId.toString());
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, BlogDetail.class);
        }
        Blog blog = this.getById(blogId);
        BlogDetail blogDetail = new BlogDetail();
        BeanUtils.copyProperties(blog, blogDetail);
        Content content = contentMapper.selectById(blog.getContentId());
        content.setText(MarkdownUtil.markdownToHtmlExtensions(content.getText()));
        blogDetail.setContent(content);
        Category category = categoryMapper.selectById(blog.getCategoryId());
        blogDetail.setCategory(category);
        List<Tag> tags = tagMapper.findTagsByBlogId(blogId);
        blogDetail.setTags(tags);
        redisService.setHash(RedisKeyConstant.BLOG_DETAIL, blogId.toString(), JsonUtil.writeValueAsString(blogDetail));
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
    public PageResult<BlogInfo> getBlogInfosByCategoryId(Long categoryId, Integer pageNum, Integer pageSize) {
        String jsonString = redisService.getHash(RedisKeyConstant.CATEGORY_BLOG_INFO + categoryId, pageNum.toString());
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, new TypeReference<PageResult<BlogInfo>>() {
            });
        }
        LambdaQueryWrapper<Blog> blogQuery = new LambdaQueryWrapper<>();
        blogQuery.eq(Blog::getCategoryId, categoryId).orderByDesc(Blog::getTop).orderByDesc(Blog::getUpdateTime);
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        Page<BlogInfo> blogInfoPage = blogMapper.getBlogInfos(blogPage, categoryId);
        PageResult<BlogInfo> pageResult = getPageResult(pageSize, blogInfoPage);
        redisService.setHash(RedisKeyConstant.CATEGORY_BLOG_INFO + categoryId, pageNum.toString(),
                JsonUtil.writeValueAsString(pageResult));
        return pageResult;
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
    public PageResult<BlogInfo> getBlogInfosByTagId(Long tagId, Integer pageNum, Integer pageSize) {
        String jsonString = redisService.getHash(RedisKeyConstant.TAG_BLOG_INFO + tagId, pageNum.toString());
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, new TypeReference<PageResult<BlogInfo>>() {
            });
        }
        Page<BlogInfo> blogPage = new Page<>(pageNum, pageSize);
        Page<BlogInfo> selectPage = blogMapper.getBlogPageByTagId(blogPage, tagId);
        // 拿到需要查询的blogIds
        List<Long> blogIds = selectPage.getRecords().stream().map(BlogInfo::getBlogId).collect(Collectors.toList());
        // 查询关联dto
        List<BlogTagDto> blogTagDtos = blogTagDtoMapper.getListByBlogIds(blogIds);
        // 分组聚合
        Map<Long, List<BlogTagDto>> collect =
                blogTagDtos.stream().collect(Collectors.groupingBy(BlogTagDto::getBlogId, Collectors.toList()));
        // 设置tags
        selectPage.getRecords().forEach(blogInfo -> {
            List<BlogTagDto> dtos = collect.get(blogInfo.getBlogId());
            List<Tag> tags = new ArrayList<>();
            copyTags(dtos, tags);
            blogInfo.setTags(tags);
        });

        PageResult<BlogInfo> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal((int) selectPage.getTotal());
        pageResult.setDataList(selectPage.getRecords());

        redisService.setHash(RedisKeyConstant.TAG_BLOG_INFO + tagId, pageNum.toString(),
                JsonUtil.writeValueAsString(pageResult));
        return pageResult;
    }

    private static void copyTags(List<BlogTagDto> dtos, List<Tag> tags) {
        dtos.forEach(d -> {
            Tag tag = new Tag();
            BeanUtils.copyProperties(d, tag);
            tags.add(tag);
        });
    }

    private static PageResult<BlogInfo> getPageResult(Integer pageSize, Page<BlogInfo> blogInfoPage) {
        List<BlogInfo> records = blogInfoPage.getRecords();
        records.forEach(blogInfo -> blogInfo.setDescription(MarkdownUtil.markdownToHtmlExtensions(blogInfo.getDescription())));
        PageResult<BlogInfo> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal((int) blogInfoPage.getTotal());
        pageResult.setDataList(records);
        return pageResult;
    }

    private PageResult<BlogInfo> getBlogInfoPageResult(Integer pageSize, Page<Blog> page) {
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

    /**
     * 查询归档信息
     *
     * @return: com.touki.blog.entity.vo.ArchiveResult
     */
    @Override
    public ArchiveResult getArchive() {
        String jsonString = redisService.getValue(RedisKeyConstant.ARCHIVE_INFO);
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, ArchiveResult.class);
        }
        ArchiveResult archiveResult = new ArchiveResult();
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
        redisService.setValue(RedisKeyConstant.ARCHIVE_INFO, JsonUtil.writeValueAsString(archiveResult));
        return archiveResult;
    }

    /**
     * 通过年月字符串查询分页BlogInfo
     *
     * @param pageNum   :  第几页，从1开始
     * @param pageSize  : 分页大小
     * @param yearMonth : 年月，格式为"yyyy年MM月"
     * @return: com.touki.blog.entity.vo.PageResult<com.touki.blog.entity.vo.BlogInfo>
     */
    @Override
    public PageResult<BlogInfo> getBlogInfosByYearMonth(Long pageNum, Integer pageSize, String yearMonth) {
        String jsonString = redisService.getHash(RedisKeyConstant.BLOG_INFO_YEAR_MONTH, yearMonth);
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, new TypeReference<PageResult<BlogInfo>>() {
            });
        }
        Page<Blog> blogPage = new Page<>(pageNum, pageSize);
        Page<Blog> pageResult = blogMapper.archiveYearMonth(blogPage, yearMonth);
        PageResult<BlogInfo> blogInfoPageResult = getBlogInfoPageResult(pageSize, pageResult);
        redisService.setHash(RedisKeyConstant.BLOG_INFO_YEAR_MONTH, yearMonth,
                JsonUtil.writeValueAsString(blogInfoPageResult));
        return blogInfoPageResult;
    }
}
