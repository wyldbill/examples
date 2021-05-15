package net.littlethunder.infloop;

import lombok.extern.log4j.Log4j2;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public final class ModernInfiniteLoop {

    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final Random rand = new Random();
    private final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        new ModernInfiniteLoop().go();
    }

    private void go() throws InterruptedException{
        log.info("Submitting Task#:{}",counter.incrementAndGet());
        exec.submit(makeTask(counter.get()));
        exec.awaitTermination(10, TimeUnit.MINUTES);
    }

    private Callable<String> makeTask(int id) {
        return () -> {
            if (counter.get() > 20){
                return "Done";
            }
                log.info("Running Task#:{} - Submitting Task#:{}", id,counter.incrementAndGet());
                exec.submit(makeTask(counter.get())); //submit a task instance to run after this one
                taskBody(id);
                return "Finished";
        };
    }

    private void taskBody(int id) {
        log.info("Starting Task #:{}...",id);
        try {
            Thread.sleep(rand.nextInt(2000) + rand.nextInt(2000)); //2,000-10,000
        } catch (InterruptedException e){
            log.error("Interrupted while waiting!",e);
            Thread.currentThread().interrupt();
        }
        log.info("Task#:{} Complete!",id);
    }

}