package com.alone.common.event;

import com.alone.dts.client.JobClient;
import com.alone.dts.thrift.struct.JobStruct;
import com.alone.dts.thrift.struct.PublicStructConstants;
import com.alone.common.GsonStatis;
import com.alone.common.outside.OutsideAspect;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.AmqpTemplate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description:
 * @date 2017/1/19 10:26
 */
@Slf4j
public class MqEventAspect extends OutsideAspect<Object> {
    @Setter
    private AmqpTemplate amqpTemplate;
    @Setter
    private JobClient jobClient;

    @Override
    protected void fire(JoinPoint joinPoint, Set<Object> values) {
        MqEvent mnsEvent = hasEvent(joinPoint);
        final String queue = mnsEvent.queue();
        for (final Object o : values) {
            final String json = GsonStatis.instance().toJson(o);
            try {
                log.info("event fire queue: {} msg: {}", queue, json);
                amqpTemplate.convertAndSend(queue, o);
            } catch (Throwable e) {
                log.error("event fire error queue: {} msg: {}", queue, json, e);
                addFailJob(queue, o);
                throw new RuntimeException(e);
            }
        }
    }

    private void addFailJob(String queue, Object content) {
        if (jobClient != null) {
            JobStruct job = new JobStruct();
            job.setTaskId(String.format("EVENT-FIRE-JOB-%s-%s", queue, UUID.randomUUID().toString()));
            job.setAction("FIRE_EVENT");
            job.setParams(GsonStatis.instance().toJson(new HashMap<String, Object>(){{
                put("queue", queue);
                put("content", content);
            }}));
            job.setType(PublicStructConstants.REAL_TIME);
            jobClient.addJob(job);
            log.info("触发事件异常, 创建任务:{}", job.getTaskId());
        }
    }

    private MqEvent hasEvent(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return method.getAnnotation(MqEvent.class);
    }

    public <T> void addEvent(T value) {
        addSomething(value);
    }
}
