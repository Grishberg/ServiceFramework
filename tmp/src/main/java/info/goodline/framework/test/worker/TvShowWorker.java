package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestException;
import info.goodline.framework.test.models.ListTvShowRest;
import info.goodline.framework.test.models.RutrackerModel;
import retrofit.Call;

/**
 * Created on 03.11.15.
 *
 * @author g
 */
public class TvShowWorker {
    private static final String TAG = TvShowWorker.class.getSimpleName();
    public static TvShowRequest makeRequest(){
        return new TvShowRequest() {
            @Override
            public Call<ListTvShowRest> onRequest() throws BaseRestException {
                return mService.getAllTVShows();
            }
        };
    }
}
