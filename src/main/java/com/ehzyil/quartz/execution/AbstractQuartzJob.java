package com.ehzyil.quartz.execution;

import com.ehzyil.constant.ScheduleConstant;
import com.ehzyil.domain.Task;
import com.ehzyil.domain.TaskLog;
import com.ehzyil.mapper.TaskLogMapper;
import com.ehzyil.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;

import static com.ehzyil.constant.CommonConstant.FALSE;
import static com.ehzyil.constant.CommonConstant.TRUE;


/**
 * @author ehyzil
 * @Description 抽象quartz调用
 * @create 2023-10-2023/10/16-17:31
 */
public abstract class AbstractQuartzJob implements Job {


    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);
    //线程本地变量 用于存储执行开始时间
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        Task task = new Task();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstant.TASK_PROPERTIES), task);
        try {
            before(context, task);
            doExecute(context, task);
            after(context, task, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, task, e);
        }

    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param task    系统计划任务
     */
    private void after(JobExecutionContext context, Task task, Exception e) {
        //获取任务开始时间
        Date startTime = threadLocal.get();
        threadLocal.remove();

        //记录任务日志
        final TaskLog taskLog = new TaskLog();
        taskLog.setTaskName(task.getTaskName());
        taskLog.setTaskGroup(task.getTaskGroup());
        taskLog.setInvokeTarget(task.getInvokeTarget());
        long runTime = new Date().getTime() - startTime.getTime();
        taskLog.setTaskMessage(taskLog.getTaskName() + " 总共耗时：" + runTime + "毫秒");

        //处理异常情况
        if (e != null) {
            taskLog.setStatus(FALSE);
            //截取异常信息
            String errorMsg = StringUtils.substring(e.getMessage(), 0, 2000);
            taskLog.setErrorInfo(errorMsg);
        } else {
            //正常情况
            taskLog.setStatus(TRUE);
        }

        // 写入数据库当中
        SpringUtils.getBean(TaskLogMapper.class).insert(taskLog);

    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param task    系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, Task task) throws Exception;


    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param task    系统计划任务
     */
    private void before(JobExecutionContext context, Task task) {
        threadLocal.set(new Date());
    }


}
