package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.dto.SiteSettingsUpdate;
import com.touki.blog.model.entity.SiteSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 后台站点设置
     *
     * @return Map<String, List < SiteSetting>>
     */
    Map<String, List<SiteSetting>> getAdminList();

    /**
     * 更新站点设置
     *
     * @param siteSettingsUpdate SiteSettingsUpdate
     */
    void updateSiteSettings(SiteSettingsUpdate siteSettingsUpdate);
}
