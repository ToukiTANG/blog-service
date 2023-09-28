package com.touki.blog.controller.admin;

import com.touki.blog.model.entity.UpyunSetting;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.UpyunSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/pic")
public class PictureSettingcontroller {

    private final UpyunSettingService upyunSettingService;

    public PictureSettingcontroller(UpyunSettingService upyunSettingService) {
        this.upyunSettingService = upyunSettingService;
    }

    @PreAuthorize("hasAnyRole('admin')")
    @GetMapping("/upyunSetting")
    public Result getUpyunSetting() {
        UpyunSetting upyunSetting = upyunSettingService.getUpyunSetting();
        return Result.data(upyunSetting);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/upyunSetting/update")
    public Result updateUpyunSetting(@RequestBody UpyunSetting setting) {
        upyunSettingService.updateUpyunSetting(setting);
        return Result.success();
    }
}
