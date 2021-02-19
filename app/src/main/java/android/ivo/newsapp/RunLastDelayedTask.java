package android.ivo.newsapp;

import android.os.SystemClock;
/**
 * The <code>RunLastDelayedTask</code> will delay the run() method of the <code>Runnable</code>
 * and then execute it only <b>once</b>. If multiple calls are made then only the last call will
 * be executed after the delay has taken place.
 * In order to get the mentioned  functionality only one instance of <code>RunLastDelayedTask</code>
 * needs to be created.
 *
 * @author Ivo Ganev
 */
class RunLastDelayedTask implements Runnable {
    private int mLastTaskCount;
    private int mCurrentTaskCount;
    private long mDelay;
    private static final long DEFAULT_DELAY = 500;

    private Runnable mRunnable;

    private RunLastDelayedTask() {
        mDelay = DEFAULT_DELAY;
    }

    /**
     * <code>RunLastDelayedTask</code> is a wrapper to a <code>Runnable</code>
     * with a changed functionality to run only when the <code>delay</code> duration
     * has been reached. If more than one threads execute the run() method then <b>only
     * the final</b> call will be executed.
     * For example: User starts to input quickly the characters "ABCDEF" with a time interval of
     * 2000 milliseconds. When the final character 'F' is being input, <b>only the last</b> call to the
     * run() method will execute.
     * <p>
     * The class is good for waiting a certain linear event to finish within a time period
     * and then execute a single task.
     *
     * @param delay the delay at which the run() will execute
     * @param runnable the runnable to execute after the delay has been reached
     */
    RunLastDelayedTask(long delay, Runnable runnable) {
        mDelay = delay;
        mRunnable = runnable;
    }

    @Override
    public void run() {
        runDelayed(mDelay);
    }

    private void runDelayed(long milliseconds) {
        mLastTaskCount++;
        // SystemClock.elapsedRealtime() shouldn't jump back and forth like System.currentTimeMillis()
        long time = SystemClock.elapsedRealtime() + milliseconds;
        while (SystemClock.elapsedRealtime() < time) {
            synchronized (this) {
                try {
                    Thread.sleep(time - SystemClock.elapsedRealtime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        mCurrentTaskCount++;
        if (mLastTaskCount == mCurrentTaskCount) {
            if (mRunnable != null)
                mRunnable.run();
            // execute and reset
            mLastTaskCount = 0;
            mCurrentTaskCount = 0;
        }
    }
}

