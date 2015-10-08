package info.goodline.framework.test;

import android.util.Log;

import info.goodline.framework.interfaces.ThreadObserver;
import info.goodline.framework.multithreading.BaseTask;
import info.goodline.framework.multithreading.PriorityRunnable;

/**
 * Created by g on 07.10.15.
 * test worker runnable
 */
public class TestRunnable extends BaseTask {
    private static final String TAG = TestRunnable.class.getSimpleName();
    private int mId;

    public TestRunnable(ThreadObserver observer, String taskTag, int priority, int mId) {
        super(observer, taskTag, priority);
        this.mId = mId;
    }

    @Override
    protected void runTask() {
        Log.d(TAG, "run id=" + mId + " priority=" + getPriority());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.d(TAG, "thread id=" + mId + " interrupted exception");
        }
        if (Thread.currentThread().isInterrupted()) {
            Log.d(TAG, "thread id=" + mId + " interrupted, need shutdown");
        }
        Log.d(TAG, "thread id=" + mId + " is done");
    }
}
