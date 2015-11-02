package info.goodline.framework.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.Future;

import info.goodline.framework.Const;
import info.goodline.framework.interfaces.IBinderService;
import info.goodline.framework.interfaces.ServiceThreadInteractionObserver;
import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.rest.HttpLoggingInterceptor;
import info.goodline.framework.service.BaseThreadPoolService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Test worker service
 */
public class TestService extends BaseThreadPoolService {
    private static final String TAG = TestService.class.getSimpleName();
    private TestServiceApi pairService;

    public TestService() {
        initService();
    }

    private void initService() {
        if (pairService == null) {

            OkHttpClient client = new OkHttpClient();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RestConst.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            pairService = retrofit.create(TestServiceApi.class);
        }
    }

    public int startThread(BaseRestRequest request, String tag, int priority, int id) {
        Log.d(TAG, "schedule thread id=" + id + " priority=" + priority);
        TestRunnable runnable = new TestRunnable(pairService, request, this, tag, priority, id);
        return startManagedTask(runnable, tag);
    }
}
