package com.touki.blog.service;

import com.touki.blog.exception.MyException;
import com.upyun.UpException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Touki
 */
public interface FileManageService {
    /**
     * 上传文件
     *
     * @param file MultipartFile
     * @return url
     */
    String uploadFile(MultipartFile file) throws IOException, MyException, UpException;
}
