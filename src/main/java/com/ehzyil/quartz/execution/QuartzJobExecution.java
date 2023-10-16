package com.ehzyil.quartz.execution;


import com.ehzyil.domain.Task;
import com.ehzyil.quartz.utils.TaskInvokeUtils;
import org.quartz.JobExecutionContext;

public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Task task) throws Exception {
        TaskInvokeUtils.invokeMethod(task);
    }
}
