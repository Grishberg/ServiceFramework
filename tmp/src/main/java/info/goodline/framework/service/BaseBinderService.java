package info.goodline.framework.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Serializable;
import info.goodline.framework.Const;

/**
 * Created by g on 07.10.15.
 */
public abstract class BaseBinderService extends Service {
    private static final String TAG = BaseBinderService.class.getSimpleName();
    private int mBindersCount;
    private int mActivitiesCount;
    private boolean mIsBroadcasRegistered;
    private ApiServiceBinder mBinder;
    private IntentFilter mLocalBroadcast;

    public BaseBinderService() {
        mBindersCount = 0;
        mBinder = new ApiServiceBinder();
    }

    /**
     * broadcast to listen create/destroy events from activities
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(Const.EXTRA_TASK_CODE, -1);
            Log.d(TAG, "onReceive: code=" + code);
            switch (code) {
                case Const.EXTRA_CREATE_ACTIVITY_CODE:
                    mActivitiesCount++;
                    break;
                case Const.EXTRA_DESTROY_ACTIVITY_CODE:
                    mActivitiesCount--;
                    checkDestroyService();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
        mLocalBroadcast = new IntentFilter(Const.ACTIVITY_ACTION);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBindersCount--;
        if (mBindersCount == 0) {
            // start shutdown
            checkDestroyService();
        }
        return true;
    }

    /**
     * checking shutdown service
     */
    private void checkDestroyService() {
        if (mBindersCount == 0 && mActivitiesCount == 0) {
            stopSelf();
        }
    }

    private void registerBroadcast() {
        if (!mIsBroadcasRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mMessageReceiver, mLocalBroadcast);
            mIsBroadcasRegistered = true;
        }
    }

    private void unregisterBroadcast() {
        if (mIsBroadcasRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    mMessageReceiver);
            mIsBroadcasRegistered = false;
        }
    }

    //--------------- bindings -----------------------------------

    @Override
    public void onRebind(Intent intent) {
        mBindersCount++;
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBindersCount == 0 && mActivitiesCount == 0) {
            mActivitiesCount = 1;
        }
        mBindersCount++;
        return mBinder;
    }

    /**
     * service container for Activity
     */
    public class ApiServiceBinder extends Binder {
        public BaseBinderService getService() {
            return BaseBinderService.this;
        }
    }

    /**
     * send message to activity
     */
    public void sendMessage(int code, int id) {
        sendMessage(Const.SERVICE_ACTION_TASK_DONE, code, null, id);
    }

    public void sendMessage(String action, int code, int id) {
        sendMessage(action, code, null, id);
    }

    public void sendMessage(String action, int code, Serializable data, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Const.EXTRA_TASK_SERIALIZABLE, data);
        intent.putExtra(Const.EXTRA_TASK_CODE, code);
        intent.putExtra(Const.EXTRA_TASK_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }

    public void sendMessageWithParcel(String action, int code, Parcelable data, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Const.EXTRA_TASK_PARCELABLE, data);
        intent.putExtra(Const.EXTRA_TASK_CODE, code);
        intent.putExtra(Const.EXTRA_TASK_ID, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }
}

