package com.touki.blog.controller.admin;

import com.touki.blog.model.vo.Result;
import com.touki.blog.service.AboutService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/about")
public class AdminAboutController {
    private final AboutService aboutService;

    public AdminAboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping
    public Result getAbout() {
        Map<String, Object> map = aboutService.getAdminAbout();
        return Result.data(map);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/update")
    public Result updateAbout(@RequestBody Map<String, Object> map) {
        aboutService.updateAbout(map);
        return Result.success();
    }
}
