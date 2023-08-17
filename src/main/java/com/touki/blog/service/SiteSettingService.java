package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.entity.SiteSetting;

import java.util.HashMap;

/**
 * @author Touki
 */
public interface SiteSettingService extends IService<SiteSetting> {
    /**
     * 获取所有的网站设置
     *
     * @return: java.util.List<com.touki.blog.entity.SiteSetting>
     */
    HashMap<String, Object> getSitSettings();
}
