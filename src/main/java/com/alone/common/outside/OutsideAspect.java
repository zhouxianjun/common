package com.alone.common.outside;

import com.alone.common.GsonStatis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description:
 * @date 2017/3/17 9:57
 */
@Slf4j
public abstract class OutsideAspect<T> {
    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private final ThreadLocal<Set<T>> threadLocalData = new ThreadLocal<>();
    private final ThreadLocal<Boolean> rollback = new ThreadLocal<>();
    private final ThreadLocal<Boolean> remove = new ThreadLocal<>();

    public void before(JoinPoint joinPoint) {
        String signature = joinPoint.getSignature().toLongString();
        Outside outside = hasOutside(joinPoint);
        if (outside == null) {
            log.warn("当前注解没有被Outside注解.{}", signature);
            return;
        }
        remove.set(false);

        log.debug("进入Outside: {}", signature);
        // 继承: 当前线程不存在或者当前线程存在并且不继承则创建否则继承
        if (threadLocal.get() == null || (threadLocal.get() != null && !outside.extend())) {
            threadLocal.set(signature);
            log.info("创建Outside: {}", threadLocal.get());
        } else {
            log.info("继承Outside: {}", threadLocal.get());
        }

        if (outside.clear() && !outside.extend()) {
            log.debug("清空Outside: {}", signature);
            threadLocalData.remove();
        }
    }

    public void afterReturn(JoinPoint joinPoint) {
        String signature = joinPoint.getSignature().toLongString();
        Outside outside = hasOutside(joinPoint);
        if (outside == null) {
            log.warn("当前注解没有被Outside注解.{}", signature);
            return;
        }

        if (threadLocal.get() != null && !threadLocal.get().equals(signature)) {
            log.debug("当前函数:{} 不是Outside结束函数:{}", signature, threadLocal.get());
            return;
        }

        log.info("触发Outside:{} 开始", signature);
        try {
            if (isRollback()) {
                log.info("Outside回滚");
            } else {
                log.info("Outside fire: {}", GsonStatis.instance().toJson(current()));
                // 这里是异步操作
                fire(joinPoint, current());
            }
        } catch (Throwable e) {
            log.warn("触发Outside:{} 异常", signature, e);
        } finally {
            remove();
        }
        log.info("触发Outside:{} 结束", signature);
    }

    public void afterThrowing(JoinPoint joinPoint) {
        String signature = joinPoint.getSignature().toLongString();
        Outside outside = hasOutside(joinPoint);
        if (outside == null) {
            log.warn("当前注解没有被Outside注解.{}", signature);
            return;
        }
        // 这里会调用多次，所以做个标记
        // 例如: A函数 -> B函数, A、B函数都被代理, B函数抛出异常, 则本方法会被B触发后又被A触发
        if (!BooleanUtils.toBoolean(remove.get())) {
            log.warn("异常删除Outside: {}-{}", signature, threadLocal.get());
            remove.set(true);
        }
        remove();
    }

    private Outside hasOutside(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Annotation[] annotations = method.getAnnotations();
        if (annotations == null) return null;
        for (Annotation annotation : annotations) {
            Outside outside = annotation.annotationType().getAnnotation(Outside.class);
            if (outside != null) return outside;
        }
        return null;
    }

    private Set<T> current() {
        Set<T> contents = threadLocalData.get();
        if (contents == null) {
            contents = new LinkedHashSet<>();
            threadLocalData.set(contents);
        }
        return contents;
    }

    public void rollback() {
        rollback.set(true);
    }

    protected void addSomething(T value) {
        current().add(value);
    }

    private void remove() {
        threadLocalData.remove();
        rollback.remove();
        threadLocal.remove();
    }

    private boolean isRollback() {return BooleanUtils.toBoolean(rollback.get());}

    protected abstract void fire(JoinPoint joinPoint, Set<T> values);
}
