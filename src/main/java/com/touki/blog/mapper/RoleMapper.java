package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过userId查询角色集合
     *
     * @param userId 用户id
     * @return List<Role>
     */
    List<Role> getRolesByUserId(Long userId);
}