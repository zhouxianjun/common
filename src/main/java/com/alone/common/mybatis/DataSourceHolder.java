package com.alone.common.mybatis;

import com.alone.common.enums.DataSourceType;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 17-6-26 下午12:54
 */
public class DataSourceHolder {
    private static final ThreadLocal<DataSourceType> holder = new ThreadLocal<>();

    public static void changeDs(DataSourceType ds) {
        holder.set(ds);
    }

    public static DataSourceType getDataSourceName() {
        return holder.get();
    }
}