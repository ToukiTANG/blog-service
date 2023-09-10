package com.touki.blog.schedule;

import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.RedisService;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    public void syncRedisViews2Database() {
        // TODO 批量同步views
        Map<Object, Object> hashMap = redisService.getHashMap(RedisKeyConstant.BLOG_VIEWS);

    }
}
