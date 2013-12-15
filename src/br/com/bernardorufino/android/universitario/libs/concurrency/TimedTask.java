package br.com.bernardorufino.android.universitario.libs.concurrency;


import android.os.AsyncTask;
import android.os.Handler;
import br.com.bernardorufino.android.universitario.helpers.Helper;

import java.util.concurrent.Executor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public abstract class TimedTask {

    private static final long UNDEFINED_INTERVAL = -1;
    private static Executor sThreadPool = AsyncTask.THREAD_POOL_EXECUTOR;

    public static void setThreadPool(Executor threadPool) { sThreadPool = threadPool; }

    /*
    * Will run periodically on worker thread, releasing mMainHandler thread
    * */
    public abstract void work();

    /*
    * Will mRun on mMainHandler thread after completion of work(), for UI updates for example
    * */
    public void onComplete() { /* Override */ }

    /*
    * Override for handling case when worker thread is stopped.
    * Will run on mMainHandler thread.
    * */
    public void onCancel() {
        Helper.log("Worker thread canceled");
    }


    // mMainHandler thread = thread from which this object was created
    // worker thread = new thread properly allocated to running timed task
    private Runnable mOnComplete;
    private Runnable mOnCancel;
    private Handler mMainHandler;
    private volatile boolean mRun;
    private long mInterval = UNDEFINED_INTERVAL;


    public TimedTask() {
        mMainHandler = new Handler();
        mOnComplete = new Runnable() {
            public void run() { onComplete(); }
        };
        mOnCancel = new Runnable() {
            public void run() { onCancel(); }
        };
    }

    public TimedTask(long intervalInMillis) {
        checkArgument(intervalInMillis > 0, "Interval must be greater than zero.");
        mInterval = intervalInMillis;
    }

    public void setInterval(long intervalInMillis) {
        mInterval = intervalInMillis;
    }

    /*
    * Execution is not guaranteed to start immediately, however interval is mostly precise
    * Priority here is not the compromise to the overall periodicity, instead the compromise
    * is with each interval. In other words, if intervalInMillis is set to 1000 ms and for some
    * reason the thread waits 800 ms, that does NOT mean the next interval will compensate with
    * perhaps 1200 ms, next interval will try to be most close to 1000 ms again.
    * */
    public void fire(final long intervalInMillis) {
        checkArgument(intervalInMillis > 0, "Interval must be greater than zero.");
        mRun = true;
        // Below code runs in worker thread
        sThreadPool.execute(new Runnable() {
            public void run() {
                while (mRun) {
                    work();
                    mMainHandler.post(mOnComplete);
                    try {
                        Thread.sleep(intervalInMillis);
                    } catch (InterruptedException e) {
                        mRun = false;
                    }
                }
                mMainHandler.post(mOnCancel);
            }
        });
    }

    public void fire() {
        checkState(mInterval != UNDEFINED_INTERVAL, "Interval wasn't set");
        fire(mInterval);
    }

    /*
    * Can be called to cancel execution
    * */
    public void cancel() {
        mRun = false;
    }
}
