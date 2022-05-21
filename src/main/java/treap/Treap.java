package treap;

import javafx.util.Pair;

import java.util.Random;

public class Treap<T extends Comparable<T>> {
    public static class Node<T> {
        public T x;
        public int y;
        public Node<T> left, right;

        Node(T x) {
            this.x = x;
            Random r = new Random();
            this.y = r.nextInt(1000);
        }
    }


    public Node<T> root;

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

    public Node<T> remove(Node<T> node, T x) {
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

    public boolean add(T x) {
        if (find(x) != null) return false;
        Node<T> m = new Node<>(x);
        Pair p = split(root, x);
        root = merge((Node<T>) p.getKey(), merge(m, (Node<T>) p.getValue()));
        find(x);
        return true;
    }
}
