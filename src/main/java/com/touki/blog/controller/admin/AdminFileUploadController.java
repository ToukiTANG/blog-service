package com.touki.blog.controller.admin;

import com.touki.blog.exception.MyException;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.FileManageService;
import com.upyun.UpException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/file")
public class AdminFileUploadController {
    private final FileManageService fileManageService;

    public AdminFileUploadController(FileManageService fileManageService) {
        this.fileManageService = fileManageService;
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile image) throws MyException, UpException, IOException {
        String url = fileManageService.uploadFile(image);
        return Result.data(url);
    }
}
