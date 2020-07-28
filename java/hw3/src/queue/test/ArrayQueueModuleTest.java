package queue.test;

import queue.ArrayQueueModule;

public class ArrayQueueModuleTest {
    private static int SIZE = 1000;

    public static void fill() {
        for (int i = 0; i < SIZE; i++) {
            ArrayQueueModule.enqueue(i);
            System.out.println(ArrayQueueModule.peek());
            System.out.println(ArrayQueueModule.peek());

        }
    }

    public static void print() {
        for (int i = 0; i < ArrayQueueModule.size(); i++) {
            System.out.println(ArrayQueueModule.get(i));
        }
    }

    public static void set() {
        for (int i = 0; i < ArrayQueueModule.size(); i++) {
            ArrayQueueModule.set(i, 1000 - i);
        }
    }

    public static void dump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.size() + " " +
                    ArrayQueueModule.element() + " " + ArrayQueueModule.dequeue());
            if (!ArrayQueueModule.isEmpty()) {
                System.out.println(ArrayQueueModule.size() + " " +
                        ArrayQueueModule.peek() + " " + ArrayQueueModule.remove());
            }
        }
    }

    public static void main(String[] args) {
        fill();
        print();
        set();
        dump();
    }
}
