package br.com.bernardorufino.android.universitario.ext.concurrency;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/* TODO: Analyse if we externalize the thread as a parameter (the handler) */
/* TODO: Can it be not synchronized? */
public abstract class TrialScheduler {

    private final TicToc mClock;
    private final Handler mHandler;
    private long mInterval;

    protected TrialScheduler(long interval) {
        mInterval = interval;
        mHandler = new TrialHandler(this);
        mClock = new TicToc(interval);
    }

    protected abstract void doExecute();

    public void tryExecute() {
        if (mClock.tictoc() >= mInterval) {
            doExecute();
        } else {
            mHandler.removeMessages(Messages.EXECUTE);
            mHandler.sendEmptyMessageDelayed(Messages.EXECUTE, mInterval);
        }
    }

    private static class TrialHandler extends Handler {

        private WeakReference<TrialScheduler> mTrialScheduler;

        private TrialHandler(TrialScheduler trialScheduler) {
            mTrialScheduler = new WeakReference<>(trialScheduler);
        }

        @Override
        public void handleMessage(Message msg) {
            TrialScheduler scheduler = mTrialScheduler.get();
            if (scheduler == null) throw new AssertionError("TrialScheduler is null on handleMessage()");
            switch (msg.what) {
                case Messages.EXECUTE:
                    scheduler.doExecute();
                    break;
                default:
                    throw new AssertionError("Unexpected message code " + msg.what);
            }
        }
    }

    private static class Messages {

        public static final int EXECUTE = 1;
    }
}
