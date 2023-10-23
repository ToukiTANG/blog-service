package com.touki.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getIpAddress exception:", e);
                }
                assert inet != null;
                ip = inet.getHostAddress();
            }
        }
        return StringUtils.substringBefore(ip, ",");
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
