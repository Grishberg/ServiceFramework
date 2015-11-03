package info.goodline.framework.test.worker;

import info.goodline.framework.rest.BaseRestRequest;
import info.goodline.framework.test.models.TestModel;
import info.goodline.framework.test.retrofit_services.TestServiceApi;

/**
 * Created by g on 03.11.15.
 */
public abstract class TestRequest extends BaseRestRequest<TestModel, TestServiceApi> {
}
