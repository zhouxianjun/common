package com.alone.common.mvc;

import com.alone.common.GsonStatis;
import com.alone.common.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
public abstract class BaseController {
    public static String JSON_TYPE = "application/json;charset=utf-8";
    public static String ERROR_RESULT_TYPE = "ERROR_RESULT_TYPE";
    public static String ERROR_RESULT_JSON = "JSON";
    public static String ERROR_RESULT_JSONP = "JSONP";

    @ExceptionHandler
    public void exception(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        log.error(ex.getMessage(), ex);
        Result result = validationException(ex.getClass().getName(), ex);
        Object type = getResultType(request);
        writeData(request, response, transformResult(result, request), (String) type);
    }

    protected Object transformResult(Result result, HttpServletRequest request) {
        return result;
    }

    protected Object getResultType(HttpServletRequest request) {
        Object type = request.getAttribute(ERROR_RESULT_TYPE);
        if (type == null) {
            type = request.getParameter(ERROR_RESULT_TYPE);
        }
        return type;
    }

    public static void writer(Object obj, String jsoncallback, HttpServletResponse response) throws Exception {
        log.debug("返回信息:{}", GsonStatis.instance().toJson(obj));
        if (StringUtils.isBlank(jsoncallback)) {
            writeJSON(obj, response);
            return;
        }
        writeJSONP(obj, jsoncallback, response);
    }

    protected void writeData(HttpServletRequest request, HttpServletResponse response, Object result, String type) throws Exception {
        if (type == null) {
            type = ERROR_RESULT_JSON;
        }
        if (ERROR_RESULT_JSON.equalsIgnoreCase(type)) {
            writeJSON(result, response);
        } else if (ERROR_RESULT_JSONP.equalsIgnoreCase(type)) {
            String jsoncallback = request.getParameter("jsoncallback");
            if (jsoncallback == null) {
                jsoncallback = "jsonp";
            }
            writeJSONP(result, jsoncallback, response);
        } else {
            request.setAttribute(ERROR_RESULT_TYPE, result);
            request.getRequestDispatcher(type).forward(request, response);
        }
    }

    public ServletContext getServletContext(HttpServletRequest request) {
        return request.getSession().getServletContext();
    }

    public static void writeJSONP(Object obj, String jsoncallback, HttpServletResponse response) throws Exception {
        writer(jsoncallback + "(" + GsonStatis.instance().toJson(obj) + ")", response, JSON_TYPE);
    }

    private static void writer(String string,
                               HttpServletResponse response, String type) throws IOException, Exception {
        response.setContentType(type);
        PrintWriter writer = response.getWriter();
        writer.print(string);
        writer.flush();
        writer.close();
    }

    public void writer(byte[] data,
                       HttpServletResponse response) throws IOException, Exception {
        response.setContentType("application/x-download");
        response.setContentLength(data.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    public static void writeJSON(Object obj, HttpServletResponse response) throws Exception {
        writer(GsonStatis.instance().toJson(obj), response, JSON_TYPE);
    }

    protected String getPath(HttpServletRequest request, String path) {
        return request.getSession().getServletContext().getRealPath("/") + File.separator + (path == null ? "" : path) + File.separator;
    }

    /**
     * 获取访问者IP
     * <p>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    protected String getIpAddr(HttpServletRequest request) {
        String remoteIp = request.getHeader("x-forwarded-for");
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("X-Real-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }
        if (remoteIp == null || remoteIp.isEmpty()
                || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteHost();
        }
        return remoteIp;
    }

    public HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    protected Result validationException(String className, Exception ex) {
        Result result = Result.fail(Result.PARAM_FAIL);
        result.setCode(Result.PARAM_FAIL);
        if ("org.springframework.web.bind.MissingServletRequestParameterException".equalsIgnoreCase(className)) {
            MissingServletRequestParameterException msrpe = (MissingServletRequestParameterException) ex;
            result.put("name", msrpe.getParameterName());
            result.put("type", msrpe.getParameterType());
            result.setMsg(msrpe.getMessage());
        } else if ("org.springframework.validation.BindException".equalsIgnoreCase(className)) {
            BindException bindException = (BindException) ex;
            List<FieldError> list = bindException.getFieldErrors();
            Set<Map> errors = new HashSet<>();
            for (FieldError fieldError : list) {
                errors.add(new HashMap<String, Object>() {{
                    put("className", bindException.getFieldType(fieldError.getField()).getName());
                    put("errorMsg", fieldError.getDefaultMessage());
                    put("name", fieldError.getField());
                    put("value", bindException.getFieldValue(fieldError.getField()));
                }});
            }
            result.put("validation", errors);
        }
        return result;
    }
}