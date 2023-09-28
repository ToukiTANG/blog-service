package com.touki.blog.aspect;

import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Touki
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class RemoveRedisCacheAspect {
    private final RedisService redisService;

    public RemoveRedisCacheAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    @Pointcut("@annotation(com.touki.blog.annotation.RemoveRedisCache)")
    private void pointcut() {
    }


    @Around("pointcut()")
    public Object aroundPointcut(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RemoveRedisCache annotation = method.getAnnotation(RemoveRedisCache.class);
        String keyPattern = annotation.keyPattern();
        String key = annotation.key();
        String[] keys = annotation.keys();
        try {
            Object proceed = pjp.proceed();
            if (!StringUtils.isBlank(keyPattern)) {
                redisService.removeKeyPattern(keyPattern);
                log.info("删除redis缓存成功，keyPattern：{}执行方法：{}", keyPattern,
                        signature.getDeclaringTypeName() + ":" + method.getName());
            }
            if (!StringUtils.isBlank(key)) {
                redisService.removeKey(key);
                log.info("删除redis缓存成功，key：{}执行方法：{}", key, signature.getDeclaringTypeName() + ":" + method.getName());
            }
            if (keys.length != 0) {
                redisService.removeKey(keys);
                log.info("删除redis缓存成功，keys：{}执行方法：{}", keys, signature.getDeclaringTypeName() + ":" + method.getName());
            }
            return proceed;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
