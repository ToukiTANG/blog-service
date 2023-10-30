package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.touki.blog.mapper.DashboardMapper;
import com.touki.blog.model.dto.CategoryBlogCountDTO;
import com.touki.blog.model.dto.TagBlogCountDTO;
import com.touki.blog.model.entity.*;
import com.touki.blog.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Service
public class DashboardServiceImpl implements DashboardService {
    // 查询最近30天的访客记录
    private static final Integer VISIT_RECORD_LIMIT_NUM = 30;
    private final DashboardMapper dashboardMapper;
    private final BlogService blogService;
    private final CategoryService categoryService;
    private final TagsService tagsService;
    private final VisitRecordService visitRecordService;
    private final CityVisitorService cityVisitorService;

    public DashboardServiceImpl(DashboardMapper dashboardMapper, BlogService blogService,
                                CategoryService categoryService, TagsService tagsService,
                                VisitRecordService visitRecordService, CityVisitorService cityVisitorService) {
        this.dashboardMapper = dashboardMapper;
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagsService = tagsService;
        this.visitRecordService = visitRecordService;
        this.cityVisitorService = cityVisitorService;
    }

    @Override
    public int todayVisitLog() {
        return dashboardMapper.todayVisitLog();
    }

    @Override
    public int blogCount() {
        return (int) blogService.count();
    }

    @Override
    public Map<String, List<?>> categoryBlogCountMap() {
        List<Blog> blogs = blogService.list();
        Map<Long, List<Blog>> blogCollect = blogs.stream().collect(Collectors.groupingBy(Blog::getCategoryId));
        List<Category> categories = categoryService.list();
        List<String> lengend = categories.stream().map(Category::getCategoryName).collect(Collectors.toList());
        List<CategoryBlogCountDTO> series = new ArrayList<>();
        categories.forEach(category -> {
            List<Blog> blogCount = blogCollect.get(category.getCategoryId());
            int value = CollectionUtils.isEmpty(blogCount) ? 0 : blogCount.size();
            CategoryBlogCountDTO dto = new CategoryBlogCountDTO(category.getCategoryName(), value);
            series.add(dto);
        });
        Map<String, List<?>> map = new HashMap<>(4);
        map.put("lengend", lengend);
        map.put("series", series);
        return map;
    }

    @Override
    public Map<String, List<?>> tagBlogCountMap() {
        List<Tag> tags = tagsService.list();
        List<String> lengend = tags.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<TagBlogCountDTO> series = tagsService.tagBlogCount();
        series.stream().filter(dto -> dto.getValue() == null).forEach(dto -> dto.setValue(0));
        Map<String, List<?>> map = new HashMap<>(4);
        map.put("lengend", lengend);
        map.put("series", series);
        return map;
    }

    @Override
    public Map<String, List<?>> visitRecordMap() {
        List<VisitRecord> visitRecordList = visitRecordService.visitRecordListLimit(VISIT_RECORD_LIMIT_NUM);
        List<String> date = new ArrayList<>(visitRecordList.size());
        List<Integer> pv = new ArrayList<>(visitRecordList.size());
        List<Integer> uv = new ArrayList<>(visitRecordList.size());
        for (int i = visitRecordList.size() - 1; i >= 0; i--) {
            VisitRecord visitRecord = visitRecordList.get(i);
            date.add(visitRecord.getDate());
            pv.add(visitRecord.getPv());
            uv.add(visitRecord.getUv());
        }
        Map<String, List<?>> map = new HashMap<>(8);
        map.put("date", date);
        map.put("pv", pv);
        map.put("uv", uv);
        return map;
    }

    @Override
    public List<CityVisitor> cityVisitorList() {
        LambdaQueryWrapper<CityVisitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(CityVisitor::getUv);
        return cityVisitorService.list(queryWrapper);
    }
}
