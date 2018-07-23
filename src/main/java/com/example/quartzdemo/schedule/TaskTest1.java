package com.example.quartzdemo.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaskTest1 {
    public static final Logger LOGGER = LoggerFactory.getLogger(TaskTest1.class);

    public void run1(){
    	LOGGER.info("执行方法1");
    }

    public void run2(){
    	LOGGER.info("执行方法2");
    }

    public void run3(){
    	LOGGER.info("执行方法3");
    }

    public void run4(){
    	LOGGER.info("执行方法4");
    }

}
