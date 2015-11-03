package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestException;
import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.test.models.RutrackerModel;
import info.goodline.framework.test.retrofit_services.RutrackerServiceApi;

/**
 * Created by g on 03.11.15.
 */
public abstract class RutrackerRequest extends BaseRestRequest<RutrackerModel, RutrackerServiceApi> {
}
