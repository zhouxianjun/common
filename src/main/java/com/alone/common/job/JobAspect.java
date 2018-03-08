package com.alone.common.job;

import com.alone.dts.client.JobClient;
import com.alone.dts.thrift.struct.JobStruct;
import com.alone.dts.thrift.struct.PublicStructConstants;
import com.alone.common.GsonStatis;
import com.alone.common.outside.OutsideAspect;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import java.util.*;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description:
 * @date 2017/3/17 14:29
 */
@Slf4j
public class JobAspect extends OutsideAspect<JobStruct> {
    @Setter
    private JobClient jobClient;
    @Setter
    private String nodeGroup;
    @Setter
    private Integer maxRetryTimes;
    @Setter
    private String prefix = "SYSTEM-JOB-";
    @Override
    protected void fire(JoinPoint joinPoint, Set<JobStruct> values) {
        List<JobStruct> jobs = new ArrayList<>();
        for (JobStruct job : values) {
            try {
                jobs.add(job);
                log.info("添加任务: TaskId: {}, params: {}", job.getTaskId(), GsonStatis.instance().toJson(job.getParams()));
            } catch (Throwable e) {
                log.warn("添加任务异常", e);
            }
        }
        if (!jobs.isEmpty()) {
            jobClient.addJob(jobs);
        }
    }

    public void addJob(Collection<JobStruct> jobs) {
        for (JobStruct job : jobs) {
            addSomething(job);
        }
    }

    public void addJob(String action, Collection<Map<String, String>> params) {
        for (Map<String, String> param : params) {
            addJob(UUID.randomUUID().toString(), action, param);
        }
    }

    public void addJob(String taskId, String action, Map<String, String> params) {
        JobStruct job = new JobStruct();
        job.setTaskId(prefix + taskId);
        job.setParams(GsonStatis.instance().toJson(params));
        job.setAction(action);
        job.setNodeGroup(nodeGroup);
        if (maxRetryTimes != null) {
            job.setMaxRetryTimes(maxRetryTimes);
        }
        job.setType(PublicStructConstants.REAL_TIME);
        addSomething(job);
    }
}
