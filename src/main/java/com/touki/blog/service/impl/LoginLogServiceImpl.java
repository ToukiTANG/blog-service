package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.mapper.LoginLogMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.LoginLog;
import com.touki.blog.model.query.LoginLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.LoginLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public PageResult<LoginLog> loginLogList(LoginLogQuery query) {
        String[] dates = StringUtils.split(query.getDate(), DelimiterConstant.COMMA);
        String startTime = null;
        String endTime = null;
        if (dates != null && dates.length == 2) {
            startTime = dates[0];
            endTime = dates[1];
        }
        Page<LoginLog> logPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between(LoginLog::getCreateTime, startTime, endTime);
        }
        queryWrapper.orderByDesc(LoginLog::getCreateTime);
        logPage = this.page(logPage, queryWrapper);

        PageResult<LoginLog> pageResult = new PageResult<>();
        pageResult.setTotal((int) logPage.getTotal());
        pageResult.setPageSize(query.getPageSize());
        pageResult.setDataList(logPage.getRecords());
        return pageResult;
    }
}
