package info.goodline.framework.test;

import android.util.Log;

import java.io.IOException;

import info.goodline.framework.interfaces.ServiceThreadInteractionObserver;
import info.goodline.framework.interfaces.ThreadObserver;
import info.goodline.framework.multithreading.BaseTask;
import info.goodline.framework.multithreading.PriorityRunnable;
import info.goodline.framework.rest.BaseRestRequest;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by g on 07.10.15.
 * test worker runnable
 */
public class TestRunnable extends BaseTask {
    private static final String TAG = TestRunnable.class.getSimpleName();
    public static final int TRIES_COUNT = 5;
    private int mId;
    private TestServiceApi mService;
    private BaseRestRequest mRequest;

    public TestRunnable(TestServiceApi api, BaseRestRequest request, ServiceThreadInteractionObserver observer, String taskTag, int priority, int mId) {
        super(observer, taskTag, priority);
        this.mId = mId;
        mService = api;
        mRequest = request;
    }

    @Override
    protected void runTask() {
        boolean isInterrupted = false;
        Object result = null;
        try {
            result = executeRequest(mRequest);

            Log.d(TAG, "    run id=" + mId + " priority=" + getPriority());
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                if (Thread.currentThread().isInterrupted()) {
                    Log.d(TAG, "thread id=" + mId + " interrupted, need shutdown");
                    break;
                }
            }
        } catch (InterruptedException e) {
            isInterrupted = true;
            Log.d(TAG, "thread id=" + mId + " interrupted exception");
        }

        if (!isInterrupted) {
            ((ServiceThreadInteractionObserver) getThreadObserver()).onSuccess(getId(), mId);
        }
    }

    private Object executeRequest(BaseRestRequest request) {
        Response result = null;
        for (int tries = 0; tries < TRIES_COUNT; tries++) {
            try {
                result = request.onRequest(mService).execute();
                request.onSuccess(result.body());
                return result.body();
            }/*
            catch (AccessDeniedException e) {
                //update refresh token
                RefreshTokenResponse tokenResponse = refreshToken();
                if (tokenResponse != null) {
                    DeviceInfoHelper.updateDevRefreshToken(tokenResponse);
                }
                Log.e(TAG, "try " + tries + " error: " + e.getMessage());
            } catch (BaseAggeregatorException e) {
                Log.e(TAG, "try " + tries + " error: " + e.getMessage());
                request.onFail(e.getMessage(), e.getCode());
                break;
            } */ catch (Exception e) {
                Log.e(TAG, "try " + tries + " error: " + e.getMessage());
                if (e.getMessage() == null) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void refreshToken() {
        //TODO: refresh token
    }
}
