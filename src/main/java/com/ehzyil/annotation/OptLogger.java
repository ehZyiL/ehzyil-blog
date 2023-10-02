package com.ehzyil.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解

 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptLogger {

    /**
     * @return 操作描述
     */
    String value() default "";

}
