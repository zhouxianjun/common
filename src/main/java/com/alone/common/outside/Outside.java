package com.alone.common.outside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description: 外围注解
 * @date 2017/1/19 9:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Outside {
    boolean clear() default false; // 是否清空之前的
    boolean extend() default true; // 是否继承之前的事件
}
