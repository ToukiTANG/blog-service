package com.touki.blog.util;

import com.touki.blog.constant.RespCode;
import com.touki.blog.exception.MyException;
import com.upyun.RestManager;
import com.upyun.UpException;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Touki
 */
public class UpyunUtil {
    public static final String BUCKET_NAME = "img-touki";
    public static final String USERNAME = "touki";
    public static final String PASSWORD = "CwxhOjuLd2EI0Kw6gVJzKGE8bkhY8k4Y";
    public static final String BASE_URL = "https://cdn.touki.top";
    private static final RestManager MANAGER = new RestManager(BUCKET_NAME, USERNAME, PASSWORD);

    public static String upload(String filePath, String fileName, InputStream inputStream) throws UpException,
            IOException, MyException {
        try (Response response = MANAGER.writeFile(filePath + fileName, inputStream, null)) {
            if (response.isSuccessful()) {
                return BASE_URL + filePath + fileName;
            } else {
                throw new MyException(RespCode.SERVER_ERROR, "上传文件失败，请稍后重试！");
            }
        }
    }
}
