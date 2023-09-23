package com.touki.blog.model.dto;

import com.touki.blog.model.entity.SiteSetting;
import lombok.Data;

import java.util.List;

/**
 * @author Touki
 */
@Data
public class SiteSettingsUpdate {
    private List<Long> deleteIds;
    private List<SiteSetting> settings;
}
