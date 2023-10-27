package com.touki.blog.aspect;

import com.touki.blog.annotation.VisitLogger;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.constant.RespCode;
import com.touki.blog.enums.VisitBehaviorEnum;
import com.touki.blog.model.dto.VisitLogRemarkDTO;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.entity.VisitLog;
import com.touki.blog.model.entity.Visitor;
import com.touki.blog.model.vo.BlogDetail;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.*;
import com.touki.blog.util.AopUtil;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Touki
 */
@Aspect
@Component
@Slf4j
public class VisitLoggerAspect {
    private final RedisService redisService;
    private final VisitorService visitorService;
    private final VisitorLogService visitorLogService;
    private final CategoryService categoryService;
    private final TagsService tagsService;

    public VisitLoggerAspect(RedisService redisService, VisitorService visitorService,
                             VisitorLogService visitorLogService, CategoryService categoryService,
                             TagsService tagsService) {
        this.redisService = redisService;
        this.visitorService = visitorService;
        this.visitorLogService = visitorLogService;
        this.categoryService = categoryService;
        this.tagsService = tagsService;
    }

    @Pointcut("@annotation(com.touki.blog.annotation.VisitLogger)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundPointcut(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        long startTime = System.currentTimeMillis();
        Result result = (Result) pjp.proceed();
        long endTime = System.currentTimeMillis();
        int timeConsumed = (int) (endTime - startTime);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        // 校验访客标识码
        String identification = checkIdentification(attributes);
        VisitLog visitLog = handleLog(pjp, request, result, timeConsumed, identification, method);
        visitorLogService.saveLog(visitLog);
        return result;
    }

    private VisitLog handleLog(ProceedingJoinPoint pjp, HttpServletRequest request, Result result, int timeConsumed,
                               String identification, Method method) {
        String uri = request.getRequestURI();
        String requestMethod = request.getMethod();
        String ip = IpAddressUtil.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> paramMap = AopUtil.getRequestParams(pjp);
        VisitLogger visitLogger = method.getAnnotation(VisitLogger.class);
        VisitLogRemarkDTO visitLogRemark = judgeBehavior(visitLogger.value(), paramMap, result);
        VisitLog log = new VisitLog(identification, uri, requestMethod, visitLogger.value().getBehavior(),
                visitLogRemark.getContent(), visitLogRemark.getRemark(), ip, timeConsumed, userAgent);
        log.setParam(StringUtils.substring(JsonUtil.writeValueAsString(paramMap), 0, 2000));
        return log;
    }

    /**
     * 根据访问行为设置备注
     *
     * @param behavior VisitBehaviorEnum
     * @param paramMap 参数map
     * @param result   接口访问结果
     * @return VisitLogRemarkDTO
     */
    private VisitLogRemarkDTO judgeBehavior(VisitBehaviorEnum behavior, Map<String, Object> paramMap, Result result) {
        String remark = "";
        String content = behavior.getContent();

        switch (behavior) {
            case INDEX:
                remark = "第" + paramMap.get("pageNum") + "页";
                break;
            case MOMENT:
                remark = "第" + paramMap.get("pageNum") + "页";
                break;
            case BLOG:
                if (Objects.equals(result.getCode(), RespCode.SUCCESS)) {
                    BlogDetail blog = (BlogDetail) result.getData();
                    String title = blog.getTitle();
                    content = title;
                    remark = "文章标题：" + title;
                }
                break;
            case SEARCH:
                if (Objects.equals(result.getCode(), RespCode.SUCCESS)) {
                    String query = (String) paramMap.get("queryString");
                    content = query;
                    remark = "搜索内容：" + query;
                }
                break;
            case CATEGORY:
                if (Objects.equals(result.getCode(), RespCode.SUCCESS)) {
                    Long categoryId = (Long) paramMap.get("categoryId");
                    Category category = categoryService.getById(categoryId);
                    content = category.getCategoryName();
                    remark = "分类名称：" + content + "，第" + paramMap.get("pageNum") + "页";
                }
                break;
            case TAG:
                if (Objects.equals(result.getCode(), RespCode.SUCCESS)) {
                    Long tagId = (Long) paramMap.get("tagId");
                    Tag tag = tagsService.getById(tagId);
                    content = tag.getTagName();
                    remark = "标签名称：" + content + "，第" + paramMap.get("pageNum") + "页";
                }
                break;
        }
        return new VisitLogRemarkDTO(content, remark);
    }

    private String checkIdentification(ServletRequestAttributes attributes) {
        HttpServletRequest request = attributes.getRequest();
        String identification = request.getHeader("identification");
        if (identification == null) {
            identification = saveUUID(attributes);
        } else {
            boolean redisHas = redisService.hasValueInSet(RedisKeyConstant.IDENTIFICATION_SET, identification);
            // Redis中不存在uuid，TODO 可以考虑定时任务清理访客set，防止oom
            if (!redisHas) {
                // 校验数据库中是否存在uuid
                boolean mysqlHas = visitorService.hasUUID(identification);
                if (mysqlHas) {
                    redisService.hasValueInSet(RedisKeyConstant.IDENTIFICATION_SET, identification);
                } else {
                    saveUUID(attributes);
                }
            }
        }
        return identification;
    }

    private String saveUUID(ServletRequestAttributes attributes) {
        HttpServletResponse response = attributes.getResponse();
        HttpServletRequest request = attributes.getRequest();
        // 获取当前小时开始的时间，每个小时限制访问
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String timestamp = Long.toString(calendar.getTimeInMillis() / 1000);
        // 获取访问信息
        String ip = IpAddressUtil.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        // 根据时间戳、ip、userAgent生成UUID
        String nameUUID = timestamp + ip + userAgent;
        String uuid = UUID.nameUUIDFromBytes(nameUUID.getBytes()).toString();// 添加访客标识码UUID至响应头
        assert response != null;
        response.addHeader("identification", uuid);
        // 暴露自定义header供页面资源使用
        response.addHeader("Access-Control-Expose-Headers", "identification");
        boolean redisHas = redisService.hasValueInSet(RedisKeyConstant.IDENTIFICATION_SET, uuid);

        if (!redisHas) {
            // 保存至Redis
            redisService.saveValueToSet(RedisKeyConstant.IDENTIFICATION_SET, uuid);
            // 保存至数据库
            Visitor visitor = new Visitor();
            visitor.setUuid(uuid);
            visitor.setIp(ip);
            visitor.setUserAgent(userAgent);
            Date date = new Date();
            visitor.setCreateTime(date);
            visitor.setLastTime(date);
            visitor.setPv(0);
            visitorService.saveVisitor(visitor);
        }
        return uuid;
    }
}
