package info.goodline.framework.activities;

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
import info.goodline.framework.interfaces.IBindedService;
import info.goodline.framework.service.BaseBinderService;
import info.goodline.framework.service.BaseThreadPoolService;

/**
 * Created by g on 07.10.15.
 */
public abstract class BaseBinderActivity extends AppCompatActivity {
    private boolean mIsBound;
    private Intent mIntent;
    private boolean mIsBroadcasRegistered;
    private IntentFilter mLocalBroadcast;
    protected BaseBinderService mService;
    private boolean mIsFirstBind = true;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIsBound = true;
            mService = (BaseBinderService)((IBindedService) service).getService();
            if(mIsFirstBind){
                mIsFirstBind = false;
                onFirstBound();
            }
            onBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };

    protected void onFirstBound(){

    }

    protected abstract Intent getServiceIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent = getServiceIntent();
        mLocalBroadcast = new IntentFilter(Const.SERVICE_ACTION_TASK_DONE);
        mLocalBroadcast.addAction(Const.SERVICE_ACTION_TASK_FAIL);
        mIsFirstBind = true;
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

    protected boolean cancelTask(String tag){
        if(!mIsBound) return false;
        ((BaseThreadPoolService)mService).cancelTaskQueue(tag);
        return true;
    }

    protected boolean delaylTask(String tag){
        if(!mIsBound) return false;
        ((BaseThreadPoolService)mService).delayTaskQueue(tag);
        return true;
    }


    protected boolean cancelTask(String tag, int id){
        if(!mIsBound) return false;
        ((BaseThreadPoolService)mService).cancelTaskQueue(tag, id);
        return true;
    }

    protected void onBound() {
    }

    protected void onTaskDone(String tag, int taskId, int code){
    }

    protected void onTaskFail(String tag, int taskId, int code){
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            String tag;
            int id;
            int code;
            switch (intent.getAction()) {
                case Const.SERVICE_ACTION_TASK_DONE:
                    tag = intent.getStringExtra(Const.EXTRA_TASK_TAG);
                    id = intent.getIntExtra(Const.EXTRA_TASK_ID, -1);
                    code = intent.getIntExtra(Const.EXTRA_TASK_CODE, -1);
                    onTaskDone(tag, id, code);
                    break;
                case Const.SERVICE_ACTION_TASK_FAIL:
                    tag = intent.getStringExtra(Const.EXTRA_TASK_TAG);
                    id = intent.getIntExtra(Const.EXTRA_TASK_ID, -1);
                    code = intent.getIntExtra(Const.EXTRA_TASK_CODE, -1);
                    onTaskFail(tag, id, code);
                    break;
            }
        }
    };
}
