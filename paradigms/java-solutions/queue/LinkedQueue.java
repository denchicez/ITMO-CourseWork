package queue;


public class LinkedQueue extends AbstractQueue {
    //private int size = 0;
    private Node tail = new Node(null, null);
    private Node head = new Node(null, tail);
    private Node toFast;

    public void enqueueRealization(Object element) {
        if (size == 1) {
            head.value = element;
        } else {
            tail.value = element;
            tail.next = new Node(null, null);
            tail = tail.next;
        }
    }

    public Object dequeueRealization() {
        Object out = head.value;
        head = head.next;
        if (size == 1) {
            clear();
            size = 1;
        }
        return out;
    }

    public Object element() {
        assert size > 0;
        return head.value;
    }

    //public int size() {
    //    return size;
    //}

    public void clearRealization() {
        tail = new Node(null, null);
        head = new Node(null, tail);
    }

    //public boolean isEmpty() {
    //    return size == 0;
    //}
    public Object nextElement(int i) {
        if (i == 0) toFast = head;
        if (i != 0) toFast = toFast.next;
        return toFast.value;
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
