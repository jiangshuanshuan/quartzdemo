package com.example.quartzdemo.schedule;

import org.quartz.spi.JobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class CusSchedulerFactoryBean extends SchedulerFactoryBean{

	public CusSchedulerFactoryBean(JobFactory jobFactory) {
		super();
	}
}
