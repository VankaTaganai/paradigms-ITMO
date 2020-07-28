package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    protected void enqueueImpl(Object x) {
        Node tmp = tail;
        tail = new Node(x, null);
        if (size == 0) {
            head = tail;
        } else {
            tmp.next = tail;
        }
    }

    public Object elementImpl() {
        return head.value;
    }

    protected void remove() {
        head = head.next;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private class Node {
        private Object value;
        private Node next;

        private Node(Object value, Node next) {
            this.next = next;
            this.value = value;
        }
    }

}
