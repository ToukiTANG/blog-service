package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.About;

import java.util.Map;

/**
 * @author Touki
 */
public interface AboutService extends IService<About> {
    /**
     * 查询about
     *
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    Map<String, Object> getAboutInfo();
}
