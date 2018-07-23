package com.example.quartzdemo.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ScheduleJob {
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 任务名
	 */
	private String jobName;
	/**
	 * 任务组
	 */
	private String jobGroup;
	/**
	 * 要执行的方法的名称
	 */
	private String methodName;
	/**
	 * 要执行的方法所在的class路径
	 */
	private String beanClass;
	/**
	 * 定时任务状态，0表示正常，1表示停止
	 */
	private Integer status;
	/**
	 * 时间表达式
	 */
	private String cronExpression;
	/**
	 * 参数
	 */
	private String params;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date modifyTime;

}
