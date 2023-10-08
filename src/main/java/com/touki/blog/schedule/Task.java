package com.touki.blog.schedule;

import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Component
public class Task {

    private final RedisService redisService;
    private final BlogService blogService;

    public Task(RedisService redisService, BlogService blogService) {
        this.redisService = redisService;
        this.blogService = blogService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncRedisViews2Database() {
        Map<Object, Object> hashMap = redisService.getHashMap(RedisKeyConstant.BLOG_VIEWS);
        if (!hashMap.isEmpty()) {
            Set<Object> keySet = hashMap.keySet();
            List<Object> values = redisService.multiGet(RedisKeyConstant.BLOG_VIEWS, keySet);
            List<Long> ids = keySet.stream().map(o -> Long.valueOf((String) o)).collect(Collectors.toList());
            List<Integer> views = values.stream().map(v -> (Integer) v).collect(Collectors.toList());
            ArrayList<Blog> blogs = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                Blog blog = new Blog();
                blog.setBlogId(ids.get(i));
                blog.setViews(views.get(i));
                blogs.add(blog);
            }
            blogService.updateBatchById(blogs);
        }
    }
}
