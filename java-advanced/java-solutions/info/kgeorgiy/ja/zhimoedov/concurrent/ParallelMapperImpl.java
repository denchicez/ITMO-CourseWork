package info.kgeorgiy.ja.zhimoedov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final List<Thread> workers;

    /**
     * Start workers for map function
     *
     * @param threads count threads to treatment
     */
    public ParallelMapperImpl(int threads) {
        if (threads < 1) {
            throw new IllegalArgumentException("threads MUST be more than 1");
        }
        workers = new ArrayList<>(Collections.nCopies(threads, null));
        for (int threadIndex = 0; threadIndex < threads; threadIndex++) {
            Thread threadMain = new Thread(() -> {
                try {
                    while (true) {
                        solve();
                    }
                } catch (InterruptedException e) {
                    // interrupt!!!
                } finally {
                    Thread.currentThread().interrupt();
                }
            });
            workers.set(threadIndex, threadMain);
            threadMain.start();
        }
    }

    private void solve() throws InterruptedException {
        Runnable task;
        synchronized (tasks) {
            while (tasks.isEmpty()) {
                tasks.wait();
            }
            task = tasks.poll();
        }
        task.run();
    }

    /**
     * Convert {@code args} by {@code func} to new array.
     *
     * @param func convert function
     * @param args array which need convert
     * @return new array from func(args)
     * @throws InterruptedException If wrong some bads in threads
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> func,
                              List<? extends T> args) throws InterruptedException {
        SyncList<R> resultSyncList = new SyncList<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            final int finalI = i;
            synchronized (tasks) {
                tasks.add(() -> {
                    R resultArgs;
                    try {
                        resultArgs = func.apply(args.get(finalI));
                    } catch (RuntimeException e) {
                        System.err.println(e);
                        resultArgs = null;
                    }
                    resultSyncList.set(finalI, resultArgs);
                });
                tasks.notify();
            }
        }
        return resultSyncList.get();
    }

    /**
     * Close all worker threads.
     */
    @Override
    public void close() {
        workers.forEach(Thread::interrupt);
        for (Thread work : workers) { // check all complete
            try {
                work.join();
            } catch (InterruptedException e) {
                work.interrupt();
                System.err.println(e);
            }
        }
    }
}
