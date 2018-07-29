package org.springframework.core.annotation;


import java.lang.annotation.*;

/**
 * 提供@AliasFor注解，来给注解的属性起别名，让使用注解时，更加的容易理解
 * @AliasFor is an annotation that is used to declared aliases for annotation attributes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AliasFor {

    /**
     * @AliasFor 在value 和 attribute 上面都这样写的目的,是value和attribute的属性作用一样
     *
     * 如果这样标记了,那么value和attribute必须成对出现,并且必须要有默认值
     *
     */


    @AliasFor("attribute")
    String value() default "";

    @AliasFor("value")
    String attribute() default "";

    /**
     * 作用于那个注解
     * 该注解必须继承自annotation
     */
    Class<? extends Annotation> annotation() default Annotation.class;

}
