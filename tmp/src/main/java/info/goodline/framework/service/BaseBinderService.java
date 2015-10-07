package info.goodline.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import info.goodline.framework.Const;

/**
 * Created by g on 07.10.15.
 */
public abstract class BaseBinderService extends Service {
    private static final int SHUTDOWN_TIMER = 5000;
    private boolean mIsShutdowning;
    private Handler mShutdownHandler;
    private int mBindersCount;
    private ApiServiceBinder mBinder;

    public BaseBinderService() {
        mBindersCount = 0;
        mShutdownHandler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBindersCount--;
        if (mBindersCount == 0) {
            // start shutdown
            mIsShutdowning = true;
            mShutdownHandler.postDelayed(mShutdownRunnable, SHUTDOWN_TIMER);
        }
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        if (mIsShutdowning) {
            // cancel shutdown
            mShutdownHandler.removeCallbacks(mShutdownRunnable);
            mIsShutdowning = false;
        }
        mBindersCount++;
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBindersCount++;
        return mBinder;
    }

    private Runnable mShutdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (mBindersCount == 0) {
                stopSelf();
            }
        }
    };

    // service container for Activity
    public class ApiServiceBinder extends Binder {
        public BaseBinderService getService() {
            return BaseBinderService.this;
        }
    }

    /**
     * send message to activities
     */
    protected void sendMessage(String action, int code, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Const.REST_SERVICE_RESPONSE_CODE_EXTRA, code);
        intent.putExtra(Const.REST_SERVICE_RESPONSE_ID_EXTRA, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    protected void sendFeedMessage() {
        Intent intent = new Intent(Const.SERVICE_ACTION_FEED_RESPONSE);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    protected void sendAuthMessage(String accessToken, String userName, String userProfile) {
        Intent intent = new Intent(Const.SERVICE_ACTION_AUTH);
        intent.putExtra(Const.EXTRA_ACCESS_TOKEN, accessToken);
        intent.putExtra(Const.EXTRA_USER_NAME, userName);
        intent.putExtra(Const.EXTRA_USER_PICTURE, userProfile);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    protected void sendImageLoadedMessage(long id) {
        Intent intent = new Intent(Const.SERVICE_ACTION_IMAGE_LOADED);
        intent.putExtra(Const.EXTRA_IMAGE_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }
}

