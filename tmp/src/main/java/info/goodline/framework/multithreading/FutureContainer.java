package info.goodline.framework.multithreading;

import java.util.concurrent.Future;

/**
 * Created by g on 13.10.15.
 * container for tasks
 */
public class FutureContainer {
    public Future future;
    public PriorityRunnable runnable;

    public FutureContainer(Future future, PriorityRunnable runnable) {
        this.future = future;
        this.runnable = runnable;
    }
}
