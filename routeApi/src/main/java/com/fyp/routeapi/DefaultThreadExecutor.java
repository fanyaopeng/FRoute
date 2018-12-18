package com.fyp.routeapi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by huisoucw on 2018/6/5.
 * 任何地方 请调用次方法 开启线程
 */

public class DefaultThreadExecutor extends ThreadPoolExecutor {
    private static final int core = Runtime.getRuntime().availableProcessors();
    private static DefaultThreadExecutor sInstance;

    public static DefaultThreadExecutor getInstance() {
        if (sInstance == null) {
            synchronized (DefaultThreadExecutor.class) {
                if (sInstance == null) {
                    sInstance = new DefaultThreadExecutor(core, core + 1, 30L, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<Runnable>(64));
                }
            }
        }
        return sInstance;
    }

    private DefaultThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
}
