package info.goodline.framework.test;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by g on 02.11.15.
 */
public interface TestServiceApi {
    @GET("/rc/getPolicy")
    Call<TestModel> getPolicy();
}
