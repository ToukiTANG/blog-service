package com.touki.blog.controller;

import com.touki.blog.annotation.VisitLogger;
import com.touki.blog.constant.RespCode;
import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.enums.VisitBehaviorEnum;
import com.touki.blog.exception.MyException;
import com.touki.blog.model.vo.*;
import com.touki.blog.service.BlogService;
import com.touki.blog.util.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/list")
    @VisitLogger(VisitBehaviorEnum.INDEX)
    public Result blogList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageResult<BlogInfo> blogInfoPage = blogService.getBlogInfos(pageNum, SiteDataConstant.BLOG_HOME_SIZE);
        return Result.data(blogInfoPage);
    }

    @GetMapping
    @VisitLogger(VisitBehaviorEnum.BLOG)
    public Result blogDetailById(@RequestParam Long blogId) {
        BlogDetail blogDetail = blogService.getBlogDetailById(blogId);
        return Result.data(blogDetail);
    }

    @GetMapping("/searchBlog")
    @VisitLogger(VisitBehaviorEnum.SEARCH)
    public Result searchBlog(String queryString) throws MyException {
        String trimStr = queryString.trim();
        int strLen = trimStr.length();
        if (strLen >= SiteDataConstant.SEARCH_QUERY_LEN_LIMIT) {
            throw new MyException(RespCode.PARAMETER_ERROR, "查询字符串太长了！");
        } else if (strLen == 0) {
            throw new MyException(RespCode.PARAMETER_ERROR, "查询字符串为空！");
        }
        if (StringUtil.hasSpecialChar(trimStr)) {
            throw new MyException(RespCode.PARAMETER_ERROR, "查询字符串不允许有特殊字符！");
        }
        List<SearchBlog> searchBlogs = blogService.searchBlog(queryString);
        return Result.data(searchBlogs);
    }
}
