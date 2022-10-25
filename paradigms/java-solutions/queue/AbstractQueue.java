package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void enqueueRealization(Object element);

    public void enqueue(Object element) {
        assert element != null;
        size++;
        enqueueRealization(element);
    }

    protected abstract Object dequeueRealization();

    public Object dequeue() {
        assert size > 0;
        Object out = dequeueRealization();
        size--;
        return out;
    }

    protected abstract void clearRealization();

    public void clear() {
        size = 0;
        clearRealization();
    }


    protected abstract Object nextElement(int i);

    public int countIf(Predicate<Object> element) {
        int counter = 0;
        for (int i = 0; i < size; i++) {
            if (element.test(nextElement(i))) counter++;
        }
        return counter;
    }
}
