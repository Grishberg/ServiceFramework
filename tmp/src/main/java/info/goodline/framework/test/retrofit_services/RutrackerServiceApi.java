package info.goodline.framework.test.retrofit_services;

import info.goodline.framework.test.RestConst;
import info.goodline.framework.test.models.RutrackerModel;
import info.goodline.framework.test.models.TestModel;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by g on 03.11.15.
 */
public interface RutrackerServiceApi {
    @GET(RestConst.CAT_FORUM_TREE)
    Call<RutrackerModel> catForumTree();
}
