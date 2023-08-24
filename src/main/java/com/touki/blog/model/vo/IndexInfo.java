package com.touki.blog.model.vo;

import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
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
