package queue;

public class ArrayQueue extends AbstractQueue {
    private final int MIN_CAPACITY = 4;
    private int head;
    private Object[] elements = new Object[MIN_CAPACITY];

    public void enqueueImpl(Object x) {
        ensureCapacity();
        elements[getIndex(size)] = x;
    }

    protected void remove() {
        elements[head] = null;
        head = getIndex(1);
    }

    public Object elementImpl() {
        return elements[head];
    }

    public void clear() {
        head = 0;
        size = 0;
        elements = new Object[MIN_CAPACITY];
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int tail = getIndex(size);
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

    private int getIndex(int ind) {
        return (head + ind + elements.length) % elements.length;
    }
}
