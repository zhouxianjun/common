package com.alone.common.mybatis;

import com.alone.common.dto.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 17-6-27 上午11:51
 */
public interface PageMapper<T> {
    List<Map<String, Object>> listByPage(@Param("page") Page page, @Param("sortName") String sortName, @Param("sortDir") String sortDir);
}