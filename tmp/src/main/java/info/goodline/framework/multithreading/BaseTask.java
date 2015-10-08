package info.goodline.framework.multithreading;

import android.util.Log;

import info.goodline.framework.interfaces.ThreadObserver;

/**
 * Created by g on 08.10.15.
 */
public abstract class BaseTask  implements PriorityRunnable  {
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

    public void setId(int taskId){
        mTaskId = taskId;
    }

    @Override
    public int getPriority() {
        return mPriority;
    }

    protected abstract void runTask();

    @Override
    public void run() {
        runTask();
        onDone();
    }

    protected void onDone(){
        if(mThreadObserver != null){
            mThreadObserver.onTaskDone(mTaskTag, mTaskId);
        }
    }
}
