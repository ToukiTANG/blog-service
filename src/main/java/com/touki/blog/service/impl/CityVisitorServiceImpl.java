package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.CityVisitorMapper;
import com.touki.blog.model.entity.CityVisitor;
import com.touki.blog.service.CityVisitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * @author Touki
 */
@Service
public class CityVisitorServiceImpl extends ServiceImpl<CityVisitorMapper, CityVisitor> implements CityVisitorService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateVisitor(ArrayList<CityVisitor> cityVisitors) {
        this.saveOrUpdateBatch(cityVisitors);
    }
}
