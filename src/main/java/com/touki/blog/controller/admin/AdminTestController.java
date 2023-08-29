package com.touki.blog.controller.admin;

import com.touki.blog.model.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/test")
public class AdminTestController {
    @GetMapping("/hello")
    public Result hello() {
        return Result.data("hello admin");
    }
}
