package info.kgeorgiy.ja.zhimoedov.arrayset;

import java.util.*;

public class ArraySet<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {
    private final List<T> elements;
    private final Comparator<T> comparator;

    private ArraySet(List<T> elements, Comparator<T> comparator) { // O(1)
        this.elements = elements;
        this.comparator = comparator;
    }

    public ArraySet() { // O(1)
        this(new ArrayList<>(), Comparator.naturalOrder());
    }

    public ArraySet(Collection<T> arr) { // O(NlogN)
        this(new ArrayList<>(new TreeSet<>(arr)), Comparator.naturalOrder());
    }

    public ArraySet(Collection<T> arr, Comparator<T> comparator) { // O(NlogN)
        TreeSet<T> treesets = new TreeSet<>(comparator);
        treesets.addAll(arr);
        elements = new ArrayList<>(treesets);
        this.comparator = comparator;
    }

    @Override
    public Comparator<T> comparator() { // O(1)
        if (comparator == Comparator.naturalOrder()) return null;
        return comparator;
    }

    private SortedSet<T> subSet(T fromElement, T toElement, boolean toInclusive) { // O(log n)
        int start = safeBinarySearch(fromElement);
        int end = safeBinarySearch(toElement);
        if (toInclusive) end++;
        if (start > end) {
            return new ArraySet<>(new ArrayList<>(), comparator);
        }
        return new ArraySet<>(elements.subList(start, end), comparator);
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) { // O(log n)
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        return subSet(fromElement, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) { // O(log n)
        if (isEmpty()) return new ArraySet<>(new ArrayList<>(), comparator);
        return subSet(first(), toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) { // O(log n)
        if (isEmpty()) return new ArraySet<>(new ArrayList<>(), comparator);
        return subSet(fromElement, last(), true);
    }

    @Override
    public T first() { // O(1)
        if (isEmpty()) throw new NoSuchElementException();
        return elements.get(0);
    }

    @Override
    public T last() { // O(1)
        if (isEmpty()) throw new NoSuchElementException();
        return elements.get(size() - 1);
    }

    @Override
    public int size() { // O(1)
        return elements.size();
    }

    private int safeBinarySearch(T o) { // O(log n)
        int res = Collections.binarySearch(elements, o, comparator);
        if (res < 0) res = -res - 1;
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) { // O(log n)
        int l = safeBinarySearch((T) o);
        return l < elements.size() && l >= 0 && comparator.compare(elements.get(l), (T) o) == 0;
    }

    @Override
    public Iterator<T> iterator() { // O(n)
        return elements.iterator();
    }
}
