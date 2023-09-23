package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.constant.SiteSettingName;
import com.touki.blog.mapper.SiteSettingMapper;
import com.touki.blog.model.dto.SiteSettingsUpdate;
import com.touki.blog.model.entity.SiteSetting;
import com.touki.blog.model.vo.Copyright;
import com.touki.blog.model.vo.Favorite;
import com.touki.blog.model.vo.Filing;
import com.touki.blog.model.vo.Introduction;
import com.touki.blog.service.SiteSettingService;
import com.touki.blog.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Touki
 */
@Service
public class SiteSettingServiceImpl extends ServiceImpl<SiteSettingMapper, SiteSetting> implements SiteSettingService {
    /**
     * 获取所有的网站设置
     *
     * @return: java.util.List<com.touki.blog.entity.SiteSetting>
     */
    @Override
    public HashMap<String, Object> getSitSettings() {
        // 存放网站信息
        HashMap<String, Object> siteInfo = new HashMap<>(8);
        List<SiteSetting> siteSettings = this.list(null);
        Introduction introduction = new Introduction();
        ArrayList<Favorite> favorites = new ArrayList<>();
        siteSettings.forEach(siteSetting -> {
            switch (siteSetting.getType()) {
                case 1:
                    // 传播许可
                    if (SiteSettingName.COPYRIGHT.equals(siteSetting.getNameEn())) {
                        Copyright copyright = JsonUtil.readValue(siteSetting.getValue(), Copyright.class);
                        siteInfo.put(siteSetting.getNameEn(), copyright);
                        // 备案信息
                    } else if (SiteSettingName.FILING.equals(siteSetting.getNameEn())) {
                        Filing filing = JsonUtil.readValue(siteSetting.getValue(), Filing.class);
                        siteInfo.put(siteSetting.getNameEn(), filing);
                        // 网页标题后缀
                    } else {
                        siteInfo.put(siteSetting.getNameEn(), siteSetting.getValue());
                    }
                    break;
                case 2:
                    switch (siteSetting.getNameEn()) {
                        case SiteSettingName.AVATAR:
                            introduction.setAvatar(siteSetting.getValue());
                            break;
                        case SiteSettingName.NICKNAME:
                            introduction.setNickname(siteSetting.getValue());
                            break;
                        case SiteSettingName.BILIBILI:
                            introduction.setBilibili(siteSetting.getValue());
                            break;
                        case SiteSettingName.DESCRIPTION:
                            introduction.setDescription(siteSetting.getValue());
                            break;
                        case SiteSettingName.GITHUB:
                            introduction.setGithub(siteSetting.getValue());
                            break;
                        case SiteSettingName.NETEASE:
                            introduction.setNetease(siteSetting.getValue());
                            break;
                        case SiteSettingName.FAVORITES:
                            Favorite favorite = JsonUtil.readValue(siteSetting.getValue(), Favorite.class);
                            favorites.add(favorite);
                            break;
                    }
            }
        });
        introduction.setFavorites(favorites);
        HashMap<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("introduction", introduction);
        resultMap.put("siteInfo", siteInfo);
        return resultMap;
    }

    @Override
    public Map<String, List<SiteSetting>> getAdminList() {
        List<SiteSetting> siteSettings = this.list();
        List<SiteSetting> type1 = new ArrayList<>();
        List<SiteSetting> type2 = new ArrayList<>();
        for (SiteSetting s : siteSettings) {
            switch (s.getType()) {
                case 1:
                    type1.add(s);
                    break;
                case 2:
                    type2.add(s);
                    break;
                default:
                    break;
            }
        }
        Map<String, List<SiteSetting>> map = new HashMap<>(8);
        map.put("type1", type1);
        map.put("type2", type2);
        return map;
    }

    @Override
    @RemoveRedisCache(key = RedisKeyConstant.SITE_SETTING_INFO)
    @Transactional(rollbackFor = Exception.class)
    public void updateSiteSettings(SiteSettingsUpdate siteSettingsUpdate) {
        List<Long> deleteIds = siteSettingsUpdate.getDeleteIds();
        this.removeBatchByIds(deleteIds);
        List<SiteSetting> settings = siteSettingsUpdate.getSettings();
        this.saveOrUpdateBatch(settings);
    }
}
