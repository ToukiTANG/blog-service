package com.touki.blog.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
public class AopUtil {
    /**
     * 获取接口参数
     *
     * @param joinPoint JoinPoint
     * @return Map<String, Object>
     */
    public static Map<String, Object> getRequestParams(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        List<String> paramList = Arrays.asList(parameterNames);
        Object[] args = joinPoint.getArgs();
        List<Object> argList = Arrays.asList(args);
        return paramList.stream().collect(Collectors.toMap(p -> p,
                p -> argList.get(paramList.indexOf(p))));
    }
}
