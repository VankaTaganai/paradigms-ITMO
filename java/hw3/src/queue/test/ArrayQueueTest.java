package queue.test;

import queue.ArrayQueue;

public class ArrayQueueTest {
    private static int SIZE = 1000;

    public static void fill(ArrayQueue queue) {
        for (int i = 0; i < SIZE; i++) {
            queue.enqueue(i);
            System.out.println(queue.peek());
            System.out.println(queue.peek());

        }
    }

    public static void print(ArrayQueue queue) {
        for (int i = 0; i < queue.size(); i++) {
            System.out.println(queue.get(i));
        }
    }

    public static void set(ArrayQueue queue) {
        for (int i = 0; i < queue.size(); i++) {
            queue.set(i, 1000 - i);
        }
    }

    public static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.size() + " " +
                    queue.element() + " " + queue.dequeue());
            if (!queue.isEmpty()) {
                System.out.println(queue.size() + " " +
                        queue.peek() + " " + queue.remove());
            }
        }
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        fill(queue);
        print(queue);
        set(queue);
        dump(queue);
    }
}
