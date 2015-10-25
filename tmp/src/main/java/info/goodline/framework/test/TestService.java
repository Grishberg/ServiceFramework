package info.goodline.framework.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.Future;

import info.goodline.framework.Const;
import info.goodline.framework.interfaces.IBinderService;
import info.goodline.framework.interfaces.ServiceThreadInteractionObserver;
import info.goodline.framework.service.BaseThreadPoolService;

/**
 * Test worker service
 */
public class TestService extends BaseThreadPoolService {
    private static final String TAG = TestService.class.getSimpleName();

    public TestService() {
    }

    public int startThread(String tag, int priority, int id) {
        Log.d(TAG, "schedule thread id=" + id + " priority=" + priority);
        TestRunnable runnable = new TestRunnable(this, tag, priority, id);
        return startManagedTask(runnable, tag);
    }

}
