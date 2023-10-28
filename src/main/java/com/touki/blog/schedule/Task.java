package com.touki.blog.schedule;

import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.model.dto.VisitLogStatistic;
import com.touki.blog.model.dto.VisitorUpdateDTO;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.model.entity.CityVisitor;
import com.touki.blog.model.entity.VisitRecord;
import com.touki.blog.service.*;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Component
public class Task {

    private final RedisService redisService;
    private final BlogService blogService;
    private final VisitorLogService visitorLogService;
    private final VisitRecordService visitRecordService;
    private final VisitorService visitorService;
    private final CityVisitorService cityVisitorService;

    public Task(RedisService redisService, BlogService blogService, VisitorLogService visitorLogService,
                VisitRecordService visitRecordService, VisitorService visitorService,
                CityVisitorService cityVisitorService) {
        this.redisService = redisService;
        this.blogService = blogService;
        this.visitorLogService = visitorLogService;
        this.visitRecordService = visitRecordService;
        this.visitorService = visitorService;
        this.cityVisitorService = cityVisitorService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncRedisViews2Database() {
        Map<Object, Object> hashMap = redisService.getHashMap(RedisKeyConstant.BLOG_VIEWS);
        if (!hashMap.isEmpty()) {
            Set<Object> keySet = hashMap.keySet();
            List<Object> values = redisService.multiGet(RedisKeyConstant.BLOG_VIEWS, keySet);
            List<Long> ids = keySet.stream().map(o -> Long.valueOf((String) o)).collect(Collectors.toList());
            List<Integer> views = values.stream().map(v -> (Integer) v).collect(Collectors.toList());
            ArrayList<Blog> blogs = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                Blog blog = new Blog();
                blog.setBlogId(ids.get(i));
                blog.setViews(views.get(i));
                blogs.add(blog);
            }
            blogService.updateBatchById(blogs);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncVisitInfoToDatabase() {
        // 删除每天的访客缓存，以清理redis空间
        redisService.removeKey(RedisKeyConstant.IDENTIFICATION_SET);
        List<VisitLogStatistic> logStatistics = visitorLogService.visitLogStatisticYesterday();
        HashMap<String, Integer> pvMap = new HashMap<>(1024);
        HashMap<String, Date> lastTimeMap = new HashMap<>(1024);
        logStatistics.forEach(log -> {
            String uuid = log.getUuid();
            Date time = log.getTime();
            pvMap.merge(uuid, 1, Integer::sum);
            // sql中order desc了，所以第一个time就是最后访问时间
            lastTimeMap.putIfAbsent(uuid, time);
        });
        int pv = logStatistics.size();
        int uv = pvMap.size();
        // 获取昨天的日期字符串
        String date = new SimpleDateFormat("MM-dd").format(DateUtils.addDays(new Date(), -1));
        // 记录昨天的PV和UV
        visitRecordService.saveRecord(new VisitRecord(pv, uv, date));
        ArrayList<VisitorUpdateDTO> visitorList = new ArrayList<>();
        pvMap.forEach((uuid, pvNum) -> {
            visitorList.add(new VisitorUpdateDTO(uuid, lastTimeMap.get(uuid), pvNum));
        });
        // 更新访问记录到访客表
        visitorService.updateRecord(visitorList);
        ArrayList<CityVisitor> cityVisitors = getCityVisitors();
        cityVisitorService.saveOrUpdateVisitor(cityVisitors);
    }

    @NotNull
    private ArrayList<CityVisitor> getCityVisitors() {
        List<String> ipSorces = visitorService.getNewVisitorIpSourceYesterday();
        // 统计每日新增uv
        HashMap<String, Integer> cityVisitorMap = new HashMap<>(512);
        ipSorces.forEach(i -> {
            if (i.startsWith("中国")) {
                String[] split = i.split("\\|");
                if (split.length == 4) {
                    String city = split[2];
                    cityVisitorMap.merge(city, 1, Integer::sum);
                }
            }
        });
        // 更新城市新增访客UV数
        ArrayList<CityVisitor> cityVisitors = new ArrayList<>();
        cityVisitorMap.forEach((k, v) -> cityVisitors.add(new CityVisitor(k, v)));
        return cityVisitors;
    }
}
