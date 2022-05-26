import java.util.*;

public class SkipList<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private final int MAX_LEVEL = 914748999;

    private Node<T> head;
    // tail всегда null;

    private int heightOfSkipList; // чтобы не было быстрого роста
    private int size;

    private SortedMap<T, Integer> mapOfNodes = new TreeMap<>();

    public Random random = new Random();

    public SkipList() {
        this.head = new Node<>(null, MAX_LEVEL);
        this.heightOfSkipList = 1;
        this.size = 0;
    }

    public TreeSet<T> getKeys() {
        TreeSet<T> set = new TreeSet<>();
        for (Map.Entry<T, Integer> node : mapOfNodes.entrySet()) {
            set.add(node.getKey());
        }
        return set;

    }

    private boolean flipCoin() {
        return random.nextBoolean();
    }


    private int flipAndIncrementLevel() {
        int level = 0;
        for (int i = 0; i < head.getValue(); i++) {
            if (flipCoin()) {
                level++;
                if (level == this.heightOfSkipList) {
                    heightOfSkipList++;
                }
            } else
                break;
        }
        return level;
    }


    public void printL() {
        for (Map.Entry<T, Integer> node : mapOfNodes.entrySet()) {
            System.out.println(node.getKey() + " " + node.getValue());
        }
    }


    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    public void skipInsert(T key) {

        if (search(key))
            return;

        int level = flipAndIncrementLevel();

        Node<T> newNode = new Node<>(key,level);

        Node searcher = head;

        for (int i = heightOfSkipList - 1; i >= 0; i--) {
            while (null != searcher.next[i]) {
                if (greaterThan((T) searcher.next[i].getKey(), key)) {
                    break;
                }
                searcher = searcher.next[i];
            }

            if (i <= level && !newNode.equals(searcher.next[i])) {
                newNode.next[i] = searcher.next[i];
                searcher.next[i] = newNode;
            }
        }

        mapOfNodes.put(newNode.getKey(), newNode.getValue());

        size++;
    }

    public boolean delete(T key) {
        Node searcher = head;
        boolean result = false;
        for (int i = heightOfSkipList - 1; i >= 0; i--) {
            while (null != searcher.next[i]) {
                if (greaterThan((T) searcher.next[i].getKey(), key))
                    break;

                if (equalTo((T) searcher.next[i].getKey(), key)) {
                    searcher.next[i] = searcher.next[i].next[i];
                    mapOfNodes.remove(key);
                    result = true;
                    size--;
                    break;
                }
                searcher = searcher.next[i];
            }
        }


        return result;
    }


    public void levelPrint() {

        Node cursor = head;
        int start = head.getValue() - 1 ;
        while (null == cursor.next[start]) {
            start--;
        }

        cursor = head;
        List<Node> ref = new ArrayList<>();

        while (null != cursor) {
            ref.add(cursor);
            cursor = cursor.next[0];
        }

        for (int i = 0; i <= start; i++) {

            cursor = head;
            cursor = cursor.next[i];
            System.out.print( "Layer "+ i + " | head |");

            int levelIndex = 1;
            while (null != cursor) {

                if (i > 0) {
                    while (ref.get(levelIndex).getKey() != cursor.getKey()) {
                        levelIndex++;
                        System.out.print( "-------------------------");
                    }
                    levelIndex++;
                }

                System.out.print( "----> " + cursor);
                cursor = cursor.next[i];
            }

            System.out.println();
        }
    }

    public boolean search(T key) {
        TreeSet<T> set = getKeys();
        for (T x : set) {
            if (x.compareTo(key) == 0)
                return true;
        }
        return false;

    }


    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        TreeSet<T> set = getKeys();
        TreeSet<T> subSet = new TreeSet<>();

        if (greaterThan(fromElement, toElement) || toElement.compareTo(set.last()) > 0 ||
                fromElement.compareTo(set.last()) > 0 || fromElement.compareTo(set.first()) < 0 ||
                toElement.compareTo(set.first()) < 0) {
            throw new IllegalArgumentException();
        }

        for (T key : set) {
            if (key.compareTo(fromElement) >= 0 && key.compareTo(toElement) < 0) {
                subSet.add(key);
            }
        }

        return subSet;
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        TreeSet<T> set = getKeys();
        TreeSet<T> headSet = new TreeSet<>();

        if (toElement.compareTo(set.last()) > 0 || toElement.compareTo(set.first()) < 0) {
            throw new IllegalArgumentException();
        }

        for (T key : set) {
            if (key.compareTo(toElement) < 0) {
                headSet.add(key);
            }
        }

        return headSet;
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        TreeSet<T> set = getKeys();
        TreeSet<T> tailSet = new TreeSet<>();
        for (T key : set) {
            if (key.compareTo(fromElement) >= 0) {
                tailSet.add(key);
            }
        }

        return tailSet;
    }

    @Override
    public T first() {
        return mapOfNodes.firstKey();
    }

    @Override
    public T last() {
        return mapOfNodes.lastKey();
    }


    private boolean equalTo(T x, T y) {
        return x.compareTo(y) == 0;
    }

    private boolean greaterThan(T x, T y) {
        return x.compareTo(y) > 0;
    }


}