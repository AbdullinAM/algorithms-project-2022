package treap;

import jakarta.xml.bind.annotation.XmlType;
import javafx.util.Pair;

import jakarta.xml.bind.annotation.XmlRootElement;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@XmlRootElement(name = "Treap")
public class Treap<T extends Comparable<T>> implements Set {

    public static class Node<T> {
        public T x;
        public int y;
        public Node<T> left, right;

        public Node() {
        }

        Node(T x) {
            this.x = x;
            Random r = new Random();
            this.y = r.nextInt(1000);
        }
    }

    private int size;

    public Node<T> root;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        return find((T) o) != null;
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return new TreapIterator(root);
    }

    public class TreapIterator implements Iterator<T> {
        Stack<Node> leftstack = new Stack<Node>();

        public void TreeIterator(Node root) {
            while (root != null) {
                leftstack.push(root);
                root = root.left;
            }
        }

        private TreapIterator(Node node) {
            TreeIterator(node);
        }

        @Override
        public boolean hasNext() {
            return !leftstack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node node = leftstack.pop();
            T result = (T) node.x;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    leftstack.push(node);
                    node = node.left;
                }
            }
            return result;
        }
    }

    @NotNull
    @Override
    public Object[] toArray() {
        ArrayList result = new ArrayList();
        for (Object element : this) {
            result.add(element);
        }
        return result.toArray();
    }

    @Override
    public boolean add(Object o) {
        if (contains(o)) return false;
        Node<T> m = new Node<>((T) o);
        Pair p = split(root, (T) o);
        root = merge((Node<T>) p.getKey(), merge(m, (Node<T>) p.getValue()));
        find((T) o);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        remove(root, (T) o);
        size--;
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection c) {
        boolean result = false;
        for (Object element : c) {
            add(element);
            result = true;
        }
        return result;
    }

    @Override
    public void clear() {
        for (Object element : this) {
            remove(element);
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection c) {
        boolean result = false;
        for (Object element : c) {
            remove(element);
            result = true;
        }
        return result;
    }

    @Override
    public boolean retainAll(@NotNull Collection c) {
        boolean result = false;
        for (Object element : this) {
            if (!c.contains(element)) {
                remove(element);
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean containsAll(@NotNull Collection c) {
        boolean result = true;
        for (Object element : c) {
            if (!contains(element)) {
                result = false;
                break;
            }
        }
        return result;
    }

    @NotNull
    @Override
    public Object[] toArray(@NotNull Object[] a) {
        ArrayList result = new ArrayList();
        for (Object element : this) {
            result.add(element);
        }
        return result.toArray();
    }

    private Pair split(Node<T> node, T x) {
        if (node == null)
            return new Pair(null, null);
        if (node.x.compareTo(x) < 0) {
            Pair pair = split(node.right, x);
            node.right = null;
            return new Pair(merge(node, (Node<T>) pair.getKey()), pair.getValue());
        } else {
            Pair pair = split(node.left, x);
            node.left = null;
            return new Pair(pair.getKey(), merge((Node<T>) pair.getValue(), node));
        }
    }

    private Node<T> merge(Node<T> left, Node<T> right) {
        if (right == null)
            return left;
        else if (left == null)
            return right;
        else if (left.y < right.y) {
            right.left = merge(left, right.left);
            return right;
        } else {
            left.right = merge(left.right, right);
            return left;
        }
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> lnode = node.left;
        node.left = lnode.right;
        lnode.right = node;
        return lnode;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> rnode = node.right;
        node.right = rnode.left;
        rnode.left = node;
        return rnode;
    }

    private Node<T> remove(Node<T> node, T x) {
        if (node == null) {
            return null;
        }
        if (x.compareTo(node.x) < 0) {
            node.left = remove(node.left, x);
        } else if (x.compareTo(node.x) > 0) {
            node.right = remove(node.right, x);
        } else {
            boolean isRoot = node.x == root.x;
            if (node.left == null && node.right == null) {
                node = null;
            } else if (node.left != null && node.right != null) {
                if (node.left.y < node.right.y) {
                    node = rotateLeft(node);
                    node.left = remove(node.left, x);
                } else {
                    node = rotateRight(node);
                    node.right = remove(node.right, x);
                }
            } else {
                if (node.left != null)
                    node = node.left;
                else {
                    node = node.right;
                }
            }
            if (isRoot)
                root = node;
        }
        return node;
    }

    public Node<T> find(T x) {
        Node<T> current = root;
        int i = 0;
        while (current != null && current.x.compareTo(x) != 0)
            if (x.compareTo(current.x) < 0)
                current = current.left;
            else
                current = current.right;
        return current;
    }
}
