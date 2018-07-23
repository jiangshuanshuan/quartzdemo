package com.example.quartzdemo.service;

import java.util.List;

import com.example.quartzdemo.domain.ScheduleJob;

public interface ScheduleJobService {
    /**
     * 查询所有的定时任务
     * @param pageSize
     * @param pageNumber
     * @return BootstrapTableResult
     */
	public List<ScheduleJob> listAllJob();

    /**
     * 暂停定时任务
     * @param jobId
     */
    void pauseJob(int jobId);

    /**
     * 恢复一个定时任务
     * @param jobId
     */
    void resumeJob(int jobId);

    /**
     * 立即执行一个定时任务
     * @param jobId
     */
    void runOnce(int jobId);

    /**
     * 更新时间表达式
     * @param id
     * @param cronExpression
     */
    void updateCron(int id, String cronExpression);

}
