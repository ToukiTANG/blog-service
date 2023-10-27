package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.mapper.VisitorLogMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.VisitLog;
import com.touki.blog.model.query.VisitLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.VisitorLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public PageResult<VisitLog> visitLogList(VisitLogQuery query) {

        String[] dates = StringUtils.split(query.getDate(), DelimiterConstant.COMMA);
        String startTime = null;
        String endTime = null;
        if (dates != null && dates.length == 2) {
            startTime = dates[0];
            endTime = dates[1];
        }

        Page<VisitLog> logPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<VisitLog> queryWrapper = new LambdaQueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between(VisitLog::getCreateTime, startTime, endTime);
        }
        String uuid = query.getUuid();
        if (!StringUtils.isBlank(uuid)) {
            queryWrapper.eq(VisitLog::getUuid, uuid);
        }
        queryWrapper.orderByDesc(VisitLog::getCreateTime);
        logPage = this.page(logPage, queryWrapper);

        PageResult<VisitLog> pageResult = new PageResult<>();
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal((int) logPage.getTotal());
        pageResult.setDataList(logPage.getRecords());
        return pageResult;
    }
}
