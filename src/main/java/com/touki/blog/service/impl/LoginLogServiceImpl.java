package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.LoginLogMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.LoginLog;
import com.touki.blog.service.LoginLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
    private final UserAgentUtil userAgentUtil;

    public LoginLogServiceImpl(UserAgentUtil userAgentUtil) {
        this.userAgentUtil = userAgentUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(LoginLog loginLog) {
        String ipSource = IpAddressUtil.getCityInfo(loginLog.getIp());
        UserAgentDTO userAgentDTO = userAgentUtil.parseOsAndBrowser(loginLog.getUserAgent());
        loginLog.setIpSource(ipSource);
        loginLog.setOs(userAgentDTO.getOs());
        loginLog.setBrowser(userAgentDTO.getBrowser());
        this.save(loginLog);
    }
}
