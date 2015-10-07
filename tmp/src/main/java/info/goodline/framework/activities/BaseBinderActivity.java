package info.goodline.framework.activities;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import info.goodline.framework.Const;
import info.goodline.framework.service.BaseBinderService;

/**
 * Created by g on 07.10.15.
 */
public abstract class BaseBinderActivity extends AppCompatActivity {
    private boolean mIsBound;
    private Intent mIntent;
    private boolean mIsBroadcasRegistered;
    private IntentFilter mLocalBroadcast;
    protected BaseBinderService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIsBound = true;
            mService = ((BaseBinderService.ApiServiceBinder) service).getService();
            onBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };

    protected abstract Intent getServiceIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent = getServiceIntent();
        mLocalBroadcast = new IntentFilter(Const.SERVICE_ACTION_AUTH);
        mLocalBroadcast.addAction(Const.SERVICE_ACTION_FEED_RESPONSE);
        registerBroadcast();

        bindToService();
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindFromService();
        unregisterBroadcast();
    }

    @Override
    public void onResume() {
        super.onResume();
        bindToService();
        registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        unbindFromService();
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

    private void bindToService() {
        if (!mIsBound) {
            bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindFromService() {
        if (mIsBound) {
            unbindService(mServiceConnection);
            mIsBound = false;
            mService = null;
        }
    }

    protected void onBound() {
    }

    protected void onResponseAccessToken(String accessToken, String username, String userAvatar) {

    }

    protected void onGetFeedResponse() {

    }

    protected void onImageLoaded(long id){

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Const.SERVICE_ACTION_FEED_RESPONSE:
                    onGetFeedResponse();
                    break;
                case Const.SERVICE_ACTION_AUTH:
                    String accessToken = intent.getStringExtra(Const.EXTRA_ACCESS_TOKEN);
                    String userName = intent.getStringExtra(Const.EXTRA_USER_NAME);
                    String userPic = intent.getStringExtra(Const.EXTRA_USER_PICTURE);
                    onResponseAccessToken(accessToken, userName, userPic);
                    break;
                case Const.SERVICE_ACTION_IMAGE_LOADED:
                    long id = intent.getLongExtra(Const.EXTRA_IMAGE_ID, 0);
                    onImageLoaded(id);
            }
        }
    };
}
