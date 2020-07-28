package queue;

// inv: e.size >= 0 && forall 0 <= i < e.size: ei != null && e = [e1, ..., en]
public class ArrayQueue {
    private final int MIN_CAPACITY = 4;
    private int head;
    private int size;
    private Object[] elements = new Object[MIN_CAPACITY];

    // Pre: x != null
    // Post: e' = [e1, ..., en, x]
    public void enqueue(Object x) {
        assert x != null;

        ensureCapacity();
        elements[getIndex(head + size)] = x;
        size++;
    }

    // Pre: e.size > 0
    // Post: R = e1 && e' = [e2, ..., en]
    public Object dequeue() {
        assert size > 0;

        Object result = element();
        elements[head] = null;
        head = getIndex(head + 1);
        size--;
        return result;
    }

    // Pre: e.size > 0
    // Post: R = e1 && e' = e
    public Object element() {
        assert size > 0;

        return elements[head];
    }

    // Pre: --
    // Post: R = e.size && e' = e
    public int size() {
        return size;
    }

    // Pre: --
    // Post: R = (e.size == 0) && e' = e
    public boolean isEmpty() {
        return size == 0;
    }

    // Pre: --
    // Post: e'.size = 0 && e = []
    public void clear() {
        head = 0;
        size = 0;
        elements = new Object[MIN_CAPACITY];
    }

    // Pre: x != null
    // Post: e = [x, e1, ..., en]
    public void push(Object x) {
        assert x != null;

        ensureCapacity();
        head = getIndex(head - 1);
        elements[head] = x;
        size++;
    }

    // Pre: e.size > 0
    // Post: R = en && e' = e
    public Object peek() {
        assert size > 0;

        return get(size - 1);
    }

    // Pre: e.size > 0
    // Post: R = en && e = [e1, ..., e(n - 1)]
    public Object remove() {
        assert size > 0;

        Object result = peek();
        elements[getIndex(head + size - 1)] = null;
        size--;
        return result;
    }

    // Pre: e.size > 0 && 0 <= ind < size
    // Post: R = e(ind + 1) && e' = e
    public Object get(int ind) {
        assert 0 <= ind && ind < size;

        return elements[getIndex(head + ind)];
    }

    // Pre: e.size > 0 && 0 <= ind < size && x != null
    // Post: e'(ind + 1) = x && forall i != ind: e'(i) = e(i)
    public void set(int ind, Object x) {
        assert x != null && 0 <= ind && ind < size;

        elements[getIndex(head + ind)] = x;
    }

    // Pre: --
    // Post: e' = e && (size == e.capacity <=> 2 * size == e'.capacity)
    private void ensureCapacity() {
        if (size == elements.length) {
            int tail = getIndex(head + size);
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
    private int getIndex(int ind) {
        return (ind + elements.length) % elements.length;
    }
}
