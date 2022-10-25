package queue;

public class ArrayQueue extends AbstractQueue {
    //private int size = 0;
    private int tail = 0;
    private int head = 0;
    private Object[] elements = new Object[32];

    //PRE: element!=null
    //POST: size` == size+1 && head` == head+1 && size`< elements.length && elements[head` % elements.length] == element
    public void enqueueRealization(Object element) {
        ensureCapacity(size);
        elements[head++ % elements.length] = element;
    }

    //PRE: capcity > 0
    //POST: size<elements.length && (head`==capcity-1 || head`==head) && (tail==0 || tail`==tail)
    // len(elements`) == len(elements) || len(elements`) == len(elements) * 2
    private void ensureCapacity(int capacity) {
        if (capacity >= elements.length) {
            Object[] elementsNew = new Object[2 * capacity];
            for (int i = tail; i < head; i++) {
                elementsNew[i - tail] = elements[i % elements.length];
            }
            head = capacity - 1;
            tail = 0;
            elements = elementsNew;
        }
    }

    //PRE: size > 0
    //POST: size`==size-1 && tail`=tail+1 && elements[tail` % elements.length] == null
    // res == elements[tail` % elements.length]
    public Object dequeueRealization() {
        Object value = element();
        elements[tail++ % elements.length] = null;
        return value;
    }

    //PRE: size > 0
    //POST: res == elements[tail` % elements.length]
    public Object element() {
        assert size > 0;
        return elements[tail % elements.length];
    }

    //PRE: True
    //Post: res == size
    //public int size() {
    //    return size;
    //}

    //PRE: size = head = tail = 0
    //POST: elements.length==1024 && for i in elements : elements[i] != null
    public void clearRealization() {
        head = tail = 0;
        elements = new Object[32];
    }

    //PRE: True
    //POST: res == (size==0)
    //public boolean isEmpty() {
    //    return size == 0;
    //}

    //PRE: findElement!=null
    //POST: forall i=tail..head if(arr[i]==x): l = min(l, i)
    public int count(Object findElement) {
        int counter = 0;
        for (int i = tail; i < head; i++) {
            if (elements[i % elements.length].equals(findElement)) counter++;
        }
        return counter;
    }

    public Object nextElement(int i) {
        return elements[(i + tail) % elements.length];
    }

}
