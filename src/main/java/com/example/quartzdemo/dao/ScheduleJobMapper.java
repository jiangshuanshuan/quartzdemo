package com.example.quartzdemo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quartzdemo.domain.ScheduleJob;

@Repository
public interface ScheduleJobMapper extends JpaRepository<ScheduleJob, Integer>{
    /**
     * 查询所有的定时任务
     * @return List<ScheduleJob>
     */
	@Query(value = "select * from schedule_job", nativeQuery = true)
    List<ScheduleJob> listAllJob();

    /**
     * 更新定时任务状态
     * @param scheduleJob
     */
	@Query(value = "update schedule_job set status=?1 where id=?2 ", nativeQuery = true)  
	@Modifying
	@Transactional
    void updateJobStatusById(int status,int id);

    /**
     * 根据主键查询定时任务
     * @param id
     * @return ScheduleJob
     */
    @Query(value = "select * from schedule_job where id=?1", nativeQuery = true)
    ScheduleJob getScheduleJobByPrimaryKey(int id);

    /**
     * 更新时间表达式
     * @param scheduleJob
     */
    @Query(value = "update schedule_job set cron_expression=?1 where id=?2 ", nativeQuery = true)  
	@Modifying
	@Transactional
    void updateJobCronExpressionById(String cron_expression,int id);

}
