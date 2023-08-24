package com.touki.blog.controller;

import com.touki.blog.model.vo.Result;
import com.touki.blog.service.AboutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/about")
public class AboutController {
    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping
    public Result getAboutInfo() {
        Map<String, Object> map = aboutService.getAboutInfo();
        return Result.data(map);
    }
}
