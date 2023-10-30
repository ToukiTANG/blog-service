package com.touki.blog.controller.admin;

import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.model.entity.CityVisitor;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.DashboardService;
import com.touki.blog.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final RedisService redisService;

    public DashboardController(DashboardService dashboardService, RedisService redisService) {
        this.dashboardService = dashboardService;
        this.redisService = redisService;
    }

    @GetMapping
    public Result dashboard() {
        int todayPv = dashboardService.todayVisitLog();
        int todayUv = redisService.countBySet(RedisKeyConstant.IDENTIFICATION_SET);
        int blogCount = dashboardService.blogCount();
        Map<String, List<?>> categoryMap = dashboardService.categoryBlogCountMap();
        Map<String, List<?>> tagMap = dashboardService.tagBlogCountMap();
        Map<String, List<?>> visitRecordMap = dashboardService.visitRecordMap();
        List<CityVisitor> cityVisitorList = dashboardService.cityVisitorList();

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("pv", todayPv);
        map.put("uv", todayUv);
        map.put("blogCount", blogCount);
        map.put("category", categoryMap);
        map.put("tag", tagMap);
        map.put("visitRecord", visitRecordMap);
        map.put("cityVisitor", cityVisitorList);
        return Result.data(map);
    }
}
