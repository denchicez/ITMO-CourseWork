package queue;

public class ArrayQueueModule {
    private static int size = 0;
    private static int tail = 0;
    private static int head = 0;
    private static Object[] elements = new Object[32];

    //PRE: element!=null
    //POST: size` == size+1 && head` == head+1 && size`< elements.length && elements[head` % elements.length] == element
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity(++size);
        elements[head++ % elements.length] = element;
    }

    //PRE: capcity > 0
    //POST: size<elements.length && (head`==capcity-1 || head`==head) && (tail==0 || tail`==tail)
    // len(elements`) == len(elements) || len(elements`) == len(elements) * 2
    private static void ensureCapacity(int capacity) {
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
    public static Object dequeue() {
        assert size > 0;
        Object value = element();
        elements[tail++ % elements.length] = null;
        size--;
        return value;
    }

    //PRE: size > 0
    //POST: res == elements[tail` % elements.length]
    public static Object element() {
        assert size > 0;
        return elements[tail % elements.length];
    }

    //PRE: True
    //Post: res == size
    public static int size() {
        return size;
    }

    //PRE: size = head = tail = 0
    //POST: elements.length==1024 && for i in elements : elements[i] != null
    public static void clear() {
        size = head = tail = 0;
        elements = new Object[32];
    }

    //PRE: True
    //POST: res == (size==0)
    public static boolean isEmpty() {
        return size == 0;
    }

    //PRE: findElement!=null
    //POST: forall i=tail..head if(elements[i % elements.length].equals(findElement)): res++
    // res >= 0
    public static int count(Object findElement) {
        int counter = 0;
        for (int i = tail; i < head; i++) {
            if (elements[i % elements.length].equals(findElement)) counter++;
        }
        return counter;
    }
}
