package com.touki.blog.entity.vo;

import com.touki.blog.entity.Category;
import com.touki.blog.entity.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author Touki
 */
@Data
public class IndexInfo {
    private Object siteInfo;
    private Object introduction;
    private List<NewBlog> newBlogList;
    private List<Category> categoryList;
    private List<Tag> tagList;
    private List<RandomBlog> randomBlogList;
}
