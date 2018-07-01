package com.harry.services;

import org.quartz.JobDetail;

/**
 * 任务调度业务接口
 */
public interface SchedulerService {

	/**
	 * 获取job
	 * @param jobName
	 * @param group
	 * @return
	 */
	public JobDetail getJobDetail(String jobName, String group);
	
	/**
	 * 执行调度
	 * @param name
	 * @param group
	 * @return
	 */
	public boolean exectJob(String jobName, String group);
	
	/**
	 * 判断指定的触发器的是否正在执行
	 * @param triggerName
	 * @param group
	 * @return
	 */
	public boolean getTriggerIsRun(String triggerName, String group); 
	
	/**
	 * 删除任务
	 */
	public boolean deleteJob(String jobName, String group);
	
	/**
	 * 暂停任务
	 * @param jobName
	 * @param group
	 * @return
	 */
	public boolean pauseJob(String jobName, String group);
	
	/**
	 * 继续任务
	 * @param jobName
	 * @param group
	 * @return
	 */
	public boolean resumeJob(String jobName, String group);
}
