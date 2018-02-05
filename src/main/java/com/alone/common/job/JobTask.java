package com.alone.common.job;

import com.alone.common.outside.Outside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description: 任务注解
 * @date 2017/1/19 9:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Outside
public @interface JobTask {
}
