package com.alone.common.validate;

import javax.lang.exception.ParamsMapValidationException;
import java.util.*;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description: 参数Map验证
 */
public class ParamsMapValidate {
    /**
     * 验证 ParamsMapValidate.validate(params, rules);
     * @param params 参数Map
     * @param rules 验证规则Map
     * @return
     * @throws ParamsMapValidationException
     */
    public static boolean validate(Map params, Map<String[], ValidRule[]> rules) throws ParamsMapValidationException {
        for (String[] key : rules.keySet()) {
            for (String k : key) {
                Object value = getValue(params, k);
                ValidRule[] vs = rules.get(key);
                for (ValidRule v : vs) {
                    ValidRule.BasicRule rule = (ValidRule.BasicRule) v;
                    if (value instanceof Collection) {
                        for (Object val : ((Collection) value)) {
                            rule.validate(val, k);
                        }
                    } else {
                        rule.validate(value, k);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 链式获取Map值
     * @param params map
     * @param key
     * @return
     */
    public static Object getValue(Map params, String key) {
        String[] keys = key.split("\\.");
        Object value = params;
        for (int i = 0; i < keys.length; i++) {
            String k = keys[i];

            if (value instanceof Collection) {
                value = getCollection(value, k);
                continue;
            }
            value = ((Map) value).get(k.replace("[]", ""));
            if (value == null && i == keys.length - 2) {
                value = new HashMap<>();
            }
        }
        return value;
    }

    private static Collection getCollection(Object value, String key) {
        List<Object> tmp = new ArrayList<>();
        for (Object o : ((Collection) value)) {
            if (o == null) {
                tmp.add(null);
                continue;
            }
            if (o instanceof Collection) {
                tmp.addAll(getCollection(o, key));
                continue;
            }
            tmp.add(((Map) o).get(key.replace("[]", "")));
        }
        return tmp;
    }
}
