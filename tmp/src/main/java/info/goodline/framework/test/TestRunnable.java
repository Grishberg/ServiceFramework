package info.goodline.framework.test;

import android.util.Log;

import info.goodline.framework.multithreading.PriorityRunnable;

/**
 * Created by g on 07.10.15.
 */
public class TestRunnable implements PriorityRunnable {
    private static final String TAG = TestRunnable.class.getSimpleName();
    private int mPriority;
    private int mId;

    public TestRunnable(int priority, int id) {
        mPriority = priority;
        mId = id;
    }

    @Override
    public void run() {
        Log.d(TAG, "run id="+mId+" priority="+mPriority);
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            Log.d(TAG,"thread id="+mId+"interrupted exception");
        }
        if(Thread.interrupted()){
            Log.d(TAG,"thread id="+mId+" interrupted, need shutdown");
        }
        Log.d(TAG,"thread id="+mId+" is done");
    }

    @Override
    public int getPriority() {
        return mPriority;
    }
}
