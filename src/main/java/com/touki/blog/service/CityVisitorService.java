package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.CityVisitor;

import java.util.ArrayList;

/**
 * @author Touki
 */
public interface CityVisitorService extends IService<CityVisitor> {
    /**
     * 新增或更新城市uv
     *
     * @param cityVisitors ArrayList<CityVisitor>
     */
    void saveOrUpdateVisitor(ArrayList<CityVisitor> cityVisitors);
}
