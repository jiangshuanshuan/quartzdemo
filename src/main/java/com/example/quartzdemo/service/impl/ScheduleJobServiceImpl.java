package com.example.quartzdemo.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quartzdemo.common.result.Constant;
import com.example.quartzdemo.dao.ScheduleJobMapper;
import com.example.quartzdemo.domain.ScheduleJob;
import com.example.quartzdemo.schedule.CusSchedulerFactoryBean;
import com.example.quartzdemo.schedule.QuartzJobFactory;
import com.example.quartzdemo.service.ScheduleJobService;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
	private Logger log = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);

	@Autowired
	private ScheduleJobMapper scheduleJobMapper;
	// @Autowired
	// private CusSchedulerFactoryBean schedulerFactoryBean;

	private Scheduler scheduler;

	public ScheduleJobServiceImpl(CusSchedulerFactoryBean schedulerFactoryBean) {
		scheduler = schedulerFactoryBean.getScheduler();
	}

	/**
	 * 项目启动时初始化定时器
	 */
	@PostConstruct
	public void init() {
		// 获取所有的定时任务
		List<ScheduleJob> scheduleJobList = scheduleJobMapper.listAllJob();
		if (scheduleJobList.size() != 0) {
			for (ScheduleJob scheduleJob : scheduleJobList) {
				addJob(scheduleJob);
			}
		}
	}

	/**
	 * 添加任务
	 * 
	 * @param job
	 */
	private void addJob(ScheduleJob job) {
		try {
			log.info("初始化");
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 不存在，则创建
			if (null == trigger) {
				JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
				jobDetail.getJobDataMap().put("scheduleJob", job);

				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// withIdentity中写jobName和groupName
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
				// 如果定时任务是暂停状态
				if (job.getStatus() == Constant.STATUS_NOT_RUNNING) {
					pauseJob(job.getId());
				}
			} else {
				// Trigger已存在，那么更新相应的定时设置
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (Exception e) {
			log.error("添加任务失败", e);
		}
	}

	/**
	 * 查询所有的定时任务
	 * 
	 * @return BootstrapTableResult
	 */
	@Override
	public List<ScheduleJob> listAllJob() {
		// PageHelper.startPage(pageNumber, pageSize);
		List<ScheduleJob> scheduleJobList = scheduleJobMapper.listAllJob();
		// PageInfo pageInfo = new PageInfo(scheduleJobList, Constant.PAGENUMBER);
		// int total = (int) pageInfo.getTotal();
		// BootstrapTableResult bootstrapTableResult = new BootstrapTableResult(total,
		// scheduleJobList);
		return scheduleJobList;
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param jobId
	 */
	@Override
	public void pauseJob(int jobId) {
		// 修改定时任务状态
		ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);
		scheduleJob.setId(jobId);
		scheduleJob.setStatus(Constant.STATUS_NOT_RUNNING);
		scheduleJobMapper.updateJobStatusById(Constant.STATUS_NOT_RUNNING, jobId);
		try {
			// 暂停一个job
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.pauseJob(jobKey);
		} catch (Exception e) {
			log.error("CatchException:暂停任务失败", e);
		}
	}

	/**
	 * 恢复一个定时任务
	 * 
	 * @param jobId
	 */
	@Override
	public void resumeJob(int jobId) {
		// 修改定时任务状态
		ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);
		scheduleJob.setStatus(Constant.STATUS_RUNNING);
		scheduleJobMapper.updateJobStatusById(Constant.STATUS_RUNNING, jobId);
		try {
			// 恢复一个定时任务
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.resumeJob(jobKey);
		} catch (Exception e) {
			log.error("CatchException:恢复定时任务失败", e);
		}
	}

	/**
	 * 立即执行一个定时任务
	 * 
	 * @param jobId
	 */
	@Override
	public void runOnce(int jobId) {
		try {
			ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.triggerJob(jobKey);
		} catch (Exception e) {
			log.error("CatchException:恢复定时任务失败", e);
		}
	}

	/**
	 * 更新时间表达式
	 * 
	 * @param id
	 * @param cronExpression
	 */
	@Override
	public void updateCron(int id, String cronExpression) {
		ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(id);
		scheduleJob.setCronExpression(cronExpression);
		scheduleJobMapper.updateJobCronExpressionById(cronExpression, id);
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			log.error("CatchException:更新时间表达式失败", e);
		}

	}

	/**
	 * 通过主键id查找定时任务
	 * 
	 * @param id
	 * @return ScheduleJob
	 */
	private ScheduleJob getScheduleJobByPrimaryKey(int id) {
		return scheduleJobMapper.getScheduleJobByPrimaryKey(id);
	}

}
