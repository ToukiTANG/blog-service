package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.mapper.ExceptionLogMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.ExceptionLog;
import com.touki.blog.model.query.ExceptionLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.ExceptionLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {
    private final UserAgentUtil userAgentUtil;

    public ExceptionLogServiceImpl(UserAgentUtil userAgentUtil) {
        this.userAgentUtil = userAgentUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExceptionLog(ExceptionLog log) {
        String ipSource = IpAddressUtil.getCityInfo(log.getIp());
        UserAgentDTO userAgentDTO = userAgentUtil.parseOsAndBrowser(log.getUserAgent());
        log.setIpSource(ipSource);
        log.setOs(userAgentDTO.getOs());
        log.setBrowser(userAgentDTO.getBrowser());
        this.save(log);
    }

    @Override
    public PageResult<ExceptionLog> exceptionLogList(ExceptionLogQuery query) {
        String[] dates = StringUtils.split(query.getDate(), DelimiterConstant.COMMA);
        String startTime = null;
        String endTime = null;
        if (dates != null && dates.length == 2) {
            startTime = dates[0];
            endTime = dates[1];
        }

        Page<ExceptionLog> exceptionLogPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ExceptionLog> queryWrapper = new LambdaQueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between(ExceptionLog::getCreateTime, startTime, endTime);
        }
        queryWrapper.orderByDesc(ExceptionLog::getCreateTime);
        exceptionLogPage = this.page(exceptionLogPage, queryWrapper);

        PageResult<ExceptionLog> pageResult = new PageResult<>();
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal((int) exceptionLogPage.getTotal());
        pageResult.setDataList(exceptionLogPage.getRecords());
        return pageResult;
    }
}
