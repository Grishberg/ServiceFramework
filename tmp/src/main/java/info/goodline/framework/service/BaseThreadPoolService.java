package info.goodline.framework.service;

import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import info.goodline.framework.Const;
import info.goodline.framework.interfaces.ServiceThreadInteractionObserver;
import info.goodline.framework.interfaces.ThreadObserver;
import info.goodline.framework.multithreading.BaseTask;
import info.goodline.framework.multithreading.FutureContainer;
import info.goodline.framework.multithreading.PriorityFuture;
import info.goodline.framework.multithreading.PriorityRunnable;

/**
 * Created by g on 07.10.15.
 */
public abstract class BaseThreadPoolService extends BaseBinderService
        implements ServiceThreadInteractionObserver {
    private static final String TAG = BaseThreadPoolService.class.getSimpleName();
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = NUMBER_OF_CORES;
    protected volatile Map<String, SparseArray<FutureContainer>> mTaskQueue;
    protected ThreadPoolExecutor mExecutor;
    private PriorityBlockingQueue<Runnable> mQueue;
    private int mTaskId;

    public BaseThreadPoolService() {
        mQueue = new PriorityBlockingQueue<Runnable>(10, PriorityFuture.COMP);
        mExecutor = getPriorityExecutor(CORE_POOL_SIZE, mQueue);
        mTaskQueue = new HashMap<>(CORE_POOL_SIZE);
    }

    public static ThreadPoolExecutor getPriorityExecutor(int nThreads, PriorityBlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                queue) {
            @Override
            protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
                RunnableFuture newTaskFor = super.newTaskFor((PriorityRunnable) runnable, value);
                //return super.newTaskFor(runnable, value);
                return new PriorityFuture(newTaskFor, ((PriorityRunnable) runnable).getPriority());
            }

            @Override
            protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
                return super.newTaskFor(callable);
            }

            protected <T> RunnableFuture<T> newTaskFor(Runnable runnable) {
                RunnableFuture newTaskFor = super.newTaskFor(runnable, null);
                return new PriorityFuture(newTaskFor, ((PriorityRunnable) runnable).getPriority());
            }
        };
    }

    public int startManagedTask(BaseTask task, String tag) {
        int id = mTaskId++;
        task.setId(id);
        Future future = mExecutor.submit(task);
        SparseArray<FutureContainer> queue = mTaskQueue.get(tag);
        if (queue == null) {
            queue = new SparseArray<>(CORE_POOL_SIZE);
            mTaskQueue.put(tag, queue);
        }
        queue.put(id, new FutureContainer(future, task));
        return id;
    }

    /**
     * removes task from queue when it's done
     *
     * @param id
     */
    @Override
    public synchronized void onTaskDone(String tag, int id) {
        SparseArray<FutureContainer> queue = mTaskQueue.get(tag);
        if (queue != null) {
            FutureContainer task = queue.get(id);
            if (task != null) {
                if (!task.isDelayed) {
                    queue.remove(id);
                } else {
                    task.isDelayed = false;
                    Log.d(TAG, "task was delayed, need change flag");
                }
            }
        }
    }

    /**
     * stop task with tag and id
     *
     * @param tag
     * @param id
     */
    public synchronized void cancelTaskQueue(String tag, int id) {
        SparseArray<FutureContainer> queue = mTaskQueue.get(tag);
        if (queue != null) {
            FutureContainer task = queue.get(id);
            if (task != null && task.future != null && !task.future.isCancelled()) {
                task.future.cancel(true);
                queue.remove(id);
            }
        }
    }

    /**
     * stop all tasks and remove from queue
     *
     * @param tag
     */
    public synchronized void cancelTaskQueue(String tag) {
        SparseArray<FutureContainer> queue = mTaskQueue.get(tag);
        if (queue != null) {
            for (int i = queue.size() - 1; i >= 0; i--) {
                int key = queue.keyAt(i);
                // get the object by the key.
                FutureContainer task = queue.get(key);
                if (task != null && !task.future.isCancelled()) {
                    task.future.cancel(true);
                }
                queue.removeAt(i);
            }
        }
    }

    public synchronized void cancelAll() {
        Iterator it = mTaskQueue.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, SparseArray<FutureContainer>> item = (Map.Entry<String, SparseArray<FutureContainer>>) it.next();
            SparseArray<FutureContainer> queue = mTaskQueue.get(item.getKey());
            if (queue != null) {
                for (int i = queue.size() - 1; i >= 0; i--) {
                    int key = queue.keyAt(i);
                    // get the object by the key.
                    FutureContainer task = queue.get(key);
                    if (task != null && !task.future.isCancelled()) {
                        task.future.cancel(true);
                    }
                    queue.removeAt(i);
                }
            }
            it.remove();
        }
    }

    public synchronized void delayTaskQueue(String tag) {
        SparseArray<FutureContainer> queue = mTaskQueue.get(tag);
        if (queue != null) {
            for (int i = queue.size() - 1; i >= 0; i--) {
                int key = queue.keyAt(i);
                // get the object by the key.
                FutureContainer task = queue.get(key);
                if (task != null && !task.future.isCancelled() && !task.future.isDone()) {
                    // если задача еще не выполнена, отменить
                    task.future.cancel(true);
                    task.isDelayed = true;
                    mQueue.remove(task.runnable);
                    // и заново добавить в очередь
                    task.future = mExecutor.submit(task.runnable);
                }
                //queue.removeAt(i);
            }
        }
    }
    // send message

    @Override
    public void onSuccess(int taskId) {
        sendMessage(0, taskId);
    }

    @Override
    public void onSuccess(int taskId, int code) {
        sendMessage(Const.SERVICE_ACTION_TASK_DONE, code, taskId);
    }

    @Override
    public void onSuccess(int taskId, Serializable data) {
        sendMessage(Const.SERVICE_ACTION_TASK_DONE, 0, data, taskId);
    }

    @Override
    public void onSuccess(int taskId, Parcelable data) {
        sendMessage(Const.SERVICE_ACTION_TASK_DONE, 0, data, taskId);
    }

    @Override
    public void onFail(int taskId, int code) {
        sendMessage(Const.SERVICE_ACTION_TASK_FAIL, code, taskId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor.shutdownNow();
            try {
                if (!mExecutor.awaitTermination(100, TimeUnit.MICROSECONDS)) {
                    Log.d(TAG, "Still waiting...");
                }
            } catch (InterruptedException e) {
            }
        }
        cancelAll();
    }
}
