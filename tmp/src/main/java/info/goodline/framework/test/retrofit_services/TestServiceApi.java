package info.goodline.framework.test.retrofit_services;

import info.goodline.framework.test.models.TestModel;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by g on 02.11.15.
 */
public interface TestServiceApi {
    @GET("/rc/getPolicy")
    Call<TestModel> getPolicy();
}
