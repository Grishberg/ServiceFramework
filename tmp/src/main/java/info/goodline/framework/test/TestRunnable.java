package info.goodline.framework.test;

import android.util.Log;

import info.goodline.framework.interfaces.ServiceThreadInteractionObserver;
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

    public TestRunnable(ServiceThreadInteractionObserver observer, String taskTag, int priority, int mId) {
        super(observer, taskTag, priority);
        this.mId = mId;
    }

    @Override
    protected void runTask() {
        boolean isInterrupted = false;
        try {
            Log.d(TAG, "    run id=" + mId + " priority=" + getPriority());
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                if (Thread.currentThread().isInterrupted()){
                    Log.d(TAG, "thread id=" + mId + " interrupted, need shutdown");
                    break;
                }
            }
        } catch (InterruptedException e) {
            isInterrupted = true;
            Log.d(TAG, "thread id=" + mId + " interrupted exception");
        }
        if(!isInterrupted){
            ((ServiceThreadInteractionObserver)getThreadObserver()).onSuccess(getId(),mId);
        }
    }
}
