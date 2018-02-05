package com.alone.common.mybatis;

import com.alone.common.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 17-6-26 下午12:54
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceType value() default DataSourceType.WRITE;
}