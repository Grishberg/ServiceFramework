package info.goodline.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;

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
        mBinder = new ApiServiceBinder();
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
    public void sendMessage(int code, int id) {
        Intent intent = new Intent(Const.SERVICE_ACTION_TASK_DONE);
        intent.putExtra(Const.EXTRA_TASK_CODE, code);
        intent.putExtra(Const.EXTRA_TASK_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    /**
     * send message to activities
     */
    public void sendMessage(String action, int code, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Const.EXTRA_TASK_CODE, code);
        intent.putExtra(Const.EXTRA_TASK_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    public void sendMessage(String action, int code, Serializable data, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Const.EXTRA_TASK_SERIALIZABLE, data);
        intent.putExtra(Const.EXTRA_TASK_CODE, code);
        intent.putExtra(Const.EXTRA_TASK_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    public void sendMessage(String action, int code, Parcelable data, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Const.EXTRA_TASK_PARCELABLE, data);
        intent.putExtra(Const.EXTRA_TASK_CODE, code);
        intent.putExtra(Const.EXTRA_TASK_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

}

