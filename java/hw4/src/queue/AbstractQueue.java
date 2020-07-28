package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void enqueueImpl(Object x);

    public void enqueue(Object x) {
        assert x != null;

        enqueueImpl(x);
        size++;
    }

    protected abstract void remove();

    public Object dequeue() {
        assert size > 0;

        Object result = element();
        remove();
        size--;
        return result;
    }

    protected abstract Object elementImpl();

    public Object element() {
        assert size > 0;

        return elementImpl();
    }

    public void removeIf(Predicate p) {
        int tmp = size;
        for (int i = 0; i < tmp; i++) {
            Object x = dequeue();
            if (!p.test(x)) {
                enqueue(x);
            }
        }
    }

    public void retainIf(Predicate p) {
        removeIf(p.negate());
    }

    private void takeOrDropWhile(Predicate p, boolean start_flag) {
        boolean flag = start_flag;
        int tmp = size;
        for (int i = 0; i < tmp; i++) {
            Object x = dequeue();
            if (!p.test(x)) {
                flag = !start_flag;
            }
            if (flag) {
                enqueue(x);
            }
        }
    }

    public void takeWhile(Predicate p) {
        takeOrDropWhile(p, true);
    }

    public void dropWhile(Predicate p) {
        takeOrDropWhile(p, false);
    }
}
