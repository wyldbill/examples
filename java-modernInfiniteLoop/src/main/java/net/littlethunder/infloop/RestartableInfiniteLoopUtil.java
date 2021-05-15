package net.littlethunder.infloop;

import lombok.extern.log4j.Log4j2;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A utility which runs a supplied Callable repeatedly in series, supplying the results of executions
 * via BlockingQueue or endless Stream.
 * @param <T> The type produced by
 */
@Log4j2
public class RestartableInfiniteLoopUtil<T> extends InfiniteLoopUtil<T>{
    private final Callable<T> callable;
    private AtomicBoolean stop = new AtomicBoolean(false);
    private final Lock lock = new ReentrantLock();


    /**
     * Private Constructor, which starts the task scheduling. An instance is created to hold
     * the output BlockingQueue for future use.
     * @param task the Callable to execute repeatedly in a serial fashion.
     */
    private RestartableInfiniteLoopUtil(Callable<T> task) {
        super(task);
        callable = task; //stored to enable a re-start
    }

    /**
     * Stops additional subsequent tasks from being scheudled. Does not stop a currently running instance,
     * and will probably allow one subsequent execution.
     */
    public void stop() {
        try {
            lock.lock();
            stop.set(Boolean.TRUE); //set as volatile - see JavaDoc
        } finally {
            lock.unlock();
        }
    }

    /**
     * Not locked to optimize execution. No guarantee that a stop or start takes effect immediately.
     * @return a boolean - true if a subsequent task should be scheduled
     */
    @Override
    protected boolean makeNextTask() {
        return !stop.get();
    }

    /**
     * Restart the Loop. Note that a Lock is used to prevent a race condition which MIGHT result in additional
     * task(s) being queued. This has no functional impact, but MIGHT cause a memory issue if the
     * race condition is hit many many times....
     */
    public void start() {
        try {
            lock.lock();
            if (stop.compareAndSet(true, false)){ //restart if stopped
                makeTask(callable);
            }
        } finally {
            lock.unlock();
        }
    }
}
