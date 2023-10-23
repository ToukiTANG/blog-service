package com.touki.blog.util;

import com.touki.blog.model.dto.UserAgentDTO;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

/**
 * 接续浏览器和操作系统的工具类
 *
 * @author Touki
 */
@Component
public class UserAgentUtil {
    private final UserAgentAnalyzer uaa;

    public UserAgentUtil() {
        this.uaa = UserAgentAnalyzer
                .newBuilder()
                .useJava8CompatibleCaching()
                .withCache(10000)
                .hideMatcherLoadStats()
                .withField(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR)
                .withField(UserAgent.AGENT_NAME_VERSION)
                .build();
    }

    public UserAgentDTO parseOsAndBrowser(String userAgent) {
        UserAgent agent = uaa.parse(userAgent);
        String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR);
        String browser = agent.getValue(UserAgent.AGENT_NAME_VERSION);
        return new UserAgentDTO(os, browser);
    }
}
