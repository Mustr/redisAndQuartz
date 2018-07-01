package com.harry.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import com.harry.common.ServicesBase;
import com.harry.services.SchedulerService;

@Service
public class SchedulerServiceImpl extends ServicesBase implements SchedulerService {

	private Scheduler scheduler;

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public JobDetail getJobDetail(String jobName, String group) {
		if (StringUtils.isNotBlank(jobName)) {
			try {
				return scheduler.getJobDetail(new JobKey(jobName, group));
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("获取任务名【%s】，组名【%s】调度失败",jobName,group);
			}
		}
		return null;
	}

	public boolean exectJob(String jobName, String group) {
		if (StringUtils.isNotBlank(jobName)) {
			try {
				scheduler.triggerJob(new JobKey(jobName, group));
				return true;
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("执行【%s】,组名【%s】失败！",jobName, group);
			}
		}
		return false;
	}

	public boolean getTriggerIsRun(String triggerName, String group) {
		if (StringUtils.isNotBlank(triggerName)) {
			try {
				TriggerState triggerState = scheduler.getTriggerState(new TriggerKey(triggerName,group));
				if (triggerState == TriggerState.NORMAL) {
					return true;
				} 
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean deleteJob(String jobName, String group) {
		if (StringUtils.isNotBlank(jobName)) {
			try {
				return scheduler.deleteJob(new JobKey(jobName, group));
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("删除【%s】，组【%s】失败！", jobName, group);
			}
		}
		return false;
	}

	public boolean pauseJob(String jobName, String group) {
		if (StringUtils.isNotBlank(jobName)) {
			try {
				scheduler.pauseJob(new JobKey(jobName, group));
				return true;
			} catch (SchedulerException e) {
				e.printStackTrace();
				
			}
		}
		return false;
	}

	public boolean resumeJob(String jobName, String group) {
		if (StringUtils.isNotBlank(jobName)) {
			try {
				scheduler.resumeJob(new JobKey(jobName, group));
				return true;
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("继续任务【%s】，组【%s】失败！", jobName, group);
			}
		}
		return false;
	}
	
}
