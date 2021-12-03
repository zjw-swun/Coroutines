package com.example.coroutines;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 协程调度线程池封装
 */
public class AppExecutors {
    private final Executor mDiskIO;

    private final Executor mOtherIO;

    private final Executor mMainThread;

    private static AppExecutors mExecutors;

    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mThreadId = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("APP_Thread_" + mThreadId.getAndIncrement());
            return t;
        }
    };

    public static AppExecutors newInstance() {
        if (mExecutors == null) {
            mExecutors = new AppExecutors();
        }
        return mExecutors;
    }

    private AppExecutors() {
        mDiskIO = Executors.newSingleThreadExecutor(mThreadFactory);
        mOtherIO = Executors.newFixedThreadPool(3, mThreadFactory);
        mMainThread = new MainThreadExecutor();
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    public Executor getOtherIO() {
        return mOtherIO;
    }


    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
