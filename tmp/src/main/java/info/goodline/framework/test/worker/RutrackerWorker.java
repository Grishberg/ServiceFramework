package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestException;
import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.test.models.RutrackerModel;
import info.goodline.framework.test.retrofit_services.RutrackerServiceApi;
import retrofit.Call;

/**
 * Created by g on 03.11.15.
 */
public class RutrackerWorker {
    public static RutrackerRequest makeCatForumTreeRequest(){
        return new RutrackerRequest() {
            @Override
            public Call<RutrackerModel> onRequest() throws BaseRestException {
                return mService.catForumTree();
            }
        };
    }
}
