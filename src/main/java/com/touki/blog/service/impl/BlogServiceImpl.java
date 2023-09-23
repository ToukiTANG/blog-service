package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.BlogMapper;
import com.touki.blog.mapper.CategoryMapper;
import com.touki.blog.mapper.TagMapper;
import com.touki.blog.model.dto.BlogUpdate;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.query.AdminBlogQuery;
import com.touki.blog.model.vo.*;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.RedisService;
import com.touki.blog.service.TagsService;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    private final BlogMapper blogMapper;
    private final RedisService redisService;
    private final CategoryMapper categoryMapper;
    private final TagsService tagsService;
    private final TagMapper tagMapper;

    public BlogServiceImpl(BlogMapper blogMapper, RedisService redisService, CategoryMapper categoryMapper,
                           TagsService tagsService, TagMapper tagMapper) {
        this.blogMapper = blogMapper;
        this.redisService = redisService;
        this.categoryMapper = categoryMapper;
        this.tagsService = tagsService;
        this.tagMapper = tagMapper;
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

    @Override
    public PageResult<BlogInfo> adminBlogs(AdminBlogQuery query) {
        Page<BlogInfo> blogPage = new Page<>(query.getPageNum(), query.getPageSize());
        blogPage = blogMapper.adminBlogs(blogPage, query);


        PageResult<BlogInfo> pageResult = new PageResult<>();
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal((int) blogPage.getTotal());
        pageResult.setDataList(blogPage.getRecords());
        return pageResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @RemoveRedisCache(keyPattern = "blog*")
    @Override
    public void updateTop(Long id, Boolean top) {
        LambdaUpdateWrapper<Blog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Blog::getBlogId, id).set(Blog::getTop, top).set(Blog::getUpdateTime, new Date());
        this.update(updateWrapper);
    }

    @Override
    public BlogDetail getAdminBlogDetail(Long id) {
        return blogMapper.getBlogDetail(id);
    }

    @Override
    @RemoveRedisCache(keyPattern = "blog*")
    @Transactional(rollbackFor = Exception.class)
    public void updateBlog(BlogUpdate blogUpdate) {
        // 判断分类情况
        handleCategory(blogUpdate);
        // 判断标签情况
        handleTags(blogUpdate);
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogUpdate, blog);
        blog.setUpdateTime(new Date());
        this.updateById(blog);
    }

    private void handleTags(BlogUpdate blogUpdate) {
        List<Object> tagList = blogUpdate.getTagList();

        List<Long> newTagIds =
                tagList.stream().map(tag -> (String) tag).filter(StringUtils::isNumeric).map(Long::valueOf).collect(Collectors.toList());
        List<Tag> oldTags = tagMapper.findTagsByBlogId(blogUpdate.getBlogId());
        // 过滤出不存在的id，就是需要删除的tag关系了
        List<Long> deleteTagIds =
                oldTags.stream().map(Tag::getTagId).filter(oldId -> !newTagIds.contains(oldId)).collect(Collectors.toList());
        if (!deleteTagIds.isEmpty()) {
            tagMapper.deleteBlogTagConnect(blogUpdate.getBlogId(), deleteTagIds);
        }

        List<String> newTagNames =
                tagList.stream().map(tag -> (String) tag).filter(tag -> !StringUtils.isNumeric(tag)).collect(Collectors.toList());
        Date date = new Date();
        List<Tag> newTags = newTagNames.stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setTagName(tagName);
            tag.setDescription("");
            tag.setCreateTime(date);
            tag.setUpdateTime(date);
            return tag;
        }).collect(Collectors.toList());
        tagsService.saveBatch(newTags);
        List<Long> newIds = newTags.stream().map(Tag::getTagId).collect(Collectors.toList());
        tagMapper.insertBlogTag(blogUpdate.getBlogId(), newIds);
    }

    private void handleCategory(BlogUpdate blogUpdate) {
        String cate = (String) blogUpdate.getCate();
        if (StringUtils.isNumeric(cate)) {
            blogUpdate.setCategoryId(Long.valueOf(cate));
        } else {
            Category newCategory = new Category();
            newCategory.setCategoryName(cate);
            newCategory.setDescription("");
            Date date = new Date();
            newCategory.setCreateTime(date);
            newCategory.setUpdateTime(date);
            categoryMapper.insert(newCategory);
            blogUpdate.setCategoryId(newCategory.getCategoryId());
        }
    }

    private void getBlogInfoViews(PageResult<BlogInfo> pageResult) {
        pageResult.getDataList().forEach(blogInfo -> {
            Integer views = (Integer) redisService.getHash(RedisKeyConstant.BLOG_VIEWS,
                    blogInfo.getBlogId().toString());
            // redis里没有就去则去数据库查
            if (views == null) {
                LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(Blog::getViews).eq(Blog::getBlogId, blogInfo.getBlogId());
                Blog blog = blogMapper.selectOne(queryWrapper);
                views = blog.getViews();
                redisService.setHash(RedisKeyConstant.BLOG_VIEWS, blogInfo.getBlogId().toString(), views);
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
        Integer views = (Integer) redisService.getHash(RedisKeyConstant.BLOG_VIEWS, blogId.toString());
        if (views != null) {
            redisService.incrementHash(RedisKeyConstant.BLOG_VIEWS, blogId.toString(), 1);
            blogDetail.setViews(views + 1);
        } else {
            LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Blog::getViews).eq(Blog::getBlogId, blogId);
            Blog blog = blogMapper.selectOne(queryWrapper);
            views = blog.getViews() + 1;
            redisService.setHash(RedisKeyConstant.BLOG_VIEWS, blogId.toString(), views);
            blogDetail.setViews(views);
        }
    }
}
