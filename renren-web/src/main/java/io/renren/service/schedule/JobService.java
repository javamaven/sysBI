package io.renren.service.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.service.schedule.entity.JobVo;

/**
 * 计划任务管理
 * 
 * @author liaodehui
 */
@Service
public class JobService {
	public final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ScheduleReportTaskService scheduleReportTaskService;

	/**
	 * 初始化所有定时任务
	 * 
	 * @throws Exception
	 */
	public void initJobs() throws Exception {
		System.err.println("=============初始化定时任务=============");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isRunning", "1");
		List<ScheduleReportTaskEntity> jobList = scheduleReportTaskService.queryList(map);
		for (int i = 0; i < jobList.size(); i++) {
			ScheduleReportTaskEntity scheduleReportTaskEntity = jobList.get(i);
			System.err.println("=============" + scheduleReportTaskEntity);
			JobVo jobVo = scheduleReportTaskEntity.toJobVo();
			addJob(jobVo, jobVo.getJobClass());
		}
	}
	/**
	 * 判断任务是否存在
	 * 
	 * @param job
	 * @return
	 * @throws SchedulerException
	 */
	public boolean existJob(JobVo job) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobId(), job.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (trigger == null) {
			return false;
		}
		return true;
	}

	/**
	 * 添加任务
	 * 
	 * @param job
	 * @param clazz
	 *            任务实现类
	 * @throws SchedulerException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addJob(JobVo job, Class clazz) throws SchedulerException {
		log.info("======新增任务[" + job.getJobName() + "; 调度时间：" + job.getCronExpression() + "]======");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobId(), job.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// job.setCronExpression("0 0/1 * * * ?");
		// 不存在，创建一个
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobId(), job.getJobGroup()).build();
			jobDetail.getJobDataMap().put("jobVo", job);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobId(), job.getJobGroup())
					.withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	public int getAlarmJobSize() throws SchedulerException {
		int size = 0;
		List<JobVo> jobs = getAllJob();
		for (int i = 0; i < jobs.size(); i++) {
			JobVo vo = jobs.get(i);
			if (JobVo.JOB_GROUP_NAME.equals(vo.getJobGroup())) {
				size++;
			}
		}
		return size;
	}

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobVo> getAllJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<JobVo> jobList = new ArrayList<JobVo>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				JobVo job = new JobVo();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setJobStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobVo> getRunningJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<JobVo> jobList = new ArrayList<JobVo>(executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			JobVo job = new JobVo();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = executingJob.getTrigger();
			job.setJobName(jobKey.getName());
			job.setJobGroup(jobKey.getGroup());
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			job.setJobStatus(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpression);
			}
			jobList.add(job);
		}
		return jobList;
	}

	/**
	 * 暂停一个job
	 * 
	 * @param jobVo
	 * @throws SchedulerException
	 */
	public void pauseJob(JobVo jobVo) throws SchedulerException {
		// System.out.println("======暂停任务["+jobVo.getJobName()+"]======");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobVo.getJobId(), jobVo.getJobGroup());
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复一个job
	 * 
	 * @param jobVo
	 * @throws SchedulerException
	 */
	public void resumeJob(JobVo jobVo) throws SchedulerException {
		// System.out.println("======恢复任务["+jobVo.getJobName()+"]======");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobVo.getJobId(), jobVo.getJobGroup());
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 删除一个job
	 * 
	 * @param jobVo
	 * @throws SchedulerException
	 */
	public void deleteJob(JobVo jobVo) throws SchedulerException {
		log.info("======删除任务[" + jobVo.getJobName() + "]======");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobVo.getJobId(), jobVo.getJobGroup());
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getJobId(), jobVo.getJobGroup());
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 立即执行job
	 * 
	 * @param jobVo
	 * @throws SchedulerException
	 */
	public void runAJobNow(JobVo jobVo, Class<?> clazz) throws SchedulerException {
		// System.out.println("======立即执行任务["+jobVo.getJobName()+"]======");
		if (existJob(jobVo)) {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobVo.getJobId(), jobVo.getJobGroup());
			scheduler.triggerJob(jobKey);
		} else {
			// System.out.println("======任务["+jobVo.getJobName()+"]不存在======");
			addJob(jobVo, clazz);
		}

	}

	/**
	 * 更新job时间表达式
	 * 
	 * @param jobVo
	 * @throws SchedulerException
	 */
	public void updateJobCron(JobVo jobVo, Class<?> clazz) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getJobId(), jobVo.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (trigger == null) {
			addJob(jobVo, clazz);
			return;
		}
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobVo.getCronExpression());
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
	}

}
