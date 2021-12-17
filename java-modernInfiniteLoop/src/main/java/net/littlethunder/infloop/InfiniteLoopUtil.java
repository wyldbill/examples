package net.littlethunder.infloop;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A utility which runs a supplied Callable repeatedly in series, supplying the results of executions
 * via BlockingQueue or endless Stream.
 * @param <T> The type produced by
 */
@Log4j2
public class InfiniteLoopUtil<T> {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final BlockingQueue<Future<T>> output = new LinkedBlockingDeque<>();
    private final Callable<T> callable; //only stored for re-start...
    @Setter(AccessLevel.PROTECTED)
    private long timeout = 60;
    @Setter(AccessLevel.PROTECTED)
    private TimeUnit units = TimeUnit.MINUTES;

    public static <T> BlockingQueue<Future<T>> queueLoop(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException("Cannot provide null task to loop()");
        }
        return new InfiniteLoopUtil<>(task).output;
    }

    public static <T> Stream<Future<T>> streamLoop(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException("Cannot provide null task to loop()");
        }
        return new InfiniteLoopUtil<>(task).stream();
    }

    protected boolean makeNextTask(){
        return true;
    }

    /**
     * Protected Constructor, which starts the task scheduling. An instance is created to hold
     * the output BlockingQueue for future use.
     * @param task the Callable to execute repeatedly in a serial fashion.
     */
    protected InfiniteLoopUtil(Callable<T> task) {
        callable = task; //stored to enable a re-start
        try {
            output.offer(exec.submit(makeTask(callable)));
        } catch (IllegalStateExceptiona e) {
            Thread.currentThread().interrupt();
            log.error("Cannot put Future into return Queue.", e);
            throw new IllegalStateException("Something is very wrong with the output queue. It is blocked for some reason.");
        }
    }

    /**
     * The Stream returned never terminates and is best utilized with non-terminal methods
     * @return A Stream of Futures containing the results of the executions. .
     */
    private Stream<Future<T>> stream() {
        return StreamSupport.stream(new splitMe<>(output), false);
    }

    /**
     * This seems recursive, isn't. The internal self-call is deferred until execution; no more than one
     * invocation exists at a time.
     * @param task the task to reschedule for repetition then run.
     * @return An encapsulating Callable
     */
    protected Callable<T> makeTask(Callable<T> task) {
        return () -> {
            if (makeNextTask()){
                output.put(exec.submit(makeTask(task))); //submit a task instance to run after this one
            }
            return task.call();
        };
    }

    /**
     * A Spliterator implementation used to provide the Stream functionality.
     * A private non-static class. Instances have visibility to their parents.
     * @param <U> The generic class of the items contained by the Spliterator.
     *           This is the same as T in the parent, but named differently to prevent hiding.
     */
    private class splitMe<U> extends Spliterators.AbstractSpliterator<U> {
        BlockingQueue<U> q;

        protected splitMe(BlockingQueue<U> out) {
            super(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.ORDERED);
            q = out;
        }

        @Override
        public boolean tryAdvance(Consumer<? super U> action) {
            try {
                final U next = q.poll(timeout, units);
                if (next == null) {
                    return false;
                }
                action.accept(next);
                return true;
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("interrupted", e);
            }
        }
    }
}
