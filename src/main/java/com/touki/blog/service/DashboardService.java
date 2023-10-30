package com.touki.blog.service;

import com.touki.blog.model.entity.CityVisitor;

import java.util.List;
import java.util.Map;

/**
 * @author Touki
 */
public interface DashboardService {
    /**
     * 今日浏览量
     *
     * @return int
     */
    int todayVisitLog();

    /**
     * 查询文章数量
     *
     * @return int
     */
    int blogCount();

    /**
     * 查询分类下的文章数量
     *
     * @return Map<String, List < ?>>
     */
    Map<String, List<?>> categoryBlogCountMap();

    /**
     * 查询分类下文章数量
     *
     * @return Map<String, List < ?>>
     */
    Map<String, List<?>> tagBlogCountMap();

    /**
     * 查询访客记录
     *
     * @return Map<String, List < ?>>
     */
    Map<String, List<?>> visitRecordMap();

    /**
     * 城市访客数据统计
     *
     * @return List<CityVisitor>
     */
    List<CityVisitor> cityVisitorList();
}
