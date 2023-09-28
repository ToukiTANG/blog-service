package com.touki.blog.util;

import cn.hutool.crypto.SecureUtil;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.constant.RespCode;
import com.touki.blog.exception.MyException;
import com.touki.blog.mapper.UpyunSettingMapper;
import com.touki.blog.model.entity.UpyunSetting;
import com.touki.blog.service.RedisService;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Touki
 */
@Component
public class UpyunUtil {
    private final UpyunSettingMapper upyunSettingMapper;
    private final RedisService redisService;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String API_DOMAIN = "https://v0.api.upyun.com";
    public static final String METHOD = "PUT";

    public UpyunUtil(UpyunSettingMapper upyunSettingMapper, RedisService redisService) {
        this.upyunSettingMapper = upyunSettingMapper;
        this.redisService = redisService;
    }


    public String upload(String filePath, String fileName, InputStream inputStream) throws IOException, MyException {

        UpyunSetting setting = getSetting();
        String uri = DelimiterConstant.FORWARD_SLASH + setting.getBucketName() + filePath + fileName;
        String url = API_DOMAIN + uri;
        String date = getDate();
        Request request = new Request.Builder()
                .put(create(null, inputStream))
                .url(url)
                .addHeader("Authorization", getAuthorization(setting.getUsername(), setting.getPassword(), uri, date))
                .addHeader("Date", date)
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return setting.getBaseUrl() + filePath + fileName;
            } else {
                throw new MyException(RespCode.SERVER_ERROR, "上传文件失败，请稍后重试！");
            }
        }
    }

    private UpyunSetting getSetting() {
        UpyunSetting setting = (UpyunSetting) redisService.getValue(RedisKeyConstant.UPYUN_SETTING);
        if (setting != null) {
            return setting;
        }
        setting = upyunSettingMapper.selectOne(null);
        redisService.setValue(RedisKeyConstant.UPYUN_SETTING, setting);
        return setting;
    }

    private String getAuthorization(String username, String password, String uri, String date) {
        String md5Password = SecureUtil.md5(password);
        String sp = "&";

        String sb = METHOD + sp + uri + sp + date;
        byte[] digest = SecureUtil.hmacSha1(md5Password).digest(sb);
        String signature = Base64.getEncoder().encodeToString(digest);
        return "UPYUN " + username + DelimiterConstant.COLON + signature;
    }

    public String getDate() {
        SimpleDateFormat formater = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formater.format(new Date());
    }

    private RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            public void writeTo(BufferedSink bufferedSink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    bufferedSink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
