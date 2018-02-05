package com.alone.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alone
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<E> implements Serializable {
    private static final long serialVersionUID = 816334571679000487L;
    /**当前操作是否成功*/
    private boolean success;
    /**返回文本信息*/
    private String msg;
    /**返回文本信息*/
    private int code;
    /**附加信息*/
    private Map<String, Object> data = new HashMap<>();
    private E value;

    public final static int SUCCESS = 200;
    public final static int FAIL = 0;
    public final static int PARAM_FAIL = 400;

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.success = isSuccess();
    }

    public Result(int code) {
        this.code = code;
        this.msg = "操作成功";
        this.success = isSuccess();
    }

    public boolean isSuccess() {
        return code == SUCCESS;
    }

    private void setSuccess(boolean success) {}

    public Result<E> put(String key, Object value){
        getData().put(key, value);
        return this;
    }

    public Result<E> putAll(Map<String, Object> data){
        getData().putAll(data);
        return this;
    }

    public Result<E> value(E value) {
        this.value = value;
        return this;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public <T> T get(String key){
        return (T) getData().get(key);
    }

    public static Result ok() {
        return new Result(SUCCESS);
    }

    public static Result ok(String msg) {
        return new Result(SUCCESS, msg);
    }

    public static Result fail() {
        return new Result(FAIL, "操作失败");
    }
    public static Result fail(int code) {
        return new Result(code, "操作失败");
    }
    public static Result fail(String msg) {
        return new Result(FAIL, msg);
    }
    public static Result fail(int code, String msg) {
        return new Result(code, msg);
    }
}