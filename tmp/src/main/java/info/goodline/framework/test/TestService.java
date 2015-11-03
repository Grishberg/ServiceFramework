package info.goodline.framework.test;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.rest.HttpSimpleLoggingInterceptor;
import info.goodline.framework.service.BaseThreadPoolService;
import info.goodline.framework.test.retrofit_services.RutrackerServiceApi;
import info.goodline.framework.test.retrofit_services.TestServiceApi;
import info.goodline.framework.test.worker.RutrackerRequest;
import info.goodline.framework.test.worker.TestRequest;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Test worker service
 */
public class TestService extends BaseThreadPoolService {
    private static final String TAG = TestService.class.getSimpleName();
    private RutrackerServiceApi mRutrackerServiceApi;
    private TestServiceApi mTestServiceApi;

    public TestService() {
        initRutrackerService();
        initTestService();
    }

    private void initRutrackerService() {
            OkHttpClient client = new OkHttpClient();
            HttpSimpleLoggingInterceptor interceptor = new HttpSimpleLoggingInterceptor();
            interceptor.setLevel(HttpSimpleLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RestConst.RUTRACKER_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mRutrackerServiceApi = retrofit.create(RutrackerServiceApi.class);
    }

    private void initTestService() {
        OkHttpClient client = new OkHttpClient();
        HttpSimpleLoggingInterceptor interceptor = new HttpSimpleLoggingInterceptor();
        interceptor.setLevel(HttpSimpleLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestConst.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTestServiceApi = retrofit.create(TestServiceApi.class);
    }

    public int startThread(BaseRestRequest request, String tag, int priority, int id) {
        Log.d(TAG, "schedule thread id=" + id + " priority=" + priority);
        if(request instanceof TestRequest ){
            request.init(mTestServiceApi);
        } else if(request instanceof RutrackerRequest){
            request.init(mRutrackerServiceApi);
        }

        TestRunnable runnable = new TestRunnable(request, this, tag, priority, id);
        return startManagedTask(runnable, tag);
    }
}
