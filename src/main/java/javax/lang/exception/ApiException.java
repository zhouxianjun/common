package javax.lang.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * api业务异常。
 * 由于dubbo的ExceptionFilter会重新包装所以这里放在javax包里面
 */
public class ApiException extends RuntimeException {
    
    private static final long serialVersionUID = 1099436364231429639L;
    
    /**
     * 错误代码
     */
    private int code = 0;
    
    /**
     * 错误描述
     */
	private String msg;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Object data;

	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
		this.msg = message;
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(int code, String msg) {
		super(msg);
		this.setCode(code);
		this.msg = msg;
	}

	public ApiException(int code, String msg, Object data) {
		super(msg);
		this.setCode(code);
		this.msg = msg;
        this.data = data;
	}

	public ApiException(String msg, Object data) {
		super(msg);
		this.msg = msg;
        this.data = data;
	}

	public String getMsg() {
		return this.msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

    public ApiException dataMap() {
        if (data == null || !(data instanceof Map)) {
            data = new HashMap<>();
        }
        return this;
    }

    public ApiException putData(String key, Object value) {
        if (data != null && data instanceof Map) {
            ((Map) data).put(key, value);
        }
        return this;
    }
}
