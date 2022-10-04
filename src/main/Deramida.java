package src.main;
import java.util.*;

public class Deramida<T extends Comparable<T>> implements SortedSet {

    private Node<T> root = null;
    private int size = 0;
    private static final Random random = new Random();

    private class Node<T> {
        T key;
        int priority;

        Node<T> leftChild;
        Node<T> rightChild;
        Node<T> parent = null;

        Node(T key) {
            this.key = key;
            priority = random.nextInt(30);
        }
    }

    private class NodePair<K, V> {
        private final K treeA;
        private final V treeB;

        NodePair(K treeA, V treeB) {
            this.treeA = treeA;
            this.treeB = treeB;
        }

        public K getTreeA() {
            return treeA;
        }

        public V getTreeB() {
            return treeB;
        }
    }

    @Override
    public Comparator comparator() {
        return (Comparator<T>) Comparable::compareTo;
    }

    private Node<T> merge(Node<T> treeA, Node<T> treeB) {
        if (treeA == null)
            return treeB;
        if (treeB == null)
            return treeA;

        if (treeA.priority > treeB.priority) {
            treeA.rightChild = merge(treeA.rightChild, treeB);
            treeA.rightChild.parent = treeA;
            return treeA;
        } else {
            treeB.leftChild = merge(treeA, treeB.leftChild);
            treeB.leftChild.parent = treeB;
            return treeB;
        }
    }

    private NodePair<Node<T>, Node<T>> split(Node<T> start, T key) {
        if (start == null)
            return new NodePair<>(null, null);

        if (key.compareTo(start.key) > 0) {
            NodePair<Node<T>, Node<T>> result = split(start.rightChild, key);
            start.rightChild = result.getTreeA();

            if (start.rightChild != null) start.rightChild.parent = start;
            if (result.getTreeB() != null) result.getTreeB().parent = null;
            return new NodePair<>(start, result.getTreeB());
        } else {
            NodePair<Node<T>, Node<T>> result = split(start.leftChild, key);
            start.leftChild = result.getTreeB();

            if (start.leftChild != null) start.leftChild.parent = start;
            if (result.getTreeA() != null) result.getTreeA().parent = null;
            return new NodePair<>(result.getTreeA(), start);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node<T> find(T key) {
        if (root == null) return null;
        return find(root, key);
    }

    private Node<T> find(Node<T> start, T key) {
        if (key.compareTo(start.key) == 0) {
            return start;
        } else if (key.compareTo(start.key) < 0) {
            if (start.leftChild == null) return start;
            return find(start.leftChild, key);
        } else {
            if (start.rightChild == null) return start;
            return find(start.rightChild, key);
        }
    }

    @Override
    public boolean contains(Object o) {
        T t = (T) o;
        Node<T> node = find(t);
        return node != null && t.compareTo(node.key) == 0;
    }

    @Override
    public boolean add(Object key) {
        T addKey = (T) key;

        Node<T> node = find(addKey);

        if (node != null && node.key.equals(addKey)) return false;

        NodePair<Node<T>, Node<T>> treaps = split(root, addKey);
        Node<T> temp = merge(treaps.getTreeA(), new Node<>(addKey));
        root = merge(temp, treaps.getTreeB());

        root.parent = null;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        T deleteKey = (T) o;
        Node<T> node = find(deleteKey);

        if (node != null && node.key.compareTo(deleteKey) == 0) return removeNode(node);

        return false;
    }

    public boolean removeNode(Node<T> node) {

        Node<T> parent = node.parent;

        if (parent != null) {
            if (parent.leftChild != null && parent.leftChild.key.equals(node.key)) {

                parent.leftChild = merge(node.leftChild, node.rightChild);
                if (parent.leftChild != null) parent.leftChild.parent = parent;

            } else {

                parent.rightChild = merge(node.leftChild, node.rightChild);
                if (parent.rightChild != null) parent.rightChild.parent = parent;
            }
        } else {
            root = merge(node.leftChild, node.rightChild);
            if (root != null)
                root.parent = null;
        }
        size--;
        return true;
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();

        Node<T> node = root;
        while (node.leftChild != null) node = node.leftChild;

        return node.key;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();

        Node<T> node = root;
        while (node.rightChild != null) node = node.rightChild;

        return node.key;
    }

    @Override
    public Iterator<T> iterator() {
        return new DeramidaIterator();
    }

    public class DeramidaIterator implements Iterator<T> {

        private Stack<Node<T>> stack = new Stack<>();
        Node<T> current = null;

        private DeramidaIterator() {
            pushIter(root);
        }

        private void pushIter(Node<T> node) {
            if (node != null) {
                stack.push(node);
                pushIter(node.leftChild);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();

            Node<T> node = stack.pop();
            current = node;
            pushIter(node.rightChild);

            return node.key;
        }

        @Override
        public void remove() {
            if (current == null) throw new IllegalStateException();
            removeNode(current);
            current = null;
        }
    }

    @Override
    public boolean addAll(Collection c) {
        for (Object o : c)
            if (!this.add(o)) return false;
        return true;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean removeAll(Collection c) {
        if (this.containsAll(c)) {
            for (Object o : c)
                remove(o);
            return true;
        } else return false;
    }

    @Override
    public boolean retainAll(Collection c) {
        if (this.containsAll(c)) {
            for (Object o : this)
                if (!c.contains(o)) remove(o);
            return true;
        } else return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        Iterator<T> iterator = this.iterator();
        for (int i = 0; i < this.size; i++)
            array[i] = iterator.next();
        return array;
    }

    @Override
    public Object[] toArray(Object[] a) {
        Object[] array = this.toArray();
        if (this.size >= 0) System.arraycopy(array, 0, a, 0, this.size);
        return a;
    }

     private class SubTree extends Deramida<T> {
        T bottom;
         T up;
        Deramida<T> deramida;

        SubTree(T bottom, T up, Deramida<T> deramida) {
            this.bottom = bottom;
            this.up = up;
            this.deramida = deramida;
        }

        private boolean isIncluded(T key) {

            return (bottom != null && up != null && key.compareTo(bottom) >= 0 && key.compareTo(up) < 0)
                    || (bottom == null && key.compareTo(up) < 0)
                    || (up == null && key.compareTo(bottom) >= 0 );
        }

        public int size() {
            if (deramida == null) return 0;
            int size = 0;
            for (Object key : deramida)
                if (isIncluded((T) key)) size++;
            return size;
        }

        public boolean contains(Object o) {
            return isIncluded((T) o) && deramida.contains(o);
        }

        public boolean add(Object key) {
            if (!isIncluded((T) key))
                throw new IllegalArgumentException();
            return deramida.add(key);
        }

        public boolean remove(Object o) {
            if (!isIncluded((T) o))
                throw new IllegalArgumentException();
            return deramida.remove(o);
        }

        @Override
        public Iterator<T> iterator() {
            return new SubTreeIterator();
        }

        public class SubTreeIterator implements Iterator<T> {

            private final Stack<Node<T>> stack = new Stack<>();
            Node<T> current = null;

            private SubTreeIterator() {
                if (root != null) {
                    pushIter(root);
                    current = stack.peek();
                }
            }

            private void pushIter(Node<T> node) {
                    if (node.leftChild != null) pushIter(node.leftChild);

                    if (isIncluded(node.key)) stack.push(node);

                    if (node.rightChild != null) pushIter(node.rightChild);
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();

                Node<T> node = stack.pop();
                current = node;

                return node.key;
            }

            @Override
            public void remove() {
                if (current == null) throw new IllegalStateException();
                deramida.remove(current.key);
            }
        }
    }

    @Override
    public SortedSet subSet(Object fromElement, Object toElement) {
        T from = (T) fromElement;
        T to = (T) toElement;

        if (to == null && from == null) throw new IllegalArgumentException();
        if (to != null && from.compareTo(to) >= 0) throw new IllegalArgumentException();

        return new SubTree(from, to, this);
    }

    @Override
    public SortedSet headSet(Object toElement) {
        T to = (T) toElement;

        if (to == null) throw new IllegalArgumentException();

        return new SubTree(null, to, this);
    }

    @Override
    public SortedSet tailSet(Object fromElement) {
        T from = (T) fromElement;

        if (from == null) throw new IllegalArgumentException();

        return new SubTree(from, null, this);
    }

    public void print() {
        print(root, 0);
    }

    public void print(Node node, int level){
        if (node == null ) {
            return;
        }

        print(node.rightChild, level + 1);
        if (level != 0) {
            for(int i = 0; i < level - 1; i++)
                System.out.print("|        \t");
            System.out.println("├-----------" + "[" + node.key + "; " + node.priority + "]");
        }
        else {
            System.out.println("[" + node.key + "; " + node.priority + "]");
        }
        print(node.leftChild, level + 1);
    }
}