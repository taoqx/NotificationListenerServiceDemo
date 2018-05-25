package com.example.notificationlistenerservicedemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

    private static ThreadPoolUtil mInstance = null;

    private ExecutorService executor = null;

    public static ThreadPoolUtil getInstance() {
        if (null == mInstance) {
            synchronized (ThreadPoolUtil.class) {
                if (null == mInstance) {
                    mInstance = new ThreadPoolUtil();
                }
            }
        }
        return mInstance;
    }

    private ThreadPoolUtil() {
        executor = Executors.newFixedThreadPool(5);
    }

    public void run(Runnable command) {
        try {
            executor.execute(command);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    public void close() {
        executor.shutdown();
    }
}