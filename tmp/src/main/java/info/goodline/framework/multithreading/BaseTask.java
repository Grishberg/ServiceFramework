package info.goodline.framework.multithreading;

import android.util.Log;

import info.goodline.framework.interfaces.ThreadObserver;

/**
 * Created by g on 08.10.15.
 */
public abstract class BaseTask implements PriorityRunnable {
    private static final String TAG = BaseTask.class.getSimpleName();
    private ThreadObserver mThreadObserver;
    private String mTaskTag;
    private int mTaskId;
    private int mPriority;

    public BaseTask(ThreadObserver observer, String taskTag, int priority) {
        mThreadObserver = observer;
        mTaskTag = taskTag;
        mPriority = priority;
    }

    public void setId(int taskId) {
        mTaskId = taskId;
    }

    public int getId() {
        return mTaskId;
    }

    public ThreadObserver getThreadObserver() {
        return mThreadObserver;
    }

    @Override
    public int getPriority() {
        return mPriority;
    }

    protected abstract void runTask() throws InterruptedException;

    @Override
    public void run() {
        boolean isInterrupted = false;
        try {
            runTask();
        } catch (InterruptedException e) {
            isInterrupted = true;
        }
        onDone(isInterrupted);
    }

    protected void onDone(boolean isInterrupted) {
        mThreadObserver.onTaskDone(mTaskTag, mTaskId, isInterrupted);
    }
}
