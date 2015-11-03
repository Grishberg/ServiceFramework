package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestException;
import info.goodline.framework.test.models.TestModel;
import info.goodline.framework.test.retrofit_services.TestServiceApi;
import retrofit.Call;

/**
 * Created by g on 02.11.15.
 */
public class TestWorker {
    public static TestRequest makeGetPolicyRequest(){
        return new TestRequest() {
            @Override
            public Call<TestModel> onRequest() throws BaseRestException {
                return mService.getPolicy();
            }
        };
    }
}
