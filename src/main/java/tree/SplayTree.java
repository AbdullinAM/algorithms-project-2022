package tree;

import java.util.*;

public class SplayTree<T extends Comparable<T>> implements Set<T> {

    private Node<T> root;//корень дерева
    private int size;//размер дерева

    @Override
    public int size() {
        return size;//возвращаем размер дерева
    }

    @Override
    public boolean isEmpty() {//+
        return root == null;//если корень null, то дерево пустое
    }

    @Override
    public boolean contains(Object o) {
        Node<T> node = root;//создали переменную для корня
        T object = (T) o;//скастовали объект к Т

        while (node != null) {//пока наш node не null заходим
            if (node.getValue().compareTo(object) == 0) { //делаем сравнение, что бы понять это тот элемент который мы ищем или нет
                splay(node);//делаем splay для элемента, который мы нашли
                return true;//возвращаем true потому что мы нашли нужный для нас элемент
            }

            node = object.compareTo(node.getValue()) < 0 ? node.getLeftChild() : node.getRightChild();
        }

        return false;//возвращаем false потому что не нашли элемента
    }

    @Override
    public boolean add(T t) {
        if (this.contains(t) || t == null) {//проверяем на null и что элемент уже такой не содержится в коллекции
            return false;
        }

        root = add(root, new Node<>(t));//просто добавляем новый узел
        size++;//увеличиваем ее размер
        return true;
    }


    private Node<T> add(Node<T> node, Node<T> nodeToInsert) {
        if (node == null) {//если текущий node == null
            return nodeToInsert; // то возвращаем его
        }

        //ниже идет проверка в какую из частей дерева отправить элемент, если оно больше или меньше
        if (nodeToInsert.getValue().compareTo(node.getValue()) < 0) {
            node.setLeftChild(add(node.getLeftChild(), nodeToInsert));//рекурсивно доходим, а потом добавляем в дерево на нужное место
            node.getLeftChild().setParent(node);//меняем у правого родителя на node
        } else if (nodeToInsert.getValue().compareTo(node.getValue()) > 0) {
            node.setRightChild(add(node.getRightChild(), nodeToInsert));//рекурсивно доходим, а потом добавляем в дерево на нужное место
            node.getRightChild().setParent(node);//меняем у правого родителя на node
        }

        return node;//возвращаем node
    }

    @Override
    public boolean remove(Object o) {
        if (!this.contains(o) || o == null) {//проверяем на null и что элемент уже такой не содержится в коллекции
            return false;
        }

        root = remove((T) o, root);//удаляем элемент из коллекции
        size--;//уменьшаем ее размер
        return true;
    }

    private Node<T> remove(T value, Node<T> node) {
        if (node == null) {//если текущий node == null
            return null;//возвращаем null
        }

        if (value.compareTo(node.getValue()) < 0) {//сравнение значений переданного и найденного
            node.setLeftChild(remove(value, node.getLeftChild()));//меняем левого ребенка, которого верули из рекурсии
            if (node.getLeftChild() != null) {//если не null
                node.getLeftChild().setParent(node);//меняем у node левого ребенка на node
            }
        } else if (value.compareTo(node.getValue()) > 0) {
            node.setRightChild(remove(value, node.getRightChild()));//меняем правого ребенка, которого верули из рекурсии
            if (node.getRightChild() != null) {//если не null
                node.getRightChild().setParent(node);//меняем у node правого ребенка на node
            }
        } else {
            //Ситуация когда 1 ребенок или левый node (без детей)
            if (node.getLeftChild() == null) {//если левый ребенок равен null
                return node.getRightChild();//возвращаем правого
            } else if (node.getRightChild() == null) {//если правый ребенок равен null
                return node.getLeftChild();//возвращаем левого
            }
            //Ситуация когда два ребенка
            node.setValue((isEmpty()) ? null : getMax(node.getLeftChild()));//если дерево пустое, то меняем значене на null иначе на максимального в левом ребенке
            node.setLeftChild(remove(node.getValue(), node.getLeftChild()));//меняет левого ребенка на node который мы получили в рекурсии
            if (node.getLeftChild() != null) {//если левый ребенок не null
                node.getLeftChild().setParent(node);//то меняем родителя у левого ребенка на node
            }
        }

        return node;//возвращаем node
    }

    private T getMax(Node<T> node) {
        if (node.getRightChild() != null) {//если правый ребенок не равен null
            return getMax(node.getRightChild());//заходим сюда и рекурсивно доходим до самого большого
        }

        return node.getValue();// возвращаем значение найденного node
    }

    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

    public class SplayTreeIterator implements Iterator<T> {
        private final Stack<Node<T>> stack = new Stack<>();//стек для элементов
        private Node<T> node = root;//буфер для node
        private Node<T> lastReturn;//последний возвращенный в next node

        @Override
        public boolean hasNext() {
            return (node != null || !stack.empty());//проверка есть ли следующий элемент в коллекции
        }

        @Override
        public T next() {
            while (node != null) {//смотрим не равен ли наш node null
                stack.push(node);//запихиваем в стек
                node = node.getLeftChild();//присваиваем node его левого ребенка
            }

            if (stack.empty()) {//если стек пуст, следовательно, мы не можем получить новый элемент, кидаем исключение
                throw new NoSuchElementException();
            }

            lastReturn = stack.pop();//берем из стека
            node = lastReturn.getRightChild();//берем правого ребенка из lastReturn и присваиваем node

            return lastReturn.getValue();//возвращаем значение lastReturn
        }

        @Override
        public void remove() {
            if (lastReturn == null) {//если перед этим мы не вызывали next, он равен null, следовательно, кидаем исключение
                throw new IllegalStateException();
            }

            Node<T> parentNext = lastReturn.getParent();//буфим родителя у левого
            //проверим что у lastReturn нет детей не с лева не с права
            if (parentNext != null && lastReturn.getRightChild() == null && lastReturn.getLeftChild() == null) {
                //проверка lastReturn если это левый ребенок то его можно обнулить
                if (parentNext.getLeftChild() == lastReturn) {
                    parentNext.setLeftChild(null);
                }
                //проверка lastReturn если это правый ребенок то его можно обнулить
                if (parentNext.getRightChild() == lastReturn) {
                    parentNext.setRightChild(null);
                }
                //Обработка ситуации когда левый или правый ребенок у lastReturn null
            } else if (lastReturn.getLeftChild() == null || lastReturn.getRightChild() == null) {
                if (lastReturn.getLeftChild() == null) {//если левый ребенок null
                    //ситуация когда parentNext не null и его левый ребенок равен lastReturn
                    if (parentNext != null && parentNext.getLeftChild() == lastReturn) {
                        //меняем у parentNext его левого ребенка, на правого у lastReturn
                        parentNext.setLeftChild(lastReturn.getRightChild());
                    } else {
                        if (parentNext != null) {//если parentNext not null
                            parentNext.setRightChild(lastReturn.getRightChild());//меняем его правого ребека на правого у lastReturn
                        } else {
                            root = lastReturn.getRightChild();//присваиваем корню правого ребенка в lastReturn
                        }
                    }
                    if (lastReturn.getRightChild() != null) {//проверка, что у lastReturn правый ребенок не null
                        lastReturn.getRightChild().setParent(parentNext);//меняем у этого правого ребенка его родителя на parentNext
                    }
                } else {//если правый ребенок null
                    if (parentNext != null && parentNext.getLeftChild() == lastReturn) {//если parentNext и левый ребенок у parentNext равен lastReturn
                        parentNext.setLeftChild(lastReturn.getLeftChild());//то у parentNext меняем его левого ребека на левого ребенка у lastReturn
                    } else {
                        if (parentNext != null) {//если parentNext не null
                            parentNext.setRightChild(lastReturn.getLeftChild());//то меняем его правого ребенка на левого у lastReturn
                        } else {//иначе
                            root = lastReturn.getLeftChild();//присваиваем корню левого ребенка lastReturn
                        }
                    }
                    lastReturn.getLeftChild().setParent(parentNext);//меняем родителя левого ребенка у lastReturn на parentNext
                }
            } else {
                Node<T> minNode = minNode(lastReturn.getRightChild());//находим минмальный node
                if (minNode.getRightChild() != null) {//смотрим равен ли он нулю
                    minNode.getParent().setLeftChild(minNode.getRightChild());//меняем у его родителя левого ребенка на правого
                } else {
                    minNode.getParent().setLeftChild(null);//меняем у его родителя левого ребенка на null
                }
                minNode.setRightChild(lastReturn.getRightChild());//меняем правого ребенка на правого у lastReturnм
                minNode.setLeftChild(lastReturn.getLeftChild());//меняем левого ребенка на левого у lastReturn
                if (parentNext != null) {//если родитель не равен нулю
                    if (lastReturn.getParent().getLeftChild() == lastReturn) {// берем у родителя lastReturn левого ребенка и сравниваем с lastReturn
                        parentNext.setLeftChild(minNode);//если равне то у parentNext меняем его левого ребенка на minNode
                    } else {//иначе
                        parentNext.setRightChild(minNode);//у parentNext меняем правого ребеннка на minNode
                    }
                }
            }

            lastReturn = null;//обнуляем lastReturn, так как он удален, а следующий можно получить только при вызове next
            size--;//уменьшаем размер коллекции так как удалил элемент
        }

        private Node<T> minNode(Node<T> node) {
            if (node.getLeftChild() == null) {//смотрим равен ли левый ребенок null
                return node;//возвращаем наш node
            }

            return minNode(node.getLeftChild());//рекурсивно перебираем до минимального(берем левых так как с лева всегда минимальные)
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];//создаем массив размером как наше дерево

        Iterator<T> iterator = this.iterator();//вызываем итератор
        int index = 0;//это надо что бы добавлять по индексу в массив
        while (iterator.hasNext()) {//проверяем есть ли следующий элемент
            array[index] = iterator.next();//берем этот элемент и записываем по индексу в массив
            index++;//увеличиваем индекс, чтобы записать другой элемент в следующую ячейку
        }

        return array;//возвращаем наш массив
    }

    @Override
    public boolean addAll(Collection c) {
        if (c == null) {//обработка если передали null коллекцию
            return false;
        }

        for (Object temp : c) {// проходимся по коллекции с помощью for-each
            this.add((T) temp);//на каждом элементе вызываем add тем самым добавляем новый элемент
        }

        return true;
    }

    @Override
    public void clear() {
        root = null;//делаем корень null
        size = 0;//делаем размер равный 0
    }

    @Override
    public boolean removeAll(Collection c) {
        if (c == null) {//обработка если передали null коллекцию
            return false;
        }

        for (Object temp : c) {// проходимся по коллекции с помощью for-each
            this.remove(temp);//на каждом элементе вызываем remove тем самым удаляя
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection c) {
        if (c == null) {//обработка если передали null коллекцию
            return false;
        }

        for (Object temp : this) {//проходимся по нашему дереву с помощью for-each
            if (!c.contains(temp)) {//на каждом элементе вызываем contains тем самым понимаем какой элемент содержится в дереве и в коллекции
                this.remove(temp);//удаляем элемент из дерева, если его нет в коллекции
            }
        }

        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        if (c == null || size < c.size()) {//обработка если передали null коллекцию или размер нашего дерева меньше колекции которую передали
            return false;
        }

        for (Object temp : c) {// проходимся по коллекции с помощью for-each
            if (!this.contains(temp)) {//на каждом элементе вызываем contains, если такого элемента не нашли
                return false;//просто возвращаем false
            }
        }

        return true;//если все элементы были найдены, то возвращаем true
    }

    @Override
    public Object[] toArray(Object[] a) {
        Object[] array = toArray();//получаем нашу коллекцию в массиве
        if (a.length < size) {// если наша коллекция больше чем данный массив, то возвращаем ее
            return array;
        }

        System.arraycopy(array, 0, a, 0, a.length - 1);//массив стартовый больше, то просто сливаем 2 массива
        return a;
    }

    private void splay(Node<T> node) {
        while (node != root) {//заходим пока наш node не равен root
            Node<T> parent = node.getParent();//получаем родителя node
            if (node.getGrandParent() == null) {//получаем родителя у родителя и сравниваем с null
                if (node.isLeftChild()) {//если node это левый ребенок
                    // zig ситуация
                    rotateRight(parent);//поворачиваем parent в право
                } else {//если node это правый ребенок
                    // zag ситуация
                    rotateLeft(parent);//поворачиваем parent в лево
                }
            } else if (node.isLeftChild() && parent.isLeftChild()) {//если node и parent оба левые дети
                // zig-zig ситуация
                rotateRight(node.getGrandParent());//получаем родителя родителя у node и поворачиваем его в право
                rotateRight(parent);//поворачиваем parent в право
            } else if (node.isRightChild() && parent.isRightChild()) {//если node и parent оба правые дети
                // zag-zag ситуация
                rotateLeft(node.getGrandParent());//получаем родителя родителя у node и поворачиваем его в лево
                rotateLeft(parent);//поворачиваем parent в лево
            } else if (node.isLeftChild() && parent.isRightChild()) {//если node левый, а parent правый ребенок
                // zag-zig ситуация
                rotateRight(parent);//поворачиваем parent в право
                rotateLeft(parent);//поворачиваем parent в лево
            } else {//в остальных случаях
                // zig-zag ситуация
                rotateLeft(parent);//поворачиваем parent в лево
                rotateRight(parent);//поворачиваем parent в право
            }
        }
    }

    private void rotateRight(Node<T> node) {
        Node<T> leftNode = node.getLeftChild();//получаем левого ребенка node
        node.setLeftChild(leftNode.getRightChild());//меняем у node левого ребенка на правого у левого
        if (node.getLeftChild() != null) {//если левый ребенок node не равен null
            node.getLeftChild().setParent(node);//то у левого ребенка node родителем становиться сам node
        }
        updateChildrenOfParentNode(node, leftNode);//обновляем детей у родительского node
        leftNode.setParent(node.getParent());//присваиваем родителя node родителю leftNode
        leftNode.setRightChild(node);//делаем node правым ребенком у leftNode
        node.setParent(leftNode);//меняем у node родителя на leftNode
    }

    private void rotateLeft(Node<T> node) {
        Node<T> rightNode = node.getRightChild();//получаем правого ребенка node
        node.setRightChild(rightNode.getLeftChild());//меняем у node правого ребенка на левого у правого
        if (node.getRightChild() != null) {//если правый ребенок node не равен null
            node.getRightChild().setParent(node);//то у правого ребенка node родителем становиться сам node
        }
        updateChildrenOfParentNode(node, rightNode);//обновляем детей у родительского node
        rightNode.setParent(node.getParent());//присваиваем родителя node родителю rightNode
        rightNode.setLeftChild(node);//делаем node левым ребенком у rightNode
        node.setParent(rightNode);//меняем у node родителя на rightNode
    }

    private void updateChildrenOfParentNode(Node<T> node, Node<T> tempNode) {
        if (node.getParent() == null) {//проверим что родитель null
            root = tempNode;//присваиваем корню tempNode
        } else if (node.isLeftChild()) {//проверим что node это левый ребенок
            node.getParent().setLeftChild(tempNode);//получаем у node его родителя и меняем у родителя левого ребенка на tempNode
        } else {//если сюда дошло, то node это правый ребенок
            node.getParent().setRightChild(tempNode);//получаем у node его родителя и меняем у родителя правого ребенка на tempNode
        }
    }
}
