package tree;

import java.util.Objects;

public class Node<T extends Comparable<T>> {

    private T value;
    private Node<T> leftChild;
    private Node<T> rightChild;
    private Node<T> parent;

    public Node(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(value, node.value) && Objects.equals(leftChild, node.leftChild) && Objects.equals(rightChild, node.rightChild) && Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, leftChild, rightChild, parent);
    }

    public Node<T> getLeftChild() {
        return leftChild;
    }

    public Node<T> getRightChild() {
        return rightChild;
    }

    public Node<T> getParent() {
        return parent;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setLeftChild(Node<T> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node<T> rightChild) {
        this.rightChild = rightChild;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getGrandParent() {
        return parent != null ? parent.getParent() : null;
    }

    public boolean isLeftChild() {
        return this == parent.getLeftChild();
    }

    public boolean isRightChild() {
        return this == parent.getRightChild();
    }
}
