package com.alone.common.enums;

import java.io.Serializable;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 17-6-26 下午12:52
 */
public interface BasicEnum extends Serializable {
    /**
     * 获取枚举值
     * @return
     */
    int getVal();
    String getName();
}