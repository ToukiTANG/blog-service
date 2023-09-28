package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.UpyunSettingMapper;
import com.touki.blog.model.entity.UpyunSetting;
import com.touki.blog.service.RedisService;
import com.touki.blog.service.UpyunSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class UpyunSettingServiceImpl extends ServiceImpl<UpyunSettingMapper, UpyunSetting> implements UpyunSettingService {
    private final RedisService redisService;

    public UpyunSettingServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public UpyunSetting getUpyunSetting() {
        UpyunSetting setting = (UpyunSetting) redisService.getValue(RedisKeyConstant.UPYUN_SETTING);
        if (setting != null) {
            return setting;
        }
        setting = this.getOne(null);
        redisService.setValue(RedisKeyConstant.UPYUN_SETTING, setting);
        return setting;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RemoveRedisCache(key = RedisKeyConstant.UPYUN_SETTING)
    public void updateUpyunSetting(UpyunSetting setting) {
        LambdaQueryWrapper<UpyunSetting> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UpyunSetting::getBucketName, setting.getBucketName());
        this.update(setting, queryWrapper);
    }
}
