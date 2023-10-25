package com.touki.blog.aspect;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.model.entity.OperationLog;
import com.touki.blog.service.OperationLogService;
import com.touki.blog.util.IpAddressUtil;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.JwtUtil;
import io.jsonwebtoken.Claims;
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Aspect
@Component
@Slf4j
public class OperationLoggerAspect {
    private final OperationLogService operationLogService;

    public OperationLoggerAspect(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @Pointcut("@annotation(com.touki.blog.annotation.OperationLogger)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundPointcut(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object proceed = pjp.proceed();
        long endTime = System.currentTimeMillis();
        int timeConsumed = (int) (endTime - startTime);
        OperationLog operationLog = handleLog(pjp);
        operationLog.setCreateTime(new Date());
        operationLog.setTimeConsumed(timeConsumed);
        operationLogService.saveLog(operationLog);
        return proceed;
    }

    private OperationLog handleLog(ProceedingJoinPoint pjp) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");
        String token = StringUtils.split(authorization, DelimiterConstant.SPACE)[1];
        Claims claims = JwtUtil.parseJwt(token);
        String username = claims.getSubject();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String ip = IpAddressUtil.getIpAddress(request);

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        OperationLogger operationLogger = method.getAnnotation(OperationLogger.class);
        String description = operationLogger.value();
        String[] parameterNames = signature.getParameterNames();
        List<String> paramList = Arrays.asList(parameterNames);
        Object[] args = pjp.getArgs();
        List<Object> argList = Arrays.asList(args);
        Map<String, Object> map = paramList.stream().collect(Collectors.toMap(p -> p,
                p -> argList.get(paramList.indexOf(p))));

        OperationLog operationLog = new OperationLog();
        operationLog.setUserAgent(userAgent);
        operationLog.setUsername(username);
        operationLog.setDescription(description);
        operationLog.setParam(StringUtils.substring(JsonUtil.writeValueAsString(map), 0, 2000));
        operationLog.setUri(uri);
        operationLog.setIp(ip);
        operationLog.setMethod(request.getMethod());
        return operationLog;
    }
}
