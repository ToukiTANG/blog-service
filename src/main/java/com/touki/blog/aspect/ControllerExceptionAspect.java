package com.touki.blog.aspect;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.annotation.VisitLogger;
import com.touki.blog.model.entity.ExceptionLog;
import com.touki.blog.service.ExceptionLogService;
import com.touki.blog.util.AopUtil;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Touki
 */
@Aspect
@Component
public class ControllerExceptionAspect {
    private final ExceptionLogService exceptionLogService;

    public ControllerExceptionAspect(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @Pointcut("execution(* com.touki.blog.controller..*.*(..))")
    public void logPointcut() {
    }

    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
        ExceptionLog log = handleLog(joinPoint, e);
        exceptionLogService.saveExceptionLog(log);
    }

    private ExceptionLog handleLog(JoinPoint joinPoint, Exception e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String ip = IpAddressUtil.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String description = getDescriptionFromAnnotations(joinPoint);
        String error = StringUtils.substring(StringUtil.getStackTrace(e), 0, 2000);
        ExceptionLog log = new ExceptionLog(uri, method, description, error, ip, userAgent);
        Map<String, Object> requestParams = AopUtil.getRequestParams(joinPoint);
        log.setParam(StringUtils.substring(JsonUtil.writeValueAsString(requestParams), 0, 2000));
        return log;
    }

    private String getDescriptionFromAnnotations(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OperationLogger operationLogger = method.getAnnotation(OperationLogger.class);
        if (operationLogger != null) {
            return operationLogger.value();
        }
        VisitLogger visitLogger = method.getAnnotation(VisitLogger.class);
        if (visitLogger != null) {
            return visitLogger.value().getBehavior();
        }
        return "";
    }
}
