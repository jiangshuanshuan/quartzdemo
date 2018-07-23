package com.example.quartzdemo.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quartzdemo.common.result.BaseResult;
import com.example.quartzdemo.domain.ScheduleJob;
import com.example.quartzdemo.service.ScheduleJobService;

@RestController
@RequestMapping("/scheduleJob")
public class ScheduleJobController {
	public static Logger log = LoggerFactory.getLogger(ScheduleJobController.class);

	@Resource
	private ScheduleJobService scheduleJobService;

	/**
	 * 查询所有的定时任务，用于页面加载时显示表格数据
	 * 
	 * @param pageSize
	 *            每页显示数量
	 * @param pageNumber
	 *            页数
	 * @return BootstrapTableResult
	 */
	@RequestMapping(value = "/listAllJob", method = RequestMethod.POST)
	public List<ScheduleJob> listAllJob() {
		List<ScheduleJob> bootstrapTableResult = scheduleJobService.listAllJob();
		return bootstrapTableResult;
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param jobId
	 * @return BaseResult
	 */
	@RequestMapping(value = "/pauseJob", method = RequestMethod.POST)
	public BaseResult pauseJob(@RequestParam(value="id")int jobId) {
		scheduleJobService.pauseJob(jobId);
		return new BaseResult(1, "success", "定时任务暂停成功");
	}

	/**
	 * 恢复定时任务
	 * 
	 * @param jobId
	 * @return BaseResult
	 */
	@RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
	public BaseResult resumeJob(@RequestParam(value="id")int jobId) {
		scheduleJobService.resumeJob(jobId);
		return new BaseResult(1, "success", "定时任务恢复成功");
	}

	/**
	 * 立即执行定时任务
	 * 
	 * @param jobId
	 * @return BaseResult
	 */
	@RequestMapping(value = "/runOnce", method = RequestMethod.POST)
	public BaseResult runOnce(@RequestParam(value="id")int jobId) {
		scheduleJobService.runOnce(jobId);
		return new BaseResult(1, "success", "立即执行定时任务成功");
	}

	/**
	 * 更新时间表达式
	 * 
	 * @param id
	 * @param cronExpression
	 * @return BaseResult
	 */
	@RequestMapping(value = "/updateCron", method = RequestMethod.POST)
	public BaseResult updateCron(@RequestParam(value="id")int id, @RequestParam(value="cronExpression")String cronExpression) {
		scheduleJobService.updateCron(id, cronExpression);
		return new BaseResult(1, "success", "更新时间表达式成功");
	}
}
