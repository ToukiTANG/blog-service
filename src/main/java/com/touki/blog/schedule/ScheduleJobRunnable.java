package com.touki.blog.schedule;

import com.touki.blog.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Touki
 */
public class ScheduleJobRunnable implements Runnable {
    private final Object target;
    private final Method method;
    private final String params;

    public ScheduleJobRunnable(String beanName, String methodName, String params) throws NoSuchMethodException {
        this.target = SpringContextUtil.getBean(beanName);
        if (StringUtils.isBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        }
        this.params = params;
    }

    @Override
    public void run() {
        ReflectionUtils.makeAccessible(method);
        try {
            if (StringUtils.isBlank(params)) {
                method.invoke(target);
            } else {
                method.invoke(target, params);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
