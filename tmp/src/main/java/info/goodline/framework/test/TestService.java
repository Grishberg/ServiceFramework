package info.goodline.framework.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.Future;

import info.goodline.framework.service.BaseThreadPoolService;

public class TestService extends BaseThreadPoolService {
    private static final String TAG = TestService.class.getSimpleName();
    public TestService() {
    }

    public void startThread(int priority, int id){
        Log.d(TAG, "on start thread id=" + id+" priority="+priority);
        TestRunnable runnable = new TestRunnable(priority, id);
        Future future = mExecutor.submit(runnable);
    }
}
