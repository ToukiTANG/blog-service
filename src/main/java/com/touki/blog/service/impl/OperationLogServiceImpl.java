package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.mapper.OperationLogMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.OperationLog;
import com.touki.blog.model.query.OperationLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.OperationLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    private final UserAgentUtil userAgentUtil;

    public OperationLogServiceImpl(UserAgentUtil userAgentUtil) {
        this.userAgentUtil = userAgentUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLog(OperationLog operationLog) {
        String ipSource = IpAddressUtil.getCityInfo(operationLog.getIp());
        UserAgentDTO userAgentDTO = userAgentUtil.parseOsAndBrowser(operationLog.getUserAgent());
        operationLog.setIpSource(ipSource);
        operationLog.setOs(userAgentDTO.getOs());
        operationLog.setBrowser(userAgentDTO.getBrowser());
        this.save(operationLog);
    }

    @Override
    public PageResult<OperationLog> getLogList(OperationLogQuery query) {
        String[] dates = StringUtils.split(query.getDate(), DelimiterConstant.COMMA);
        String startTime = null;
        String endTime = null;
        if (dates != null && dates.length == 2) {
            startTime = dates[0];
            endTime = dates[1];
        }

        Page<OperationLog> operationLogPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between(OperationLog::getCreateTime, startTime, endTime);
        }
        queryWrapper.orderByDesc(OperationLog::getCreateTime);
        operationLogPage = this.page(operationLogPage, queryWrapper);

        PageResult<OperationLog> pageResult = new PageResult<>();
        pageResult.setTotal((int) operationLogPage.getTotal());
        pageResult.setPageSize(query.getPageSize());
        pageResult.setDataList(operationLogPage.getRecords());
        return pageResult;
    }
}
