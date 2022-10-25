package queue;

public class QueueCastomTest {
    public static void main(String[] args) {
        //ArrayQueue
        System.out.println("ArrayQueue");
        ArrayQueue queue = new ArrayQueue();
        queue.enqueue(1);
        for (int i = 1; i < 100; i++) queue.enqueue(i);
        System.out.println(queue.count(1));
        System.out.println(queue.dequeue());
        System.out.println(queue.element());
        queue.clear();
        queue.enqueue(1);
        for (int i = 1; i < 100; i++) queue.enqueue(i);
        System.out.println(queue.size());
        System.out.println(queue.isEmpty());
        System.out.println(queue.count(2));
        //ArrayQueueADT
        System.out.println("ArrayQueueADT");
        ArrayQueueADT queueADT = new ArrayQueueADT();
        queueADT.enqueue(queueADT, 1);
        for (int i = 1; i < 100; i++) queueADT.enqueue(queueADT, i);
        System.out.println(queueADT.count(queueADT, 1));
        System.out.println(queueADT.dequeue(queueADT));
        System.out.println(queueADT.element(queueADT));
        queueADT.clear(queueADT);
        queueADT.enqueue(queueADT, 1);
        for (int i = 1; i < 100; i++) queueADT.enqueue(queueADT, i);
        System.out.println(queueADT.size(queueADT));
        System.out.println(queueADT.isEmpty(queueADT));
        System.out.println(queueADT.count(queueADT, 2));
        //ArrayQueueModule
        System.out.println("ArrayQueueModule");
        ArrayQueueModule queueModule = new ArrayQueueModule();
        queueModule.enqueue(1);
        for (int i = 1; i < 100; i++) queueModule.enqueue(i);
        System.out.println(queueModule.count(1));
        System.out.println(queueModule.dequeue());
        System.out.println(queueModule.element());
        queueModule.clear();
        queueModule.enqueue(1);
        for (int i = 1; i < 100; i++) queueModule.enqueue(i);
        System.out.println(queueModule.size());
        System.out.println(queueModule.isEmpty());
        System.out.println(queueModule.count(2));
    }
}
