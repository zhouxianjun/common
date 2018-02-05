package com.alone.common.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;

import javax.lang.exception.ParamsMapValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description:
 * @date 2016/9/28 15:35
 */
public class ValidRule {
    public static final String ID_CARD_15 = "^(\\d{18,18}|\\d{15,15}|\\d{17,17}x)$";
    public static final String ID_CARD_18 = "^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X)?$";
    public static final String MOBILE_PHONE = "^1\\d{10}$";

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotNull extends BasicRule {
        private String message = "不能为空";

        public boolean valid(Object val, String field) {
            return !isEmpty(val);
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pattern extends BasicRule {
        private String message = "不符合正则表达式";
        private String regexp;

        public Pattern(String regexp) {
            this.regexp = regexp;
        }

        public boolean valid(Object val, String field) {
            return isMatches(this.regexp, val.toString());
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdCard extends BasicRule {
        private String message = "请输入15或18位有效身份证号码";

        @Override
        protected boolean valid(Object val, String field) {
            return CheckIdCardUtils.validateCard(String.valueOf(val));
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Digits extends BasicRule {
        private String message = "必须是一个数字，其值必须在可接受的范围内";
        private Long min = Long.MIN_VALUE;
        private Long max = Long.MAX_VALUE;
        private Long[] include = null;

        public Digits(Long min, Long max) {
            this.min = min;
            this.max = max;
        }

        public Digits(Long[] include) {
            this.include = include;
        }

        @Override
        protected boolean valid(Object val, String field) {
            Number v = null;
            if (val instanceof Integer) {
                v = (Integer) val;
            } else if (val instanceof Long) {
                v = (Long) val;
            } else {
                try {
                    v = Long.valueOf(val.toString());
                } catch (Exception ignored) {
                    return false;
                }
            }
            return v.longValue() >= min && v.longValue() <= max && (include == null || ArrayUtils.indexOf(include, v.longValue()) > -1);
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Boolean extends BasicRule {
        private String message = "必须是一个布尔值";

        @Override
        protected boolean valid(Object val, String field) {
            if (val instanceof java.lang.Boolean) {
                return true;
            }
            String b = val.toString();
            return b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false");
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Date extends BasicRule {
        private String message = "不符合日期格式";
        private String format = "yyyy-MM-dd HH:mm:ss";

        public Date(String format) {
            this.format = format;
        }

        @Override
        protected boolean valid(Object val, String field) {
            if (val == null) return true;
            if (val instanceof java.util.Date) {
                return true;
            }
            try {
                new SimpleDateFormat(format).parse(val.toString());
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }

    private static boolean isMatches(String regexp, String val) {
        return java.util.regex.Pattern.compile(regexp).matcher(val).matches();
    }

    public static abstract class BasicRule extends ValidRule {
        public abstract String getMessage();

        protected abstract boolean valid(Object val, String field);

        protected boolean validate(Object val, String field) {
            if (!(this instanceof NotNull) && isEmpty(val)) return true;
            if (!valid(val, field)) {
                throw new ParamsMapValidationException(field, val, this);
            }
            return true;
        }
    }

    public static boolean isEmpty(Object val) {
        return val == null || val.toString().equals("");
    }
}
