package info.goodline.framework.multithreading;

import java.util.concurrent.Future;

/**
 * Created by g on 13.10.15.
 */
public class FutureContainer {
    public Future future;
    public PriorityRunnable runnable;
    public boolean isDelayed;

    public FutureContainer(Future future, PriorityRunnable runnable) {
        this.future = future;
        this.runnable = runnable;
    }
}
