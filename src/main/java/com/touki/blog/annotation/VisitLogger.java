package com.touki.blog.annotation;

import com.touki.blog.enums.VisitBehaviorEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Touki
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLogger {
    VisitBehaviorEnum value() default VisitBehaviorEnum.UNKNOWN;
}
