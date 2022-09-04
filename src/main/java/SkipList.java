import com.google.common.collect.Iterators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

public class SkipList<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private static Random entropy = new Random();

    private static boolean heads() { return entropy.nextBoolean(); }

    private int size;

    // tail всегда null;
    private @Nullable Node<T> head;

    SkipList() { }

    SkipList(T firstKey) {
        this.head = new Node<>(firstKey);
    }

    void insert(T key)
    {
        final Node<T> born = new Node<>(key);
        if (head == null) // so, no head?
            head = born;
        else
        {
            final @NotNull Stack<Node<T>> trace = new Stack<>();
            final @Nullable Node<T> leftmost = leftmost(head, key, nOfLevels() - 1, trace);
            if (leftmost == null) // new head
            {
                for (int level = 0; level < nOfLevels(); level++)
                    born.connect(head, level);
                this.head = born;
            }
            else
            {
                trace.push(leftmost);
                bubbleUp(born, trace);
            }
        }
        size++;
    }

    private void bubbleUp(final @NotNull Node<T> node, final @NotNull Stack<Node<T>> trace)
    {
        int atLevel = 0;
        do {
            final Node<T> parent = (trace.empty()) ? head : trace.pop();
            parent.connect(node, atLevel++);
        } while (heads());
    }

    boolean delete(T key)
    {
        if (head == null)
            return false;
        if (equalTo(head.key(), key))
        {
            head = (head.hasNext(0)) ? head.child(0) : null;
            size--;
            return true;
        }
        final @Nullable Node<T> leftmost = leftmost(key);
        if (leftmost == null || !equalTo(leftmost.key(), key))
            return false;
        for (int level = 0; level < leftmost.parentLevelSize(); level++)
            Node.wire(leftmost.parent(level), leftmost.child(level), level);
        size--;
        return true;
    }

    List<T> keys()
    {
        final List<T> keys = new LinkedList<>();
        Iterators.addAll(keys, iterator());
        return keys;
    }

    boolean contains(T key)
    {
        Node<T> leftmost = leftmost(key);
        return (leftmost != null) && (equalTo(leftmost.key(), key));
    }

    private @Nullable Node<T> leftmost(T key) {
        return (head != null) ? leftmost(head, key, nOfLevels() - 1, null) : null;
    }

    // find closest to "key" element from the left. Null if key should become a new head.
    private @Nullable Node<T> leftmost(@NotNull Node<T> current, T key, int atLevel,
                                       @Nullable Stack<Node<T>> trace)
    {
        if (equalTo(current.key(), key))
            return current;
        else if (lessThan(current.key(), key))
        {
            // find first node >= key or reach the end of level
            @NotNull Node<T> leftmostOnCurrentLevel = current;
            while (leftmostOnCurrentLevel.hasNext(atLevel) && lessOrEqual(leftmostOnCurrentLevel.next(atLevel).key(), key))
                leftmostOnCurrentLevel = leftmostOnCurrentLevel.next(atLevel);
            return (atLevel > 0) ?
                    leftmost(leftmostOnCurrentLevel, key, atLevel - 1, push(leftmostOnCurrentLevel, trace)) :
                    leftmostOnCurrentLevel;
        }
        else // current > key
            return null;
    }

    private Stack<Node<T>> push(@NotNull Node<T> node, @Nullable Stack<Node<T>> stack)
    {
        if (stack == null)
            return null;
        stack.push(node);
        return stack;
    }

    private int nOfLevels() {
        return (head == null) ? 0 :
                (head.childLevelsSize() == 0) ? 1 :
                        head.childLevelsSize();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SkipIterator(0);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Comparator<? super T> comparator() {
        return Comparator.naturalOrder();
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        final TreeSet<T> result = new TreeSet<>();
        iterator().forEachRemaining(result::add);
        return result.subSet(fromElement, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        final TreeSet<T> result = new TreeSet<>();
        iterator().forEachRemaining(result::add);
        return result.headSet(toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        final TreeSet<T> result = new TreeSet<>();
        iterator().forEachRemaining(result::add);
        return result.tailSet(fromElement);
    }

    @Override
    public T first() {
        if (head == null)
            throw new NoSuchElementException();
        return head.key();
    }

    @Override
    public T last() {
        return Iterators.getLast(iterator());
    }

    private class SkipIterator implements Iterator<T> {

        private Node<T> current;
        private int level;
        private boolean firstTime = true;

        SkipIterator(int level) {
            this.current = head;
            this.level = level;
        }

        @Override
        public boolean hasNext()
        {
            return current.hasNext(level);
        }

        @Override
        public T next()
        {
            if (firstTime)
            {
                firstTime = false;
                return current.key();
            }
            current = current.next(level);
            return current.key();
        }
    }

    private boolean equalTo(T x, T y) {
        return x.compareTo(y) == 0;
    }

    private boolean lessThan(T x, T y) {
        return x.compareTo(y) < 0;
    }

    private boolean lessOrEqual(T x, T y)
    {
        return (lessThan(x, y) || equalTo(x, y));
    }

    @Override
    public String toString()
    {
        final StringBuilder result = new StringBuilder();
        for (int level = 0; level < nOfLevels(); level++)
        {
            final List<String> elementsAtLevel = new LinkedList<>();
            new SkipIterator(level)
                    .forEachRemaining(el -> elementsAtLevel.add(el.toString()));
            result.append(String.join(" <-> ", elementsAtLevel));
            result.append("\n");
        }
        return result.toString();
    }
}