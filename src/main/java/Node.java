public class Node<T> {
    public Node[] next;

    private T key;
    private int level;

    public Node(T key, int level) {
        this.key = key;
        this.level = level;
        this.next = new Node[level + 1];
    }

    public T getKey() {
        return this.key;
    }

    public int getValue() {
        return this.level;
    }

    @Override
    public String toString() {
        return "[ level " + level + " | key "+ key + " ]";
    }
}
