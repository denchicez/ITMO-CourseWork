package queue;

// :NOTE: wrong implementation

public class ArrayQueueADT {
    private int size = 0;
    private int tail = 0;
    private int head = 0;
    private Object[] elements = new Object[32];

    //PRE: element!=null && query!=null
    //POST: size` == size+1 && head` == head+1 && size`< elements.length && elements[head` % elements.length] == element
    public static void enqueue(ArrayQueueADT query, Object element) {
        assert element != null;
        ensureCapacity(query, ++query.size);
        query.elements[query.head++ % query.elements.length] = element;
    }

    //PRE: capcity > 0 && query!=null
    //POST: size<elements.length && (head`==capcity-1 || head`==head) && (tail==0 || tail`==tail)
    // len(elements`) == len(elements) || len(elements`) == len(elements) * 2
    private static void ensureCapacity(ArrayQueueADT query, int capacity) {
        if (capacity >= query.elements.length) {
            Object[] elementsNew = new Object[2 * capacity];
            for (int i = query.tail; i < query.head; i++) {
                elementsNew[i - query.tail] = query.elements[i % query.elements.length];
            }
            query.head = capacity - 1;
            query.tail = 0;
            query.elements = elementsNew;
        }
    }

    //PRE: size > 0 && query!=null
    //POST: size`==size-1 && tail`=tail+1 && elements[tail` % elements.length] == null
    // res == elements[tail` % elements.length]
    public static Object dequeue(ArrayQueueADT query) {
        assert query.size > 0;
        Object value = element(query);
        query.elements[query.tail++ % query.elements.length] = null;
        query.size--;
        return value;
    }

    //PRE: size > 0 && query!=null
    //POST: res == elements[tail` % elements.length]
    public static Object element(ArrayQueueADT query) {
        assert query.size > 0;
        return query.elements[query.tail % query.elements.length];
    }

    //PRE: query!=null
    //Post: res == size
    public static int size(ArrayQueueADT query) {
        return query.size;
    }

    //PRE: size = head = tail = 0 && query!=null
    //POST: elements.length==1024 && for i in elements : elements[i] != null
    public static void clear(ArrayQueueADT query) {
        query.size = query.head = query.tail = 0;
        query.elements = new Object[32];
    }

    //PRE: query!=null
    //POST: res == (size==0)
    public static boolean isEmpty(ArrayQueueADT query) {
        return query.size == 0;
    }

    //PRE: findElement!=null && query!=null
    //POST: forall i=tail..head if(elements[i % elements.length].equals(findElement)): res++
    // res >= 0
    public static int count(ArrayQueueADT query, Object findElement) {
        int counter = 0;
        for (int i = query.tail; i < query.head; i++) {
            if (query.elements[i % query.elements.length].equals(findElement)) counter++;
        }
        return counter;
    }
}
