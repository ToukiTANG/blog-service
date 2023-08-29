package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.RoleMapper;
import com.touki.blog.mapper.SysUserMapper;
import com.touki.blog.model.dto.AuthUser;
import com.touki.blog.model.entity.Role;
import com.touki.blog.model.entity.SysUser;
import com.touki.blog.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Touki
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService,
        UserDetailsService {
    private final SysUserMapper sysUserMapper;
    private final RoleMapper roleMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper, RoleMapper roleMapper) {
        this.sysUserMapper = sysUserMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new UsernameNotFoundException(String.format("用户%s不存在", username));
        }
        AuthUser authUser = new AuthUser();
        BeanUtils.copyProperties(sysUser, authUser);
        List<Role> roles = roleMapper.getRolesByUserId(sysUser.getUserId());
        authUser.setRoles(roles);
        return authUser;
    }
}
