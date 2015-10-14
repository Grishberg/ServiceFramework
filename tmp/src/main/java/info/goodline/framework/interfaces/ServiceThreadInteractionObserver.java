package info.goodline.framework.interfaces;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by g on 13.10.15.
 */
public interface ServiceThreadInteractionObserver extends ThreadObserver {
    void onSuccess(int taskId);
    void onSuccess(int taskId, int code);
    void onSuccess(int taskId, Serializable data);
    void onSuccess(int taskId, Parcelable data);
    void onFail(int taskId, int code);
}
