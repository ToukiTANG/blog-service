package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.VisitorMapper;
import com.touki.blog.model.dto.UserAgentDTO;
import com.touki.blog.model.entity.Visitor;
import com.touki.blog.service.VisitorService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.UserAgentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {
    private final UserAgentUtil userAgentUtil;

    public VisitorServiceImpl(UserAgentUtil userAgentUtil) {
        this.userAgentUtil = userAgentUtil;
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
}
