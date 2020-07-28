package queue;

import java.util.function.Function;
import java.util.function.Predicate;

// inv: e.size >= 0 && forall 0 <= i < e.size: ei != null && e = [e1, ..., en]
public interface Queue {

    // Pre: x != null
    // Post: e' = [e1, ..., en, x]
    void enqueue(Object x);

    // Pre: e.size > 0
    // Post: R = e1 && e' = e
    Object element();

    // Pre: e.size > 0
    // Post: R = e1 && e' = [e2, ..., en]
    Object dequeue();

    // Pre: --
    // Post: R = e.size && e' = e
    int size();

    // Pre: --
    // Post: R = (e.size == 0) && e' = e
    boolean isEmpty();

    // Pre: --
    // Post: e'.size = 0 && e = []
    void clear();

    // Pre: --
    // Post: e' = [ei1 .. eik] | forall i : p(e[i]) == true <=> e[i] in e' && forall 1 <= j <= k: i(j) < i(j + 1)
    void removeIf(Predicate p);

    // Pre: --
    // Post: e' = [ei1 .. eik] | forall i : p(e[i]) == false <=> e[i] in e' && forall 1 <= j <= k: i(j) < i(j + 1)
    void retainIf(Predicate p);

    // Pre: --
    // ek
    // Post: e' = [e1 .. ei] | forall 1 <= j <= i : p(e[j]) == true && (i == size || p(e[i + 1]) == false)
    void takeWhile(Predicate p);

    // Pre: --
    // Post: e' = [ei .. en] | forall 1 <= j <= i : p(e[j]) == false && (i + 1 > size || p(e[i + 1]) == true)
    void dropWhile(Predicate p);
}
