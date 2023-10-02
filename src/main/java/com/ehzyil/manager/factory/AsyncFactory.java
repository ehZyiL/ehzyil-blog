package com.ehzyil.manager.factory;

import com.ehzyil.annotation.OptLogger;
import com.ehzyil.domain.ExceptionLog;
import com.ehzyil.domain.OperationLog;
import com.ehzyil.domain.VisitLog;
import com.ehzyil.service.IExceptionLogService;
import com.ehzyil.service.IOperationLogService;
import com.ehzyil.service.IVisitLogService;
import com.ehzyil.utils.SpringUtils;
import org.aspectj.weaver.IEclipseSourceContext;

import java.util.TimerTask;

/**
 * @author ehyzil
 * @Description
 * @create 2023-10-2023/10/1-21:19
 */
public class AsyncFactory {

    public static TimerTask recordVisit(VisitLog visitLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IVisitLogService.class).saveVisitLog(visitLog);
            }
        };
    }

    public static TimerTask recordOperation(OperationLog operationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IOperationLogService.class).recordOperation(operationLog);
            }
        };
    }

    public static TimerTask recordException(ExceptionLog exceptionLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IExceptionLogService.class).recordException(exceptionLog);
            }
        };
    }

}
