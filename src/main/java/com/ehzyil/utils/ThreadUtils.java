package com.ehzyil.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author ehyzil
 * @Description 线程工具类
 * @create 2023-10-2023/10/1-21:02
 */
public class ThreadUtils {

    private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    private static final long OVERTIME = 120;

    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {

            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            if (t != null) {
                logger.error(t.getMessage(), t);
            }
        }

    }

    /**
     * * 停止线程池
     * * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * * 如果仍然超時，則強制退出.
     * * 另对在shutdown时线程本身被调用中断做了处理.
     *
     * @param pool
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.isShutdown();

            try {
                if (!pool.awaitTermination(OVERTIME, TimeUnit.SECONDS)) {
                    pool.isShutdown();
                    if (!pool.awaitTermination(OVERTIME, TimeUnit.SECONDS)) {
                        logger.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                pool.isShutdown();
                Thread.currentThread().interrupt();
            }


        }
    }
}

