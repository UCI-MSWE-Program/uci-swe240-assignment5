/**
 * A single node in a binary tree.
 *
 * @param <T> the type of value stored in the node
 */
public class Node<T> {

    /** The value stored in this node. */
    public T value;

    /** The left child of this node, or {@code null} if there is none. */
    public Node<T> left;

    /** The right child of this node, or {@code null} if there is none. */
    public Node<T> right;

    /**
     * Creates a new node holding the given value with no children.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     *
     * @param value the value to store in the node
     */
    public Node(T value) {
        this.value = value;
    }
}
