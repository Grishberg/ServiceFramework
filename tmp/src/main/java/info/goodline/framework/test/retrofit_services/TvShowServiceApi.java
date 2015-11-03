package info.goodline.framework.test.retrofit_services;

import info.goodline.framework.test.RestConst;
import info.goodline.framework.test.models.ListTvShowRest;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created on 03.11.15.
 *
 * @author g
 */

public interface TvShowServiceApi {
    @GET(RestConst.JSON_TV_ALL)
    Call<ListTvShowRest> getAllTVShows(/*(@Header("If-Modified-Since") String lastModifiedDate*/);
}
