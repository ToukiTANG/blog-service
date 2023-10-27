package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.VisitorLogMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.VisitLog;
import com.touki.blog.service.VisitorLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class VisitorLogServiceImpl extends ServiceImpl<VisitorLogMapper, VisitLog> implements VisitorLogService {
    private final UserAgentUtil userAgentUtil;

    public VisitorLogServiceImpl(UserAgentUtil userAgentUtil) {
        this.userAgentUtil = userAgentUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(VisitLog visitLog) {
        String ipSource = IpAddressUtil.getCityInfo(visitLog.getIp());
        UserAgentDTO userAgentDTO = userAgentUtil.parseOsAndBrowser(visitLog.getUserAgent());

        visitLog.setIpSource(ipSource);
        visitLog.setOs(userAgentDTO.getOs());
        visitLog.setBrowser(userAgentDTO.getBrowser());
        this.save(visitLog);
    }
}
