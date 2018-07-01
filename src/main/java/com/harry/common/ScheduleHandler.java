package com.harry.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 任务调度执行方法处理类
 */
public class ScheduleHandler implements Serializable{

	private static final long serialVersionUID = 1013090770461389630L;

	public void firstSchedule() {
		System.out.println("com.harry.common.ScheduleHandler....firstSchedule()");
		Date currDate = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(format.format(currDate));
	}
}
