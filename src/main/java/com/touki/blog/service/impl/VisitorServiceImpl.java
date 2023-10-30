package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.mapper.VisitorMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.dto.VisitorUpdateDTO;
import com.touki.blog.model.entity.Visitor;
import com.touki.blog.model.query.VisitorQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.RedisService;
import com.touki.blog.service.VisitorService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Touki
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {
    private final UserAgentUtil userAgentUtil;
    private final RedisService redisService;

    public VisitorServiceImpl(UserAgentUtil userAgentUtil, RedisService redisService) {
        this.userAgentUtil = userAgentUtil;
        this.redisService = redisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVisitor(Visitor visitor) {
        String ipSource = IpAddressUtil.getCityInfo(visitor.getIp());
        UserAgentDTO userAgentDTO = userAgentUtil.parseOsAndBrowser(visitor.getUserAgent());
        visitor.setIpSource(ipSource);
        visitor.setOs(userAgentDTO.getOs());
        visitor.setBrowser(userAgentDTO.getBrowser());
        this.save(visitor);
    }

    @Override
    public boolean hasUUID(String identification) {
        return this.baseMapper.hasUUID(identification) != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecord(ArrayList<VisitorUpdateDTO> visitorList) {
        if (!CollectionUtils.isEmpty(visitorList)) {
            this.baseMapper.updateRecord(visitorList);
        }
    }

    @Override
    public List<String> getNewVisitorIpSourceYesterday() {
        return this.baseMapper.getNewVisitorIpSourceYesterday();
    }

    @Override
    public PageResult<Visitor> visitorList(VisitorQuery query) {
        String[] dates = StringUtils.split(query.getDate(), DelimiterConstant.COMMA);
        String startTime = null;
        String endTime = null;
        if (dates != null && dates.length == 2) {
            startTime = dates[0];
            endTime = dates[1];
        }
        Page<Visitor> visitorPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Visitor> queryWrapper = new LambdaQueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between(Visitor::getLastTime, startTime, endTime);
        }
        queryWrapper.orderByDesc(Visitor::getLastTime);
        visitorPage = this.page(visitorPage, queryWrapper);

        PageResult<Visitor> pageResult = new PageResult<>();
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal((int) visitorPage.getTotal());
        pageResult.setDataList(visitorPage.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVisitor(Long visitorId, String uuid) {
        redisService.deleteValuInSet(RedisKeyConstant.IDENTIFICATION_SET, uuid);
        this.removeById(visitorId);
    }
}
