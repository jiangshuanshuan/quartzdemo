package com.example.quartzdemo.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.quartzdemo.common.utils.TaskUtils;
import com.example.quartzdemo.domain.ScheduleJob;
import com.google.gson.Gson;

@Component
public class QuartzJobFactory implements Job {
    public Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        log.info("scheduleJob---"+new Gson().toJson(scheduleJob));
        TaskUtils.invokeMethod(scheduleJob);
    }
}

