package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestException;
import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.test.TestModel;
import info.goodline.framework.test.TestServiceApi;
import retrofit.Call;

/**
 * Created by g on 02.11.15.
 */
public class TestWorker {
    public static BaseRestRequest makeGetPolicyRequest(){
        return new BaseRestRequest<TestModel, TestServiceApi>() {
            @Override
            public Call<TestModel> onRequest(TestServiceApi service) throws BaseRestException {
                return service.getPolicy();
            }
        };
    }
}
