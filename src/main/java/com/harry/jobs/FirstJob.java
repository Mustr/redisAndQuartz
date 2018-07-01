package com.harry.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstJob implements Job{
	private static Logger logger = LoggerFactory.getLogger(FirstJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("debug....FirstJob....");
		logger.warn("warn...FirstJob....");
		logger.info("info...FirstJob....");
		System.out.println("com.harry.jobs.FirstJob...execute");
		Date currDate = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("curr: " + format.format(currDate));
		logger.error("error....FirstJob....");
	}

}
