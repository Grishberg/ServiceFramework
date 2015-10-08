package info.goodline.framework.service;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import info.goodline.framework.interfaces.ThreadObserver;
import info.goodline.framework.multithreading.PriorityFuture;
import info.goodline.framework.multithreading.PriorityRunnable;

/**
 * Created by g on 07.10.15.
 */
public abstract class BaseThreadPoolService extends BaseBinderService
        implements ThreadObserver {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = NUMBER_OF_CORES ;
    protected volatile Map<String, SparseArray<Future>> mTaskQueue;
    protected ThreadPoolExecutor mExecutor;

    public BaseThreadPoolService() {
        mExecutor = getPriorityExecutor(CORE_POOL_SIZE);
        mTaskQueue = new HashMap<>(CORE_POOL_SIZE);
    }

    public static ThreadPoolExecutor getPriorityExecutor(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(10, PriorityFuture.COMP)) {
            @Override
            protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
                RunnableFuture newTaskFor = super.newTaskFor((PriorityRunnable)runnable, value);
                //return super.newTaskFor(runnable, value);
                return new PriorityFuture(newTaskFor, ((PriorityRunnable) runnable).getPriority());
            }

            @Override
            protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
                return super.newTaskFor(callable);
            }

            protected <T> RunnableFuture<T>  newTaskFor(Runnable runnable) {
                RunnableFuture newTaskFor = super.newTaskFor(runnable, null);
                return new PriorityFuture(newTaskFor, ((PriorityRunnable) runnable).getPriority());
            }
        };
    }

    /**
     * removes task from queue when it's done
     *
     * @param id
     */
    @Override
    public void onTaskDone(String tag, int id) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            queue.remove(id);
        }
    }

    /**
     * stop task with tag and id
     *
     * @param tag
     * @param id
     */
    protected synchronized void cancelQueueResponse(String tag, int id) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            Future task = queue.get(id);
            if (task != null && !task.isCancelled()) {
                task.cancel(true);
                queue.remove(id);
            }
        }
    }

    /**
     * stop all tasks and remove from queue
     *
     * @param tag
     */
    protected synchronized void cancelQueueResponse(String tag) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            for (int i = queue.size() - 1; i >= 0; i--) {
                int key = queue.keyAt(i);
                // get the object by the key.
                Future task = queue.get(key);
                if (!task.isCancelled()) {
                    task.cancel(true);
                }
                queue.removeAt(i);
            }
        }
    }

    protected synchronized void cancelAll() {
        for (String tag : mTaskQueue.keySet()) {
            SparseArray<Future> queue = mTaskQueue.get(tag);
            if (queue != null) {
                for (int i = queue.size() - 1; i >= 0; i--) {
                    int key = queue.keyAt(i);
                    // get the object by the key.
                    Future task = queue.get(key);
                    if (task != null && !task.isCancelled()) {
                        task.cancel(true);
                    }
                    queue.removeAt(i);
                }
            }
            mTaskQueue.remove(tag);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExecutor != null){
            mExecutor.shutdown();
            mExecutor.shutdownNow();
        }
    }
}
