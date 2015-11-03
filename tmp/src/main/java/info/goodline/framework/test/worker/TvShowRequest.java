package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.test.models.ListTvShowRest;
import info.goodline.framework.test.retrofit_services.TvShowServiceApi;

/**
 * Created on 03.11.15.
 *
 * @author g
 */
public abstract class TvShowRequest extends BaseRestRequest<ListTvShowRest, TvShowServiceApi> {
}
