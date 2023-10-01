package com.ehzyil.annotation;

import java.lang.annotation.*;

/**
 * @author ehyzil
 * @Description 访问日志注解
 * @create 2023-10-2023/10/1-20:37
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLogger {
    /**
     *
     * @return 访问页面
     */
    String value() default "";
}
