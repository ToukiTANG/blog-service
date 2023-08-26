package com.touki.blog.service.impl;

import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.service.RedisService;
import com.touki.blog.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author Touki
 */
@Service
public class RedisClientDetailsServiceImpl extends JdbcClientDetailsService {
    private final RedisService redisService;

    public RedisClientDetailsServiceImpl(DataSource dataSource, RedisService redisService) {
        super(dataSource);
        this.redisService = redisService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        String jsonString = redisService.getHash(RedisKeyConstant.CLIENT_DETAILS, clientId);
        if (!StringUtils.isBlank(jsonString)) {
            return JsonUtil.readValue(jsonString, BaseClientDetails.class);
        }
        ClientDetails clientDetails = super.loadClientByClientId(clientId);
        redisService.setHash(RedisKeyConstant.CLIENT_DETAILS, clientId, JsonUtil.writeValueAsString(clientDetails));
        return clientDetails;
    }
}
