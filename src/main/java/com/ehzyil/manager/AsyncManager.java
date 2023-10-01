package com.ehzyil.manager;

import com.ehzyil.domain.VisitLog;
import com.ehzyil.manager.factory.AsyncFactory;
import com.ehzyil.utils.SpringUtils;
import com.ehzyil.utils.ThreadUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ehyzil
 * @Description 异步任务管理器
 * @create 2023-10-2023/10/1-21:14
 */
public class AsyncManager {
   /**
     * 单例模式，确保类只有一个实例
     */
    private AsyncManager() {
    }

    /**
     * 饿汉式，在类加载的时候立刻进行实例化
     */
    private static final AsyncManager INSTANCE = new AsyncManager();
    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 获取实例
     *
     * @return AsyncManager
     */
    public static  AsyncManager getInstance() {
        return INSTANCE;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        executor.schedule(task, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }
}
