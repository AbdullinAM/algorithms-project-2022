import java.util.*;

public class SplayTree<T extends Comparable<T>> extends AbstractSet<T> implements Set<T> {

    /**
     * SplayTree - это двоичное дерево поиска.
     * Оно позволяет находить быстрее те данные, которые использовались недавно.
     * Узлы, к которым обращались становятся корнем дерева.
     */

    public SplayTree() {
        root = null;
    }

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    public Node<T> root;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    /**
     * Получение значение корня.
     */
    public Object getRootValue() {
        return root.value;
    }

    /**
     * Проверка дерева на пустоту.
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Операция splay нужна для того чтобы доступ к недавно найденным данным был быстрее.
     * Требуется чтобы эти данные находились ближе к корню.
     * Данная функция совершает три вида поворотов,
     * благодаря чему достигается логарифмическая амортизированная оценка.
     *
     * Три вида операции splay: zig, zig-zig, zig-zag.
     *
     * x - вершина, которую хотим сделать корнем;
     * p - родитель x.
     *
     * Zig выполняется только один раз в конце, если глубина x - нечётна.
     *
     * Zig-zig: если x и p находятся оба либо в левом поддереве, либо в правом - либо оба левые дети, либо правые.
     * splay(g, p) -> splay(p, x)
     *
     * Zig-zag: если x - левый ребёнок, а p - правый (и наоборот).
     * splay(p, x) -> splay(g, x)
     */

    public Node<T> splay(Node<T> root, T value) {
        if (root == null || root.value.compareTo(value) == 0) {
            return root;
        }
        if (root.value.compareTo(value) > 0) {
            if (root.left == null) {
                return root;
            }
            if (root.left.value.compareTo(value) > 0) {
                root = zigZig(root, value);
            }
            else if (root.left.value.compareTo(value) < 0) {
                zigZag(root, value);
            }
            return root.left == null ? root : rotateToRight(root);
        } else {
            if (root.right == null) {
                return root;
            }
            if (root.right.value.compareTo(value) > 0) {
                zigZag(root, value);
            }
            else if (root.right.value.compareTo(value) < 0) {
                root = zigZig(root, value);
            }
            return root.right == null ? root : rotateToLeft(root);
        }
    }

    private Node<T> zigZig(Node<T> root, T value) {
        if (root.value.compareTo(value) > 0) {
            root.left.left = splay(root.left.left, value);
            root = rotateToRight(root);
        } else {
            root.right.right = splay(root.right.right, value);
            root = rotateToLeft(root);
        }
        return root;
    }

    private void zigZag(Node<T> root, T value) {
        if (root.value.compareTo(value) > 0) {
            root.left.right = splay(root.left.right, value);
            if (root.left.right != null) {
                root.left = rotateToLeft(root.left);
            }
        } else {
            root.right.left = splay(root.right.left, value);
            if (root.right.left != null) {
                root.right = rotateToRight(root.right);
            }
        }
    }

    private Node<T> rotateToRight(Node<T> root) {
        Node<T> temp = root.left;
        root.left = temp.right;
        temp.right = root;
        return temp;
    }
    private Node<T> rotateToLeft(Node<T> root) {
        Node<T> temp = root.right;
        root.right = temp.left;
        temp.left = root;
        return temp;
    }

    /**
     * Поиск элемента в дереве
     */
    private Node<T> find(T value) {
        if (root == null) {
            return null;
        }
        return root = splay(root, find(root, value).value);
    }

    private Node<T> find(Node<T> root, T value) {
        int comparison = value.compareTo(root.value);
        if (comparison == 0) {
            return root;
        }
        else if (comparison > 0) {
            if (root.right == null) {
                return root;
            }
            return find(root.right, value);
        }
        else { // if (comparison < 0)
            if (root.left == null) {
                return root;
            }
            return find(root.left, value);
        }
    }

    /**
     * Поиск минимального элемента
     */
    private Node<T> minimum(Node<T> root) {
        return root.left == null ? new Node<>(root.value) : minimum(root.left);
    }

    /**
     * Поиск максимального элемента
     */
    private Node<T> maximum(Node<T> root) {
        return root.right == null ? new Node<>(root.value) : maximum(root.right);
    }

    /**
     * Возвращает true, если дерево содержит данный элемент
     */
    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Возвращает true, если дерево содержит все элементы коллекции
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : this) {
            if (!c.contains(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Объединение двух деревьев
     */
    private Node<T> merge(Node<T> leftRoot, Node<T> rightRoot) {
        if (leftRoot == null) return rightRoot;
        if (rightRoot == null) return leftRoot;
        Node<T> maximumElement = maximum(leftRoot);
        leftRoot = splay(leftRoot, maximumElement.value);
        leftRoot.right = rightRoot;
        root = leftRoot;
        return root;
    }

    /**
     * Разделение дерева на две части
     */
    private Object[] split(T value) {
        Object[] pair = new Object[2];
        root = splay(root, value);
        if (root.value.compareTo(value) > 0) {
            pair[0] = root.left == null ? null : root.left;
            root.left = null;
            pair[1] = root;
        } else {
            pair[1] = root.right == null ? null : root.right;
            root.right = null;
            pair[0] = root;
        }
        return pair;
    }

    /**
     * Добавление элемента в дерево.
     * После добавления элемент становится корнем дерева.
     * Использует split для реализации этого.
     */
    @Override
    public boolean add(T value) {
        if (this.contains(value)) {
            return false;
        }
        if (root == null) { // Если дерево пустое, то создаём корень
            root = new Node<>(value);
            size++;
            return true;
        }
        Object[] pair = split(value);
        root = new Node<>(value);
        @SuppressWarnings("unchecked")
        Node<T> rootLeft = (Node<T>) pair[0];
        @SuppressWarnings("unchecked")
        Node<T> rootRight = (Node<T>) pair[1];
        root.left = pair[0] == null ? null : rootLeft;
        root.right = pair[1] == null ? null : rootRight;
        size++;
        return true;
    }

    /**
     * Добавление всех элементов коллекции в дерево
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            this.add(t);
        }
        return true;
    }

    /**
     * Удаление элемента из дерева.
     * Использует merge для объединения двух деревьев после удаления элемента.
     */
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T element = (T) o;
        Node<T> temp = splay(root, element);
        root = merge(temp.left, temp.right);
        size--;
        return true;
    }

    /**
     * Удаление всех элементов коллекции из дерева
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            this.remove(o);
        }
        return true;
    }


    /**
     * Итератор для обхода дерева.
     */
    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator(root);
    }

    public class SplayTreeIterator implements  Iterator<T> {
        Stack<Node<T>> stack = new Stack<>();
        T currentRootValue;

        private SplayTreeIterator(Node<T> root) {
            pushToStack(root);
        }

        /**
         * Функция ищет наименьший узел в поддереве, который больше узла,
         * рассматриваемого в методе next() и ставит его на вершину стека.
         * Т.е. выдаёт по окончании работы следующий узел.
         */
        private void pushToStack(Node<T> root) {
            if (root != null) {
                stack.push(root);
                pushToStack(root.left);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<T> node = stack.pop();
            currentRootValue = node.value;
            pushToStack(node.right);
            return node.value;
        }

        @Override
        public void remove() {
            if (currentRootValue == null) {
                throw new IllegalStateException();
            }
            SplayTree.this.remove(currentRootValue);
            currentRootValue = null;
        }
    }

    /**
     * Удаление всех элементов из дерева, которых нет в коллекции
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        for (Object o : this) {
            if (!c.contains(o)) {
                this.remove(o);
            }
        }
        return true;
    }

    /**
     * Преобразование дерева в массив
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        int i = 0;
        for (Object o : this) {
            array[i] = o;
            i++;
        }
        return array;
    }

    /**
     * Преобразование дерева в определённый массив
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray(Object[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (a.length < this.size) {
            return this.toArray();
        }
        int i = 0;
        for (Object o : this) {
            a[i] = o;
            i++;
        }
        return a;
    }

    /**
     * Полная очистка дерева
     */
    @Override
    public void clear() {
        for (Object o : this) {
            this.remove(o);
        }
    }
}
