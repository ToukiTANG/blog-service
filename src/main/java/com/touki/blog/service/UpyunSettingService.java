package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.UpyunSetting;

/**
 * @author Touki
 */
public interface UpyunSettingService extends IService<UpyunSetting> {
    /**
     * 查询又拍云设置
     *
     * @return UpyunSetting
     */
    UpyunSetting getUpyunSetting();

    /**
     * 更新又拍云配置
     *
     * @param setting UpyunSetting
     */
    void updateUpyunSetting(UpyunSetting setting);
}
