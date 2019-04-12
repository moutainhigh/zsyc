package com.zsyc.warehouse.task.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @ClassName: ScheduleUtils
 * @Description: 定时任务类
 * @author lixk
 * @date 2017年8月16日 下午1:27:13
 * @version [1.0, 2017年8月16日]
 * @since version 1.0
 */
public class ScheduleUtils {
	
	public static final String taskClassName = "com.zsyc.warehouse.task.TaskJob";
	public static final String taskMethodName = "run";

	private ScheduleUtils() {
	}

	// task集合
	private static final Map<String, Task> TASK_MANAGER = new HashMap<String, Task>();
	// 定时器线程池
	private static final ScheduledExecutorService EXECUTOR_POOL = Executors.newScheduledThreadPool(6);
	// 定时任务队列
	private static final BlockingQueue<Task> TASK_QUEUE = new LinkedBlockingQueue<Task>();

	private final static Logger logger = LoggerFactory.getLogger(ScheduleUtils.class);

	// 静态初始化方法
	static {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Task task = TASK_QUEUE.take();
						// 任务有效，则执行任务
						if (task.isEffective()) {
							task.execute();
						}
					} catch (Exception e) {
						logger.error("定时任务执行异常：", e);
					}
				}
			}
		});
		executor.shutdown();
	}

	/**
	 * 
	 * @Title: add
	 * @Description: 添加定时任务
	 * @param job
	 * @throws Exception
	 */
	public synchronized static void add(Job job) throws Exception {
		cancel(job); // 终结执行中的任务
		Task task = new Task(TASK_QUEUE, EXECUTOR_POOL, job.getClassName(), job.getMethodName(),job.getParas() ,job.getCron());
		TASK_MANAGER.put(job.getJobName(), task);
		// 将任务加入队列
		TASK_QUEUE.put(task);
	}

	/**
	 * 
	 * @Title: cancel
	 * @Description: 取消任务
	 * @param job
	 */
	public synchronized static void cancel(Job job) {
		if (job == null) {
			return;
		}
		String jobName = job.getJobName();
		if (jobName == null) {
			return;
		}
		Task task = TASK_MANAGER.get(jobName);
		if (task != null) {
			// 关闭任务，停止任务线程
			task.setEffective(false);
			ScheduledFuture<?> future = task.getFuture();
			if (future != null) {
				future.cancel(true);
			}
		}
		TASK_MANAGER.remove(jobName);
	}

	/**
	 * 
	 * @ClassName: Task
	 * @Description: 任务内部类
	 * @author lixk
	 * @date 2017年8月16日 下午7:38:44
	 * @version [1.0, 2017年8月16日]
	 * @since version 1.0
	 */
	private static class Task {
		private BlockingQueue<Task> queue; // 任务队列
		private CronTrigger trigger; // cron触发器
		private ScheduledExecutorService executor; // 定时器线程池
		private Class<?> clazz; // 反射类名
		private Object targetObject; // 反射对象
		private Method method; // 反射方法
		private Object[] paras;//反射方法参数
		private Task self; // task对象自己
		private ScheduledFuture<?> future; // task对象的future
		private boolean effective = true; // task对象状态

		private final static Logger logger = LoggerFactory.getLogger(Task.class);

		public Task(BlockingQueue<Task> queue, ScheduledExecutorService executor, String className, String methodName, Object [] paras,String cron) throws Exception {
			Class c[]=null;
	         if(paras!=null){//存在
	             int len=paras.length;
	             c=new Class[len];
	             for(int i=0;i<len;++i){
	                 c[i]=paras[i].getClass();
	             }
	         }
			this.queue = queue;
			this.executor = executor;
			this.trigger = new CronTrigger(cron);
			this.clazz = Class.forName(className);
			this.targetObject = clazz.newInstance();
			this.method = clazz.getDeclaredMethod(methodName,c);
			this.paras = paras;
			this.self = this;
		}

		public void execute() throws Exception {
			Date now = new Date();
			long delay = trigger.next(now).getTime() - now.getTime(); // 等待时间

			this.future = executor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						method.invoke(targetObject,paras);
					} catch (Exception e) {
						logger.error("定时任务执行异常：", e);
					} finally{
						// 把当前任务加入队列
						try {
							queue.put(self);
						} catch (InterruptedException e) {
							logger.error("添加定时任务到队列异常：", e);
						}
					}
				}
			}, delay, TimeUnit.MILLISECONDS);

		}

		public ScheduledFuture<?> getFuture() {
			return future;
		}

		public boolean isEffective() {
			return effective;
		}

		public void setEffective(boolean effective) {
			this.effective = effective;
		}
	}

	/********************************** 定时任务实体类 ************************************/
	/**
	 * 
	 * @ClassName: Job
	 * @Description: 定时任务实体类
	 * @author lixk
	 * @date 2017年8月14日 下午3:03:51
	 * @version [1.0, 2017年8月14日]
	 * @since version 1.0
	 */
	public static class Job {
		private String jobName; // 任务名称
		private final String className  = taskClassName; // 定时任务类名
		private final String methodName = taskMethodName; // 定时任务方法名
		private String cron; // 定时任务cron表达式
		private Integer status; //定时任务状态
		private Object[] paras; //任务方法参数
		
		
				
		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			//this.className = className;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			//this.methodName = methodName;
		}

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
		
		public Object[] getParas() {
			return paras;
		}

		public void setParas(Object[] paras) {
			this.paras = paras;
		}

	}
}

