package info.goodline.framework.rest;

import java.io.IOException;

import retrofit.Call;

/**
 * Created by g on 25.10.15.
 */
public abstract class BaseRestRequest<T, S> {
    protected S mService;
    public void init (S service){
        mService = service;
    }
    public abstract Call<T> onRequest() throws BaseRestException;

    // calls in new thread
    public void onSuccess(T response) {

    }

    public void onFail(String msg, int errorCode) {

    }

    /**
     * calls in main thread
     *
     * @param response
     */
    public void onDone(T response) {

    }
}