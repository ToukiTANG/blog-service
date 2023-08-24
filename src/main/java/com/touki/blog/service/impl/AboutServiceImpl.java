package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.AboutMapper;
import com.touki.blog.model.entity.About;
import com.touki.blog.service.AboutService;
import com.touki.blog.service.RedisService;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Touki
 */
@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {
    private final RedisService redisService;

    public AboutServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 查询about
     *
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    @Override
    public Map<String, Object> getAboutInfo() {
        String jsonString = redisService.getValue(RedisKeyConstant.ABOUT_INFO);
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
}
