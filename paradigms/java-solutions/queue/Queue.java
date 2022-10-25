package queue;

import java.util.function.Predicate;

// :NOTE: incomplete pre-/post- conditions;
// Immutable elements[0..size - 1]
public interface Queue {
    //PRE: element!=null
    //POST: size` == size+1 && elements`[size`] = element && len(elements`)=size`
    void enqueue(Object element);

    // :NOTE: stack behaviour
    //PRE: size>=1
    //POST: res == elements[size`] && size` == size - 1 && not_exist(elements[size`]) && len(elements)=size`
    Object dequeue();

    //PRE: size>=1
    //POST: elements[size]
    Object element();

    //PRE: True
    //Post: res == size
    int size();

    //PRE: True
    //POST: size = 0
    void clear();

    //PRE: True
    //POST: size==0
    boolean isEmpty();

    // :NOTE: incomplete/messy postconditions
    //PRE: element!=null
    //POST: for element_in in elements: res+=element.test(element_in)
    int countIf(Predicate<Object> element);
}
