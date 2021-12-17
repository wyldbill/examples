package net.littlethunder.infloop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InfiniteLoopUtilTest {
    Callable<String> c;

    @BeforeEach
    void setUp() {
        c = new Callable<String>() {
            AtomicInteger i = new AtomicInteger();

            @Override
            public String call() throws Exception {
                return "Count:" + i.getAndIncrement();
            }
        };
    }

    @AfterEach
    void tearDown() {
        c = null;
    }

    @Test
    void testQueueLoop() throws InterruptedException, ExecutionException {
        BlockingQueue<Future<String>> q = InfiniteLoopUtil.queueLoop(c);
        assertNotNull(q);
        assertEquals("Count:0", q.take().get());
        assertEquals("Count:1", q.take().get());
        assertEquals("Count:2", q.take().get());
        assertEquals("Count:3", q.take().get());
    }

    @Test
    void testQueueLoopWithNull() throws InterruptedException, ExecutionException {
        assertThrows(NullPointerException.class, () -> {
            BlockingQueue<Future<String>> q = InfiniteLoopUtil.queueLoop(null);
        });
    }

    @Test
    void streamLoop() throws ExecutionException, InterruptedException {
        Stream<Future<String>> s = InfiniteLoopUtil.streamLoop(c);
        assertNotNull(s);
        Deque<Future<String>> d = new LinkedList<>();
        s.limit(4).forEach(f -> d.push(f));
        assertEquals("Count:3", d.pop().get());
        assertEquals("Count:2", d.pop().get());
        assertEquals("Count:1", d.pop().get());
        assertEquals("Count:0", d.pop().get());

    }

    @Test
    void testStreamLoopWithNull() throws InterruptedException, ExecutionException {
        assertThrows(NullPointerException.class, () -> {
            Stream<Future<String>> q = InfiniteLoopUtil.streamLoop(null);
        });
    }

    @Test
    void makeNextTask() {
    }

    @Test
    void makeTask() {
    }
}