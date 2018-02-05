package javax.lang.exception;

import com.alone.common.validate.ValidRule;
import lombok.NoArgsConstructor;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description: 由于dubbo的ExceptionFilter会重新包装所以这里放在javax包里面
 * @date 2016/9/28 15:40
 */
@NoArgsConstructor
public class ParamsMapValidationException extends RuntimeException {
    public ParamsMapValidationException(String message) {
        super(message);
    }

    public ParamsMapValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsMapValidationException(Throwable cause) {
        super(cause);
    }

    public ParamsMapValidationException(String field, String message) {
        super(String.format("字段:%s 验证失败,错误信息:%s", field, message));
    }

    public ParamsMapValidationException(String field, Object val, String message) {
        super(String.format("字段:%s,值:%s 验证失败,错误信息:%s", field, val, message));
    }

    public ParamsMapValidationException(String field, ValidRule rule) {
        super(String.format("字段:%s 验证失败,错误信息:%s,验证规则:%s", field, ((ValidRule.BasicRule)rule).getMessage(), rule.toString()));
    }

    public ParamsMapValidationException(String field, Object val, ValidRule rule) {
        super(String.format("字段:%s,值:%s 验证失败,错误信息:%s,验证规则:%s", field, val, ((ValidRule.BasicRule)rule).getMessage(), rule.toString()));
    }
}
