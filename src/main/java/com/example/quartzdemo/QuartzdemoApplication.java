package com.example.quartzdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * springboot整合Quartz实现动态配置定时任务，1个Task多个方法
 * @author Administrator
 *
 */
@SpringBootApplication
public class QuartzdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzdemoApplication.class, args);
	}
}
