package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.AboutMapper;
import com.touki.blog.model.entity.About;
import com.touki.blog.service.AboutService;
import com.touki.blog.service.RedisService;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Touki
 */
@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {
    private final RedisService redisService;
    private final AboutMapper aboutMapper;

    public AboutServiceImpl(RedisService redisService, AboutMapper aboutMapper) {
        this.redisService = redisService;
        this.aboutMapper = aboutMapper;
    }

    /**
     * 查询about
     *
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    @Override
    public Map<String, Object> getAboutInfo() {
        String jsonString = (String) redisService.getValue(RedisKeyConstant.ABOUT_INFO);
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        }
        List<About> aboutList = this.list();
        HashMap<String, Object> map = new HashMap<>(3);
        aboutList.forEach(about -> {
            if ("content".equals(about.getNameEn())) {
                map.put(about.getNameEn(), MarkdownUtil.markdownToHtmlExtensions(about.getValue()));

            } else {
                map.put(about.getNameEn(), about.getValue());
            }
        });
        redisService.setValue(RedisKeyConstant.ABOUT_INFO, JsonUtil.writeValueAsString(map));
        return map;
    }

    @Override
    public Map<String, Object> getAdminAbout() {
        List<About> aboutList = this.list();
        HashMap<String, Object> map = new HashMap<>(3);
        aboutList.forEach(about -> map.put(about.getNameEn(), about.getValue()));
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RemoveRedisCache(key = RedisKeyConstant.ABOUT_INFO)
    public void updateAbout(Map<String, Object> map) {
        Set<String> keys = map.keySet();
        keys.forEach(key -> aboutMapper.updateAbout(key, map.get(key)));
    }
}
