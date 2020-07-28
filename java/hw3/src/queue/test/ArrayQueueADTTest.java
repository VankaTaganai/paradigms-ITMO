package queue.test;

import queue.ArrayQueue;
import queue.ArrayQueueADT;

public class ArrayQueueADTTest {
    private static int SIZE = 1000;

    public static void fill(ArrayQueueADT queue) {
        for (int i = 0; i < SIZE; i++) {
            ArrayQueueADT.enqueue(queue, i);
            System.out.println(ArrayQueueADT.peek(queue));
            System.out.println(ArrayQueueADT.peek(queue));

        }
    }

    public static void print(ArrayQueueADT queue) {
        for (int i = 0; i < ArrayQueueADT.size(queue); i++) {
            System.out.println(ArrayQueueADT.get(queue, i));
        }
    }

    public static void set(ArrayQueueADT queue) {
        for (int i = 0; i < ArrayQueueADT.size(queue); i++) {
            ArrayQueueADT.set(queue, i, 1000 - i);
        }
    }

    public static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(ArrayQueueADT.size(queue) + " " +
                    ArrayQueueADT.element(queue) + " " + ArrayQueueADT.dequeue(queue));
            if (!ArrayQueueADT.isEmpty(queue)) {
                System.out.println(ArrayQueueADT.size(queue) + " " +
                        ArrayQueueADT.peek(queue) + " " + ArrayQueueADT.remove(queue));
            }
        }
    }

    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        fill(queue);
        print(queue);
        set(queue);
        dump(queue);
    }
}
