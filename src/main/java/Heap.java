import java.util.*;

/**
 * A generic binary heap implemented as an implicit binary tree of
 * {@link Node}s.
 *
 * <p>The heap invariant is defined by a {@link Comparator}: for every node,
 * {@code comparator.compare(parent, child) <= 0} must hold, so the root is
 * always the element that sorts first. The position of the next node to
 * insert or of the last node to pop is derived from the binary
 * representation of the size, so no parent pointers are needed.</p>
 *
 * @param <T> the type of values stored in the heap; must be
 *            {@link Comparable} so values can be ordered
 */
public class Heap<T extends Comparable<T>> {

    /** The root node of the tree, or {@code null} when the tree is empty. */
    private Node<T> root;

    /** The comparator defining the heap ordering. */
    private final Comparator<T> comparator;

    /** The number of nodes currently stored in the tree. */
    private int size;

    /**
     * Creates a new, empty heap ordering values by their natural ordering.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     */
    public Heap() {
        this(null);
    }

    /**
     * Creates a new, empty heap ordering values with the given comparator.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     *
     * @param comparator the comparator used to determine value ordering
     */
    public Heap(Comparator<T> comparator) {
        this.comparator = comparator != null ? comparator : Comparable::compareTo;
    }

    /**
     * Returns the root node of this heap.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     *
     * @return the root node, or {@code null} if the heap is empty
     */
    public Node<T> getRoot() {
        return root;
    }

    /**
     * Inserts a new node holding the given value into the heap.
     *
     * <p>The new node is attached at the next available leaf position
     * (derived from the binary representation of the size) and then bubbled
     * up toward the root until the heap invariant is restored.</p>
     *
     * Time Complexity: O(logn) where n is the number of nodes.
     * Space Complexity: O(h) where h is the height of the tree, for the
     * stack of nodes visited on the way down.
     *
     * @param value the value to insert; must not be null
     * @return the node holding the inserted value
     */
    public Node<T> insert(T value) {
        assert value != null;
        Node<T> node = new Node<>(value);
        size++;
        if (root == null) {
            root = node;
            return root;
        }
        var curr = root;

        // The binary representation of the size gives us the path from the root.
        // Skipping the first '1', a '0' means go left, and a '1' means go right.
        String path = Integer.toBinaryString(size);

        // Traverse down to the parent of where the new node should be
        Stack<Node<T>> visitedNodes = new Stack<>();
        for (int i = 1; i < path.length() - 1; i++) {
            visitedNodes.add(curr);
            if (path.charAt(i) == '0') {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        // Attach the new node
        if (path.charAt(path.length() - 1) == '0') {
            curr.left = node;
        } else {
            curr.right = node;
        }

        // Bubble the new value up while it sorts before its parent
        while (comparator.compare(node.value, curr.value) < 0) {
            T tmp = node.value;
            node.value = curr.value;
            curr.value = tmp;
            if (visitedNodes.isEmpty()) {
                break;
            } else {
                node = curr;
                curr = visitedNodes.pop();
            }
        }

        return node;
    }

    /**
     * Removes and returns the root node of this heap.
     *
     * <p>The last node's value is swapped with the root's value, the last
     * node is detached, and the root's value is sifted down until the heap
     * invariant is restored. The returned node is the detached last node,
     * which now holds the value that was at the root.</p>
     *
     * Time Complexity: O(logn) where n is the number of nodes.
     * Space Complexity: O(1).
     *
     * @return the popped node, or {@code null} if the heap is empty
     */
    public Node<T> pop() {
        if (root == null) {
            return null;
        }
        if (size == 1) {
            Node<T> poppedNode = root;
            root = null;
            size = 0;
            return poppedNode;
        }
        var curr = root;

        // The binary representation of the size gives us the path from the root.
        // Skipping the first '1', a '0' means go left, and a '1' means go right.
        String path = Integer.toBinaryString(size);

        // Traverse down to the last node, keeping track of its parent
        Node<T> prev = curr;
        for (int i = 1; i < path.length(); i++) {
            prev = curr;
            if (path.charAt(i) == '0') {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        // Swap the root value with the last node's value so the last node
        // ends up holding the value being popped
        T tmp = root.value;
        root.value = curr.value;
        curr.value = tmp;

        // Detach the last node
        if (path.charAt(path.length() - 1) == '0') {
            prev.left = null;
        } else {
            prev.right = null;
        }
        size--;

        // Sift the root value down while it sorts after one of its children
        Node<T> sift = root;
        while (sift.left != null) {
            Node<T> lesserNode = sift.left;
            if (sift.right != null && comparator.compare(sift.right.value, lesserNode.value) < 0) {
                lesserNode = sift.right;
            }
            if (comparator.compare(lesserNode.value, sift.value) < 0) {
                T swap = sift.value;
                sift.value = lesserNode.value;
                lesserNode.value = swap;
                sift = lesserNode;
            } else {
                break;
            }
        }

        curr.left = null;
        curr.right = null;
        return curr;
    }

    /**
     * Performs a level-order (breadth-first) traversal, visiting all nodes at
     * each depth from left to right before descending to the next level.
     *
     * Time Complexity: O(n) where n is the size.
     * Space Complexity: O(n) where n is the size.
     *
     * @return a list of the nodes visited in level-order, or an empty list if
     *         the tree is empty
     */
    public List<Node<T>> toLevelOrder() {
        List<Node<T>> levelOrder = new ArrayList<>();
        if (root == null) {
            return levelOrder;
        }
        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<T> node = queue.remove();
            levelOrder.add(node);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return levelOrder;
    }

}
