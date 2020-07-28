package queue;

// inv: e.size >= 0 && forall 0 <= i < e.size: ei != null
// e = [e1, ..., en] -- queue

public class ArrayQueueModule {
    private static final int MIN_CAPACITY = 4;
    private static int head;
    private static int size;
    private static Object[] elements = new Object[MIN_CAPACITY];

    // Pre: x != null
    // Post: e' = [e1, ..., en, x]
    public static void enqueue(Object x) {
        assert x != null;

        ensureCapacity();
        elements[mod(head + size)] = x;
        size++;
    }

    // Pre: e.size > 0
    // Post: R = e1 && e' = [e2, ..., en]
    public static Object dequeue() {
        assert size > 0;

        Object result = element();
        elements[head] = null;
        head = mod(head + 1);
        size--;
        return result;
    }

    // Pre: e.size > 0
    // Post: R = e1 && e' = e
    public static Object element() {
        assert size > 0;

        return elements[head];
    }

    // Pre: --
    // Post: R = e.size && e' = e
    public static int size() {
        return size;
    }

    // Pre: --
    // Post: R = (e.size == 0) && e' = e
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: --
    // Post: e'.size = 0 && e = []
    public static void clear() {
        head = 0;
        size = 0;
        elements = new Object[MIN_CAPACITY];
    }

    // Pre: x != null
    // Post: e = [x, e1, ..., en]
    public static void push(Object x) {
        assert x != null;

        ensureCapacity();
        head = mod(head - 1);
        elements[head] = x;
        size++;
    }

    // Pre: e.size > 0
    // Post: R = en && e' = e
    public static Object peek() {
        assert size > 0;

        return get(size - 1);
    }

    // Pre: e.size > 0
    // Post: R = en && e = [e1, ..., e(n - 1)]
    public static Object remove() {
        assert size > 0;

        Object result = peek();
        elements[mod(head + size - 1)] = null;
        size--;
        return result;
    }

    // Pre: e.size > 0 && 0 <= ind < size
    // Post: R = e(ind + 1) && e' = e
    public static Object get(int ind) {
        assert 0 <= ind && ind < size;

        return elements[mod(head + ind)];
    }

    // Pre: e.size > 0 && 0 <= ind < size && x != null
    // Post: e'(ind + 1) = x && forall i != ind: e'(i) = e(i)
    public static void set(int ind, Object x) {
        assert x != null && ind >= 0 && size > ind;

        elements[mod(head + ind)] = x;
    }


    // Pre: --
    // Post: e' = e && (size == e.capacity <=> 2 * size == e'.capacity)
    private static void ensureCapacity() {
        if (size == elements.length) {
            int tail = mod(head + size);
            Object[] tmp = new Object[size * 2];
            if (head < tail) {
                System.arraycopy(elements, head, tmp, 0, size);
            } else {
                System.arraycopy(elements, head, tmp, 0, size - head);
                System.arraycopy(elements, 0, tmp, size - head, tail);
            }
            elements = tmp;
            head = 0;
        }
    }

    // Pre: -e.capacity <= ind && ind = integer
    // Post: 0 <= R < e.capacity && exist k : |R - ind| == k * e.capacity && k = integer
    private static int mod(int ind) {
        return (ind + elements.length) % elements.length;
    }
}
