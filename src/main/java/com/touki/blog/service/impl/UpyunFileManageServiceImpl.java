package com.touki.blog.service.impl;

import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.exception.MyException;
import com.touki.blog.service.FileManageService;
import com.touki.blog.util.UpyunUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Touki
 */
@Service
public class UpyunFileManageServiceImpl implements FileManageService {
    private final UpyunUtil upyunUtil;
    public static final String BASE_PATH = "/blog/";

    public UpyunFileManageServiceImpl(UpyunUtil upyunUtil) {
        this.upyunUtil = upyunUtil;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException, MyException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + DelimiterConstant.DOT + extension;
        String dateFolder = new SimpleDateFormat("yy/MM-dd/").format(new Date());
        return upyunUtil.upload(BASE_PATH + dateFolder, fileName, file.getInputStream());
    }
}
