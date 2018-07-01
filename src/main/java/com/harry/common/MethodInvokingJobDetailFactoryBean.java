package com.harry.common;

import java.util.Arrays;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.MethodInvoker;


/**
 * 生成之定义的JobDetail对象
 * @see #afterPropertiesSet()
 */
public class MethodInvokingJobDetailFactoryBean implements FactoryBean<JobDetail>, BeanNameAware, InitializingBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The JobDetail produced by the <code>afterPropertiesSet</code> method of
	 * this Class will be assigned to the Group specified by this property.
	 * Default: Scheduler.DEFAULT_GROUP
	 * 
	 * @see #afterPropertiesSet()
	 * @see Scheduler#DEFAULT_GROUP
	 */
	private String group = Scheduler.DEFAULT_GROUP;


	/**
	 * Used to set the JobDetail.durable property. Default: false
	 * <p>
	 * Durability - if a job is non-durable, it is automatically deleted from
	 * the scheduler once there are no longer any active triggers associated
	 * with it.
	 * 
	 * @see <a
	 *      href="http://www.opensymphony.com/quartz/wikidocs/TutorialLesson3.html">http://www.opensymphony.com/quartz/wikidocs/TutorialLesson3.html</a>
	 * @see #afterPropertiesSet()
	 */
	private boolean durable = false;


	/**
	 * Used by <code>afterPropertiesSet</code> to set the
	 * JobDetail.requestsRecovery property. Default: false<BR>
	 * <p>
	 * RequestsRecovery - if a job "requests recovery", and it is executing
	 * during the time of a 'hard shutdown' of the scheduler (i.e. the process
	 * it is running within crashes, or the machine is shut off), then it is
	 * re-executed when the scheduler is started again. In this case, the
	 * JobExecutionContext.isRecovering() method will return true.
	 * 
	 * @see <a
	 *      href="http://www.opensymphony.com/quartz/wikidocs/TutorialLesson3.html">http://www.opensymphony.com/quartz/wikidocs/TutorialLesson3.html</a>
	 * @see #afterPropertiesSet()
	 */
	private boolean shouldRecover = false;

	/**
	 * The name assigned to this bean in the Spring ApplicationContext. Used by
	 * <code>afterPropertiesSet</code> to set the JobDetail.name property.
	 * 
	 * @see afterPropertiesSet()
	 * @see JobDetail#setName(String)
	 **/
	private String beanName;

	/**
	 * The JobDetail produced by the <code>afterPropertiesSet</code> method, and
	 * returned by the <code>getObject</code> method of the Spring FactoryBean
	 * interface.
	 * 
	 * @see #afterPropertiesSet()
	 * @see #getObject()
	 * @see FactoryBean
	 **/
	private JobDetail jobDetail;

	/**
	 * The name of the Class to invoke.
	 **/
	private String targetClass;

	/**
	 * The Object to invoke.
	 * <p>
	 * {@link #targetClass} or targetObject must be set, but not both.
	 * <p>
	 * This object must be Serializable when {@link #concurrent} is set to
	 * false.
	 */
	private Object targetObject;

	/**
	 * The instance method to invoke on the Class or Object identified by the
	 * targetClass or targetObject property, respectfully.
	 * <p>
	 * targetMethod or {@link #staticMethod} should be set, but not both.
	 **/
	private String targetMethod;

	/**
	 * The static method to invoke on the Class or Object identified by the
	 * targetClass or targetObject property, respectfully.
	 * <p>
	 * {@link #targetMethod} or staticMethod should be set, but not both.
	 */
	private String staticMethod;

	private Object[] arguments;

	private String startTime;
	
	/**
	 * Get the targetClass property.
	 * 
	 * @see #targetClass
	 * @return targetClass
	 */
	public String getTargetClass() {
		return targetClass;
	}

	/**
	 * Set the targetClass property.
	 * 
	 * @see #targetClass
	 */
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * Get the targetMethod property.
	 * 
	 * @see #targetMethod
	 * @return targetMethod
	 */
	public String getTargetMethod() {
		return targetMethod;
	}

	/**
	 * Set the targetMethod property.
	 * 
	 * @see #targetMethod
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	/**
	 * @return jobDetail - The JobDetail that is created by the
	 *         afterPropertiesSet method of this FactoryBean
	 * @see #jobDetail
	 * @see #afterPropertiesSet()
	 * @see FactoryBean#getObject()
	 */
	public JobDetail getObject() throws Exception {
		return jobDetail;
	}

	/**
	 * @return JobDetail.class
	 * @see FactoryBean#getObjectType()
	 */
	public Class<JobDetail> getObjectType() {
		return JobDetail.class;
	}

	/**
	 * @return true
	 * @see FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Set the beanName property.
	 * 
	 * @see #beanName
	 * @see BeanNameAware#setBeanName(String)
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 * @see JobDetail
	 * @see #jobDetail
	 * @see #beanName
	 * @see #group
	 * @see MethodInvokingJob
	 * @see StatefulMethodInvokingJob
	 * @see #durable
	 * @see #shouldRecover
	 * @see #targetClass
	 * @see #targetMethod
	 * @see #jobListenerNames
	 */
	public void afterPropertiesSet() throws Exception {
		try {
			logger.debug("start");

			logger.debug("Creating JobDetail " + beanName);
			JobDetailImpl jdi = new JobDetailImpl();
			jdi.setName(beanName);
			jdi.setGroup(group);
			//调用目标对象的指定方法
			jdi.setJobClass(MethodInvokingJob.class);
			jdi.setDurability(durable);
			jdi.setRequestsRecovery(shouldRecover);
			if (targetClass != null)
				jdi.getJobDataMap().put("targetClass", targetClass);
			if (targetObject != null)
				jdi.getJobDataMap().put("targetObject", targetObject);
			if (targetMethod != null)
				jdi.getJobDataMap().put("targetMethod", targetMethod);
			if (staticMethod != null)
				jdi.getJobDataMap().put("staticMethod", staticMethod);
			if (arguments != null)
				jdi.getJobDataMap().put("arguments", arguments);
			
			if(startTime != null)
				jdi.getJobDataMap().put("startTime", startTime);
			
			this.jobDetail = jdi; 
			logger.info("Created JobDetail: " + jobDetail + "; targetClass: " + targetClass + "; targetObject: " + targetObject + "; targetMethod: "
					+ targetMethod + "; staticMethod: " + staticMethod + "; arguments: " + (arguments==null?null:Arrays.toString(arguments)) + ";");
		} finally {
			logger.debug("end");
		}
	}
	

	/**
	 * setter for the durable property.
	 * 
	 * @param durable
	 * 
	 * @see #durable
	 */
	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	/**
	 * setter for the group property.
	 * 
	 * @param group
	 * 
	 * @see #group
	 */
	public void setGroup(String group) {
		this.group = group;
	}


	/**
	 * setter for the {@link #shouldRecover} property.
	 * 
	 * @param shouldRecover
	 * @see #shouldRecover
	 */
	public void setShouldRecover(boolean shouldRecover) {
		this.shouldRecover = shouldRecover;
	}

	public static class MethodInvokingJob implements Job {
		protected Logger logger = LoggerFactory.getLogger(getClass());

		public void execute(JobExecutionContext context) throws JobExecutionException {
			try {
				logger.debug("start");
				String targetClass = context.getMergedJobDataMap().getString("targetClass");
				logger.debug("targetClass is " + targetClass);
				Class<?> targetClassClass = null;
				if (targetClass != null) {
					targetClassClass = Class.forName(targetClass); // Could
																	// throw
																	// ClassNotFoundException
				}
				Object targetObject = context.getMergedJobDataMap().get("targetObject");
				logger.debug("targetObject is " + targetObject);
				String targetMethod = context.getMergedJobDataMap().getString("targetMethod");
				logger.debug("targetMethod is " + targetMethod);
				String staticMethod = context.getMergedJobDataMap().getString("staticMethod");
				logger.debug("staticMethod is " + staticMethod);
				Object[] arguments = (Object[]) context.getMergedJobDataMap().get("arguments");
				logger.debug("arguments are " + (arguments==null?null:Arrays.toString(arguments)));

				logger.debug("creating MethodInvoker");
				MethodInvoker methodInvoker = new MethodInvoker();
				methodInvoker.setTargetClass(targetClassClass);
				methodInvoker.setTargetObject(targetObject);
				methodInvoker.setTargetMethod(targetMethod);
				methodInvoker.setStaticMethod(staticMethod);
				methodInvoker.setArguments(arguments);
				methodInvoker.prepare();
				logger.info("Invoking: " + methodInvoker.getPreparedMethod().toGenericString());
				methodInvoker.invoke();
			} catch(NoSuchMethodException e) {
			    logger.debug("NoSuchMethodException " + e.getMessage());
			} catch (Exception e) {
				logger.error("execute ",e);
				throw new JobExecutionException(e);
			} finally {
				logger.debug("end");
			}
		}
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String getStaticMethod() {
		return staticMethod;
	}

	public void setStaticMethod(String staticMethod) {
		this.staticMethod = staticMethod;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
}