package queue;

// inv: e != null && e != null && e.size >= 0 && forall 0 <= i < e.size: ei != null && e = [e1, ..., en]
public class ArrayQueueADT {
    private final int MIN_CAPACITY = 4;
    private int head;
    private int size;
    private Object[] elements = new Object[MIN_CAPACITY];

    // Pre: x != null
    // Post: e' = [e1, ..., en, x]
    public static void enqueue(ArrayQueueADT queue, Object x) {
        assert x != null;

        ensureCapacity(queue);
        queue.elements[getIndex(queue, queue.head + queue.size)] = x;
        queue.size++;
    }

    // Pre: e.size > 0
    // Post: R = e1 && e' = [e2, ..., en]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object result = element(queue);
        queue.elements[queue.head] = null;
        queue.head = getIndex(queue, queue.head + 1);
        queue.size--;
        return result;
    }

    // Pre: e.size > 0
    // Post: R = e1 && e' = e
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.head];
    }

    // Pre: --
    // Post: R = e.size && e' = e
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: --
    // Post: R = (e.size == 0) && e' = e
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: --
    // Post: e'.size = 0 && e = []
    public static void clear(ArrayQueueADT queue) {
        queue.head = 0;
        queue.size = 0;
        queue.elements = new Object[queue.MIN_CAPACITY];
    }

    // Pre: x != null
    // Post: e = [x, e1, ..., en]
    public static void push(ArrayQueueADT queue, Object x) {
        assert x != null;

        ensureCapacity(queue);
        queue.head = getIndex(queue, queue.head - 1);
        queue.elements[queue.head] = x;
        queue.size++;
    }

    // Pre: e.size > 0
    // Post: R = en && e' = e
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;

        return get(queue, queue.size - 1);
    }

    // Pre: e.size > 0
    // Post: R = en && e = [e1, ..., e(n - 1)]
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object result = peek(queue);
        queue.elements[getIndex(queue, queue.head + queue.size - 1)] = null;
        queue.size--;
        return result;
    }

    // Pre: e.size > 0 && 0 <= ind < size
    // Post: R = e(ind + 1) && e' = e
    public static Object get(ArrayQueueADT queue, int ind) {
        assert ind >= 0 && queue.size > ind;

        return queue.elements[getIndex(queue, queue.head + ind)];
    }

    // Pre: e.size > 0 && 0 <= ind < size && x != null
    // Post: e'(ind + 1) = x && forall i != ind: e'(i) = e(i)
    public static void set(ArrayQueueADT queue, int ind, Object x) {
        assert x != null && ind >= 0 && queue.size > ind;

        queue.elements[getIndex(queue, queue.head + ind)] = x;
    }

    // Pre: --
    // Post: e' = e && (size == e.capacity <=> 2 * size == e'.capacity)
    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.size == queue.elements.length) {
            int tail = getIndex(queue, queue.head + queue.size);
            Object[] tmp = new Object[queue.size * 2];
            if (queue.head < tail) {
                System.arraycopy(queue.elements, queue.head, tmp, 0, queue.size);
            } else {
                System.arraycopy(queue.elements, queue.head, tmp, 0, queue.size - queue.head);
                System.arraycopy(queue.elements, 0, tmp, queue.size - queue.head, tail);
            }
            queue.elements = tmp;
            queue.head = 0;
        }
    }

    // Pre: -e.capacity <= ind && ind = integer
    // Post: 0 <= R < e.capacity && exist k : |R - ind| == k * e.capacity && k = integer
    private static int getIndex(ArrayQueueADT queue, int ind) {
        return (ind + queue.elements.length) % queue.elements.length;
    }
}
