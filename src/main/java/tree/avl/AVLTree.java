package tree.avl;

import java.util.*;

public class AVLTree<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    static class Node<T extends Comparable<T>> {
        T element;
        int h = 0;
        Node<T> left = null;
        Node<T> right = null;

        public Node(T element) {
            this.element = element;
        }
    }

    private Node<T> root;
    private int size;

    //Размер коллекции
    @Override
    public int size() {
        return size;
    }

    //Проверка коллекция пустая или нет
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    //Функция обновления высоты
    //получаем высоту у левого и правого ребенка переданного узла
    //находим из них максимальную прибавляем 1 и присваиваем эту высоту узлу
    private void updateHeight(Node<T> node) {
        node.h = Math.max(height(node.left), height(node.right)) + 1;
    }

    //Функция получения высоты узла
    private int height(Node<T> node) {
        return node == null ? -1 : node.h;
    }   //node = null -> -1 иначе высоту

    //Получение параметра сбалансированности у узла
    private int getBalance(Node<T> node) {
        return (node == null) ? 0 : height(node.right) - height(node.left);
    }

    //Проверка нахождения элемента в дереве
    //идём от корня, на каждой итерации сравниваем узел
    //с переданным нам объектом, если объект не равен
    //то идем в левое(если элемент меньше текущего узла)
    //или правое(если элемент больше текущего узла) поддерево
    @Override
    public boolean contains(Object o) {
        Node<T> current = root;
        while (current != null) {
            if (current.element.compareTo((T) o) == 0) {
                return true;
            }
            current = current.element.compareTo((T) o) < 0 ? current.right : current.left;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new AVLTreeIterator();
    }

    public class AVLTreeIterator implements Iterator<T> {
        Deque<Node<T>> stack = new LinkedList<>();
        T lastReturned;

        private AVLTreeIterator() {
            fillStacks(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }   //Проверка на наличие след. элемента

        @Override
        public T next() {   //Возвращает след. элемент
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<T> result = stack.pollLast();
            fillStacks(result.right);
            lastReturned = result.element;
            return result.element;
        }

        private void fillStacks(Node<T> node) { //Добавление в стек узла и его левых детей
            if (node != null) {
                stack.add(node);
                fillStacks(node.left);
            }
        }

        private void fillStackAfterRemove(Node<T> node, T stop) {   //Добавление в стек с проверкой прошли ли мы элемент, тк после remove() дерево могли измениться
            if (node != null) {
                if (node.element.compareTo(stop) < 0) {
                    fillStackAfterRemove(node.right, stop);
                } else {
                    stack.add(node);
                    fillStackAfterRemove(node.left, stop);
                }
            }
        }

        @Override
        public void remove() {  //Удаление последнего элемента в текущем стеке(из последнего вызова next())
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            if (!stack.isEmpty()) {
                AVLTree.this.remove(lastReturned);
                stack.clear();
                T stop = lastReturned;
                lastReturned = null;
                fillStackAfterRemove(root, stop);
            } else {
                AVLTree.this.remove(lastReturned);
                lastReturned = null;
            }
        }
    }

    //Создаем массив объектов размером нашей коллекции
    //с помощью итератора перебираем нашу коллекции и добавляем в массив по индексу
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<T> iterator = this.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            array[i] = iterator.next();
        }
        return array;
    }

    //Если размер переданной коллекции меньше нашей
    //просто генерируем массив по нашей коллекции
    //если больше, то добавляем в этот массив и все остальное заполняем null
    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
            return (T1[]) this.toArray();
        }
        Iterator<T> iterator = this.iterator();
        for (int i = 0; i < a.length; i++) {
            if (!iterator.hasNext()) {
                a[i] = null;
            } else {
                a[i] = (T1) iterator.next();
            }
        }
        return a;
    }

    private Node<T> rotateRight(Node<T> node) { //Прокрутка вправо (для перебалансировки)
        Node<T> left = node.left;
        Node<T> leftRight = left.right;
        left.right = node;
        node.left = leftRight;
        updateHeight(node);
        updateHeight(left);
        return left;
    }

    private Node<T> rotateLeft(Node<T> node) {  //Прокрутка влево (для перебалансировки)
        Node<T> right = node.right;
        Node<T> rightLeft = right.left;
        right.left = node;
        node.right = rightLeft;
        updateHeight(node);
        updateHeight(right);
        return right;
    }

    private Node<T> rebalanced(Node<T> node) {  //Перебалансировка дерева после добавления или удаления
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (height(node.right.right) <= height(node.right.left)) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        } else if (balance < -1) {
            if (height(node.left.left) <= height(node.left.right)) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }

    @Override
    public boolean add(T t) {   //Добавляет элемент в дерево и -> true или если такой уже есть -> false
        if (t == null) {
            return false;
        }
        try {
            root = add(root, t);
            size++;
        } catch (AVLTreeException e) {
            return false;
        }
        return true;
    }

    private Node<T> add(Node<T> node, T element) throws AVLTreeException {  //Добавляет элемент в дерево и перебалансирует
        if (node == null) {
            return new Node<>(element);
        } else if (node.element.compareTo(element) > 0) {
            node.left = add(node.left, element);
        } else if (node.element.compareTo(element) < 0) {
            node.right = add(node.right, element);
        } else {
            throw new AVLTreeException("Это значение уже есть в коллекции!");
        }
        return rebalanced(node);
    }

    @Override
    public boolean remove(Object o) {   //Удаляет элемент из дерева и -> true или если такого эл-та нет -> false
        if (!contains(o) || o == null) {
            return false;
        }
        root = remove(root, (T) o);
        size--;
        return true;
    }

    private Node<T> remove(Node<T> node, T element) {   //Удаляет элемент из дерева и перебалансирует
        if (node == null) {
            return null;
        } else if (node.element.compareTo(element) > 0) {
            node.left = remove(node.left, element);
        } else if (node.element.compareTo(element) < 0) {
            node.right = remove(node.right, element);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node<T> mostLeftChild = mostLeftChild(node.right);
                node.element = mostLeftChild.element;
                node.right = remove(node.right, node.element);
            }
        }
        if (node != null) {
            node = rebalanced(node);
        }
        return node;
    }

    private Node<T> mostLeftChild(Node<T> node) {   //Возвращает самый левый(наименьший) элемент
        if (node.left == null) {
            return node;
        }
        return mostLeftChild(node.left);
    }

    @Override
    public boolean containsAll(Collection<?> c) {   //True если все переданные элементы есть в дереве
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {  //Добавляет все переданные элементы
        for (Object o : c) {
            this.add((T) o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {     //Оставляет в дереве только переданные элементы
        return removeIf(a -> !c.contains(a));
    }

    @Override
    public boolean removeAll(Collection<?> c) {     //Удаляет все переданные элементы
        for (Object o : c) {
            this.remove(o);
        }
        return true;
    }

    @Override
    public void clear() {   //Очищает дерево
        root = null;
        size = 0;
    }

    @Override
    public Comparator<? super T> comparator() { //Возвращает компаратор для упорядочивания эл-тов
        return Comparable::compareTo;
    }


    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {    //Возвращает дерево из элементов от fromElement до toElement не включая
        if (!contains(fromElement) || !contains(toElement)) {
            throw new NoSuchElementException();
        }
        if (fromElement.compareTo(toElement) > 0) throw new IllegalStateException();
        AVLTree<T> subTree = new AVLTree<>();
        AVLTreeIterator iterator = new AVLTreeIterator();
        while (iterator.hasNext()) {
            T value = iterator.next();
            if (value.compareTo(fromElement) >= 0 && value.compareTo(toElement) < 0) {
                subTree.add(value);
            }
        }
        return subTree;
    }
    //Возвращает дерево из элементов от первого до toElement не включая
    @Override
    public SortedSet<T> headSet(T toElement) {
        return subSet(first(), toElement);
    }

    //Возвращает дерево из элементов от fromElement до последнего не включая
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return subSet(fromElement, last());
    }

    @Override
    public T first() {// возвращает самый первый элемент в дереве (первый в порядке увеличения value)(самый маленький)
        if (root == null) throw new NoSuchElementException();
        return mostLeftChild(root).element;
    }

    @Override
    public T last() { // возвращает самый последний элемент в дереве (последний в порядке увеличения value)(самый большой)
        if (root == null) throw new NoSuchElementException();
        Node<T> currentNode = root;
        while (currentNode.right != null) {
            currentNode = currentNode.right;
        }
        return currentNode.element;
    }
}
