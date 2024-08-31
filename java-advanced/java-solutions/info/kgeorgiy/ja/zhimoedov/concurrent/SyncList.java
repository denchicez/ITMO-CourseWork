package info.kgeorgiy.ja.zhimoedov.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Synchronized elements in list and return all completes element
 */
public class SyncList<T> {
    private final List<T> resultsQueue;
    private final int size;
    private int elementsInNow = 0;
    boolean haveException = false;

    /**
     * Construct synchronized list with capacity {@code size}
     *
     * @param size capacity of list
     */
    public SyncList(int size) {
        this.size = size;
        this.resultsQueue = new ArrayList<>(Collections.nCopies(size, null));
    }

    /**
     * Synchronized elements in list and return all completes element
     *
     * @param index   of element in array
     * @param element need set in index
     */
    synchronized public void set(int index, T element) {
        if (element == null) {
            haveException = true;
        }
        resultsQueue.set(index, element);
        elementsInNow += 1;
        if (elementsInNow == size) {
            notify();
        }
    }

    /**
     * Synchronized elements in list and return all completes element
     *
     * @return list of all complete elements
     */
    synchronized public List<T> get() throws InterruptedException {
        while (elementsInNow < size) {
            wait();
        }
        if (haveException) {
            throw new RuntimeException();
        }
        return resultsQueue;
    }
}