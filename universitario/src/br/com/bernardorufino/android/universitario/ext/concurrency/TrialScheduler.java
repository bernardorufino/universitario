package br.com.bernardorufino.android.universitario.ext.concurrency;

import android.os.Handler;

/* TODO: Analyse if we externalize the thread as a parameter (the handler) */
/* TODO: Can it be not synchronized? */
public abstract class TrialScheduler {

    private static enum State { IDLE, SCHEDULED, RETRY }

    private State mState = State.IDLE;
    private Handler mHandler = new Handler();
    private TicToc mClock;
    private long mInterval;

    protected TrialScheduler(long interval) {
        mInterval = interval;
        mClock = new TicToc(interval);
    }

    protected abstract void doExecute();

    public void tryExecute() {
        if (mClock.tictoc() >= mInterval) {
            doExecute();
        } else if (mState == State.IDLE) {
            scheduleTrial();
            mState = State.SCHEDULED;
        } else if (mState == State.SCHEDULED) {
            mState = State.RETRY;
        } /* else do nothing because it's already set for retrial */
    }

    private void scheduleTrial() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mState == State.RETRY) {
                    scheduleTrial();
                    mState = State.SCHEDULED;
                    return;
                }
                doExecute();
                mState = State.IDLE;
            }
        }, mInterval);
    }
}
