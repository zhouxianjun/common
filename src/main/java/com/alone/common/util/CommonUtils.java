package com.alone.common.util;

import com.xiaoleilu.hutool.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class CommonUtils {

    public static Map<String, Object> obj2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod) || field.getName().startsWith("__")) {
                continue;
            }
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }
    public static <T> T map2Obj(Map<String,Object> map, Class<T> clz) {
        try {
            Object obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for(Field field : declaredFields){
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                Object value = map.get(field.getName());
                if (value != null) {
                    if (field.getType().equals(Date.class)) {
                        field.set(obj, DateUtil.parseDateTime(value.toString()));
                        continue;
                    }
                    if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                        field.set(obj, Integer.parseInt(value.toString()));
                        continue;
                    }
                    if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                        field.set(obj, Boolean.valueOf(value.toString()));
                        continue;
                    }
                }
                field.set(obj, value);
            }
            return (T) obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String mapToQuery(Map<String, Object> map, String seq, String eq, boolean encode, String enc) throws UnsupportedEncodingException {
        if (map == null || map.isEmpty()) return "";
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof List) {
                for (Object o : (List) value) {
                    addValue(list, key, o, eq, encode, enc);
                }
            } else {
                addValue(list, key, value, eq, encode, enc);
            }
        }
        return StringUtils.join(list.toArray(), seq == null ? "&" : seq);
    }

    public static Map<String, Object> queryToMap(String query) {
        return queryToMap(query, null, null, false, null);
    }

    public static Map<String, Object> queryToMap(String query, String seq, String eq, boolean decode, String enc) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(query)) return map;
        String[] qs = query.split(seq == null ? "&" : seq);
        eq = eq == null ? "=" : eq;
        for (String q : qs) {
            q = q.replaceAll("\\+", "%20");
            int index = q.indexOf(eq);
            String key = index >= 0 ? q.substring(0, index) : q;
            String value = index >= 0 ? q.substring(index + eq.length()) : "";
            if (decode) {
                try {
                    key = URLDecoder.decode(key, enc);
                    value = URLDecoder.decode(value, enc);
                } catch (UnsupportedEncodingException e) {
                    log.warn("queryToMap decode error", e);
                }
            }
            if (!map.containsKey(key)) {
                map.put(key, value);
            } else {
                Object val = map.get(key);
                if (val instanceof List) {
                    ((List) val).add(value);
                } else {
                    List<String> values = new ArrayList<>();
                    values.add(val.toString());
                    values.add(value);
                    map.put(key, values);
                }
            }
        }
        return map;
    }

    public static Set<Map<String, Object>> urlParse(List<String> urls) {
        Set<String> urlSet = new HashSet<>(urls);
        Set<Map<String, Object>> result = new HashSet<>();
        for (String url : urlSet) {
            result.add(queryToMap(url));
        }
        return result;
    }

    public static String sortSignKey(Map<String, Object> params) {
        SortedMap<String, Object> parameters = new TreeMap<>();
        Set<Map.Entry<String, Object>> es = params.entrySet();

        for (Map.Entry<String, Object> entry : es) {
            parameters.put(entry.getKey(), entry.getValue());
        }

        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        return sb.toString();
    }

    private static void addValue(List<String> list, String key, Object value, String eq, boolean encode, String enc) throws UnsupportedEncodingException {
        list.add(key + (eq == null ? "=" : eq) + (value == null ? "" : encode ? URLEncoder.encode(value.toString(), enc) : value.toString()));
    }
}
