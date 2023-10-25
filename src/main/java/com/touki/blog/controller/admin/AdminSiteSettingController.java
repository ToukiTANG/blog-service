package com.touki.blog.controller.admin;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.model.dto.SiteSettingsUpdate;
import com.touki.blog.model.entity.SiteSetting;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.SiteSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/siteSettings")
public class AdminSiteSettingController {
    private final SiteSettingService siteSettingService;

    public AdminSiteSettingController(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @GetMapping
    public Result getSiteSettings() {
        Map<String, List<SiteSetting>> map = siteSettingService.getAdminList();
        return Result.data(map);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/update")
    @OperationLogger("更新网站信息")
    public Result updateSiteSettings(@RequestBody SiteSettingsUpdate siteSettingsUpdate) {
        siteSettingService.updateSiteSettings(siteSettingsUpdate);
        return Result.success();
    }
}
