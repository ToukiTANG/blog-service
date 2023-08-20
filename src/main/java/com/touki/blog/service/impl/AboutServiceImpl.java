package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.entity.About;
import com.touki.blog.mapper.AboutMapper;
import com.touki.blog.service.AboutService;
import com.touki.blog.util.markdown.MarkdownUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Touki
 */
@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {
    /**
     * 查询about
     *
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    @Override
    public Map<String, Object> getAboutInfo() {
        List<About> aboutList = this.list();
        HashMap<String, Object> map = new HashMap<>(3);
        aboutList.forEach(about -> {
            if ("content".equals(about.getNameEn())) {
                map.put(about.getNameEn(), MarkdownUtil.markdownToHtmlExtensions(about.getValue()));

            } else {
                map.put(about.getNameEn(), about.getValue());
            }
        });
        return map;
    }
}
