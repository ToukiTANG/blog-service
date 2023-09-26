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

    /**
     * 后台获取about
     *
     * @return Map<String, Object>
     */
    Map<String, Object> getAdminAbout();

    /**
     * 更新about
     *
     * @param map 参数map
     */
    void updateAbout(Map<String, Object> map);
}
