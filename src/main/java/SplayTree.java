import java.util.*;

public class SplayTree<T extends Comparable<T>> extends AbstractSet<T> implements Set<T>{
    public static class Node<T> {
        T value;
        Node<T> left = null;
        Node<T> right = null;
        Node(T value) {
            this.value = value;
        }
    }

    public Node<T> root;

    public SplayTree() {
        root = null;
    }

    public Object getRootValue() {
        return root.value;
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    private int size(Node<T> node) {
        if (node == null) return 0;
        return size(node.left) + size(node.right) + 1;
    }

    // С помощью функции find находим элемент
    // Если он был в дереве, то, из-за функции splay,
    // он стал корнем. Поэтому, если find вернул что угодно
    // (null или другой элемент), кроме нашего элемента  false

    @Override
    public boolean contains(Object object) {
        @SuppressWarnings("unchecked")
        T el = (T) object;
        return find(el) != null && Objects.requireNonNull(find(el)).value == object;
    }

    public Node<T> find(T element) {
        if (root == null) return null;
        return root = splay(root, find(root, element).value);
    }

    private Node<T> find(Node<T> start, T element) {
        int comparison = element.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, element);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, element);
        }
    }

    public class Pair {
        public Node<T> left;
        public Node<T> right;
        Pair(Node<T> left, Node<T> right) {
            this.left = left;
            this.right = right;
        }
    }

    // Возвращаем два дерева, полученные отсечением правого или левого
    // поддерева от корня, в зависимости от того, содержит корень
    // элемент больше или не больше, чем element

    public Pair split(T element) {
        if (root == null) return new Pair(null, null);
        Node<T> left = null;
        Node<T> right = null;
        root = splay(root, element);
        int comparison = element.compareTo(root.value);
        if (comparison > 0) {
            if (root.right != null) right = root.right;
            root.right = null;
            left = root;
        }
        else {
            if (root.left != null) left = root.left;
            root.left = null;
            right = root;
        }
        return new Pair(left, right);
    }

    public Node<T> findMax(Node<T> start) {
        Node<T> max = start;
        if (start.right != null) {
            Node<T> mbMax = findMax(start.right);
            if (max.value.compareTo(mbMax.value) < 0) max = mbMax;
        }
        if (start.left != null) {
            Node<T> mbMax = findMax(start.left);
            if (max.value.compareTo(mbMax.value) < 0) max = mbMax;
        }
        return max;
    }

    public Node<T> findMin(Node<T> start) {
        Node<T> min = start;
        if (start.left != null) {
            Node<T> mbMin = findMin(start.left);
            if (min.value.compareTo(mbMin.value) > 0) min = mbMin;
        }
        if (start.right != null) {
            Node<T> mbMin = findMin(start.right);
            if (min.value.compareTo(mbMin.value) > 0) min = mbMin;
        }
        return min;
    }

    public Node<T> merge(Node<T> left, Node<T> right) {
        if (left == null && right == null) return null;
        else if (left == null) return right;
        else if (right == null) return left;
        left = splay(left, findMax(left).value);
        right = splay(right, findMin(right).value);
        left.right = right;
        root = left;
        return root;
    }

    @Override
    public boolean add(T element) {
        if (root == null) {
            root = new Node<>(element);
            return true;
        }
        root = splay(root, element);
        if (root.value == element) return false;
        int comparison = element.compareTo(root.value);
        Node<T> newNode = new Node<>(element);
        if (comparison < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        }
        else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
        return true;
    }

    // Запускаем split(element), который нам возвращает
    // деревья treeLeft и treeRight, их подвешиваем к element
    // как левое и правое поддеревья соответственно.

    public boolean addWithSplit(T element) {
        if (root == null) {
            root = new Node<>(element);
            return true;
        }
        if (contains(element)) return false;
        Pair splitRes = split(element);
        root = new Node<>(element);
        if (splitRes.left != null) root.left = splitRes.left;
        if (splitRes.right != null) root.right = splitRes.right;
        return true;
    }

    @Override
    public boolean remove(Object object) {
        @SuppressWarnings("unchecked")
        T element = (T) object;
        if (root == null) return false;
        root = splay(root, element);
        if (root.value != element) return false;
        root.left = splay(root.left, element);
        if (root.left == null) {
            root = root.right;
        }
        else {
            root.left.right = root.right;
            root = root.left;
        }
        return true;
    }

    public boolean removeWithSplit(T element) {
        if (root == null) return false;
        root = splay(root, element);
        if (root.value != element) return false;
        root = merge(root.left, root.right);
        return true;
    }

    // Функция splay помещает элемент в корень, если элемент
    // присутствует в дереве. Если элемент отсутствует,
    // он переносит последний доступный элемент в корневой каталог
    // Эта функция изменяет дерево и возвращает новый корень

    private Node<T> splay(Node<T> node, T element) {
        if (node == null || node.value == element) return node;
        int cmp1 = element.compareTo(node.value);

        //Если элемент находится в левом поддереве
        if (cmp1 < 0) {
            // Если элемент не в дереве -> конец
            if (node.left == null) return node;
            // Zig-Zig (Left Left)
            int cmp2 = element.compareTo(node.left.value);
            if (cmp2 < 0) {
                // Сначала рекурсивно приведем элемент как корень left-left
                node.left.left = splay(node.left.left, element);
                // Сделаем первое вращение для корня,
                // второе вращение выполняется после else
                node = rotateRight(node);
            }
            // Zig-Zag (Left Right)
            else if (cmp2 > 0) {
                // Сначала рекурсивно приведем элемент как корень left-right
                node.left.right = splay(node.left.right, element);
                // Сделаем первое вращение для root.left
                if (node.left.right != null)
                    node.left = rotateLeft(node.left);
            }
            // Сделаем второе вращение для корня
            return (node.left == null) ? node : rotateRight(node);
        }
        //Если элемент находится в правом поддереве
        else {
            // Если элемент не в дереве -> мы закончили
            if (node.right == null) return node;
            // Zag-Zig (Right Left)
            int cmp2 = element.compareTo(node.right.value);
            if (cmp2 < 0) {
                // Приведем элемент как корень right-left
                node.right.left = splay(node.right.left, element);
                // Сделаем первое вращение для root.right
                if (node.right.left != null)
                    node.right = rotateRight(node.right);
            }
            // Zag-Zag (Right Right)
            else if (cmp2 > 0) {
                // Приведем элемент как корень right-right
                // и сделаем первое вращение
                node.right.right = splay(node.right.right, element);
                node = rotateLeft(node);
            }
            // Сделаем второе вращение для корня
            return (node.right == null) ? node : rotateLeft(node);
        }
    }

    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private Node<T> rotateRight(Node<T> x) {
        Node<T> y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

    public class SplayTreeIterator implements Iterator<T> {
        private final Stack<Node<T>> stack = new Stack<>();
        private Node<T> currNode = new Node<>(null);

        private SplayTreeIterator() {
            if (root != null) root = splay(root, findMin(root).value);
            inOrderIterator(root);
        }

        private void inOrderIterator(Node<T> node) {
            Node<T> n = node;
            while (n != null) {
                stack.push(n);
                n = n.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<T> node = stack.pop();
            currNode = node;
            inOrderIterator(node.right);
            root = splay(root, node.value);
            return node.value;
        }

        @Override
        public void remove() {
            if (currNode == null) throw new IllegalStateException();
            SplayTree.this.remove(currNode.value);
            currNode = null;
        }
    }

    @Override
    public Object[] toArray() {
        Iterator iterator = this.iterator();
        Object[] arr = new Object[this.size()];
        for (int i = 0; i < this.size(); i++) {
            arr[i] = iterator.next();
        }
        return arr;
    }

    @Override
    public Object[] toArray(Object[] a) {
        if (a == null) throw new NullPointerException("The specified array is null");
        if (a.length < this.size()) return this.toArray();
        Iterator iterator = this.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            a[i] = iterator.next();
            i++;
        }
        while (i < a.length) {
            a[i] = null;
            i++;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (Object o : c) {
            this.add((T) o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            Object a = iterator.next();
            if (!c.contains(a)) {
                this.remove(a);
                iterator = this.iterator();
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            this.remove((T) o);
        }
        return true;
    }

    @Override
    public void clear() {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}