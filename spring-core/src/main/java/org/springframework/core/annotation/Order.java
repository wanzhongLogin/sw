package org.springframework.core.annotation;

import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 用于标注组件的启动顺序
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Documented
@Inherited
public @interface Order {

    int value() default Ordered.LOWEST_PRECEDENCE;

}
