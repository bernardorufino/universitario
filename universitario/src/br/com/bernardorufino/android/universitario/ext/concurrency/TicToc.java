package br.com.bernardorufino.android.universitario.ext.concurrency;

import static com.google.common.base.Preconditions.*;

public class TicToc {

    private static final long UNDEFINED_TOC = -1;

    private volatile long mClock;
    private long mTocBeforeTic;

    public TicToc() {
        tic();
    }

    public TicToc(long tocBeforeTic) {
        checkArgument(tocBeforeTic >= 0);
        mTocBeforeTic = tocBeforeTic;
    }

    public synchronized void tic() {
        mTocBeforeTic = UNDEFINED_TOC;
        mClock = System.nanoTime();
    }

    /* In milliseconds */
    public synchronized long toc() {
        long nanoToMilli = 1000000;
        return (mTocBeforeTic == UNDEFINED_TOC) ? (System.nanoTime() - mClock) / nanoToMilli : mTocBeforeTic;
    }

    public synchronized long tictoc() {
        long toc = toc();
        tic();
        return toc;
    }

}
