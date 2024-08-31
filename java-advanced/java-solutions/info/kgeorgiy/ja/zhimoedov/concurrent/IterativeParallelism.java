package info.kgeorgiy.ja.zhimoedov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class IterativeParallelism implements ScalarIP {
    final private ParallelMapper mapper;

    private <T, R> R answer(int threads, List<? extends T> values,
                            Function<Stream<? extends T>, R> action,
                            Function<Stream<? extends R>, R> actionSublist) throws InterruptedException {
        threads = Math.min(threads, values.size());
        if (threads < 1) {
            throw new IllegalArgumentException("values size and threads MUST be more than 1");
        }
        int blockSize = values.size() / threads;
        int blockAdd = values.size() % threads;
        List<R> answers;
        List<Thread> workers = new ArrayList<>();
        int left = 0;
        List<Stream<? extends T>> subValues = new ArrayList<>();
        while (left != values.size()) {
            int needAdd = 0;
            if (blockAdd != 0) {
                needAdd = 1;
                blockAdd--;
            }
            int right = Math.min(left + blockSize + needAdd, values.size());
            subValues.add(values.subList(left, right).stream());
            left = right;
        }
        if (this.mapper == null) {
            answers = new ArrayList<>(Collections.nCopies(threads, null));
            for (int i = 0; i < subValues.size(); i++) {
                final int finalI = i;
                Thread thread = new Thread(() -> {
                    answers.set(finalI, action.apply(subValues.get(finalI)));
                }
                );
                thread.start();
                workers.add(thread);
            }
        } else {
            answers = mapper.map(action, subValues);
        }

        boolean flag = false;
        for (Thread work : workers) { // check all complete
            try {
                work.join();
            } catch (InterruptedException e) {
                work.interrupt();
                flag = true;
                System.err.println(e);
            }
        }
        if (flag) {
            throw new InterruptedException();
        }
        return actionSublist.apply(answers.stream());
    }


    public IterativeParallelism() {
        this.mapper = null;
    }
    
    public IterativeParallelism(final ParallelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Returns maximum element in {@code values}.
     * <p>
     * divides the array by the number of threads and calculates the values
     * on the subarrays, after which it calculates the values on the given results
     *
     * @param threads    count threads to treatment
     * @param values     to treatment
     * @param comparator compare values
     * @throws InterruptedException If wrong some bads in threads
     */
    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws
            InterruptedException {
        return answer(threads, values,
                stream -> stream.max(comparator).orElseThrow(),
                stream -> stream.max(comparator).orElseThrow());
    }

    /**
     * Returns minimum element in {@code values}.
     * <p>
     * divides the array by the number of threads and calculates the values
     * on the subarrays, after which it calculates the values on the given results
     *
     * @param threads    count threads to treatment
     * @param values     to treatment
     * @param comparator compare values
     * @throws InterruptedException If wrong some bads in threads
     */
    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws
            InterruptedException {
        return answer(threads, values,
                stream -> stream.min(comparator).orElseThrow(),
                stream -> stream.min(comparator).orElseThrow());
    }


    /**
     * Returns all elements in {@code values} that satisfy the predicate.
     * <p>
     * divides the array by the number of threads and calculates the values
     * on the subarrays, after which it calculates the values on the given results
     *
     * @param threads   count threads to treatment
     * @param values    to treatment
     * @param predicate predicate is valid value
     * @throws InterruptedException If wrong some bads in threads
     */
    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws
            InterruptedException {
        return answer(threads, values,
                stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(Boolean::booleanValue));
    }

    /**
     * Returns any element in {@code values} that satisfy the predicate.
     * <p>
     * divides the array by the number of threads and calculates the values
     * on the subarrays, after which it calculates the values on the given results
     *
     * @param threads   count threads to treatment
     * @param values    to treatment
     * @param predicate predicate is valid value
     * @throws InterruptedException If wrong some bads in threads
     */
    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws
            InterruptedException {
        return answer(threads, values,
                stream -> stream.anyMatch(predicate),
                stream -> stream.anyMatch(Boolean::booleanValue));
    }


    /**
     * Returns count of all elements in {@code values} that satisfy the predicate.
     * <p>
     * divides the array by the number of threads and calculates the values
     * on the subarrays, after which it calculates the values on the given results
     *
     * @param threads   count threads to treatment
     * @param values    to treatment
     * @param predicate predicate is valid value
     * @throws InterruptedException If wrong some bads in threads
     */
    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws
            InterruptedException {
        return answer(threads, values,
                stream -> (int) stream.filter(predicate).count(),
                stream -> stream.mapToInt(Integer::intValue).sum());
    }
}
