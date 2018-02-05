package com.alone.common.mybatis;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description:
 * @date 2018/1/24 9:51
 */
@Data
public class SqlWhere implements Serializable {
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String EQUAL = "=";
    public static final String LIKE_BEFORE = "BEFORE";
    public static final String LIKE_AFTER = "AFTER";
    public static final String LIKE = "like";
    public static final String GT = ">";
    public static final String LT = "<";
    public static final String NO_EQUAL = "<>";
    public static final String IN = "in";
    public static final String NOT_IN = "not in";
    private String field;
    private String compare;
    private String way;
    private Object value;
}
