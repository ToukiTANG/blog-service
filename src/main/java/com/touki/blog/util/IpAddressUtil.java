package com.touki.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Touki
 */
@Slf4j
@Component
public class IpAddressUtil {
    private static Searcher searcher;
    @Value("${spring.profiles.active}")
    private String environment;

    @PostConstruct
    private void initIp2regionResource() throws Exception {

        if ("dev".equals(environment)) {
            searcher = Searcher.newWithFileOnly("src/main/resources/ipdb/ip2region.xdb");
        } else {
            searcher = Searcher.newWithFileOnly("/root/blog/ip2region.xdb");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.isEmpty() || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

        return ip;
    }

    public static String getCityInfo(String ip) {
        try {
            String region = searcher.search(ip);
            if (region == null) {
                return "未知属地";
            }
            if (!StringUtils.isEmpty(region)) {
                region = region.replace("|0", "");
                region = region.replace("0|", "");
            }
            return region;
        } catch (Exception e) {
            log.error("getCityInfo exception:", e);
        }
        return "未知属地";
    }

    public static void main(String[] args) throws Exception {
        IpAddressUtil ipAddressUtil = new IpAddressUtil();
        ipAddressUtil.initIp2regionResource();
        String cityInfo = IpAddressUtil.getCityInfo("110.191.13.235");
        System.out.println(cityInfo);
    }
}
