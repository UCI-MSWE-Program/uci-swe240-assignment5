import java.util.*;

/**
 * A binary search tree of {@link Node}s.
 *
 * <p>The tree orders values with the configured {@link Comparator} (natural
 * ordering by default). It is a plain BST: no rotations or rebalancing are
 * applied, so the shape of the tree depends solely on the order in which
 * values are inserted and deleted.</p>
 *
 * @param <T> the type of values stored in the tree; must be {@link Comparable}
 *            so nodes can be ordered
 */
public class BST<T extends Comparable<T>> {

    /** The root node of the tree, or {@code null} when the tree is empty. */
    private Node<T> root;

    /** The comparator defining the value ordering. */
    private final Comparator<T> comparator;

    /** The number of nodes currently stored in the tree. */
    private int size;

    /**
     * Creates a new, empty tree ordering values by their natural ordering.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     */
    public BST() {
        this(Comparable::compareTo);
    }

    /**
     * Creates a new, empty tree ordering values with the given comparator.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     *
     * @param comparator the comparator used to determine value ordering
     */
    public BST(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Inserts a new node holding the given value into the tree.
     *
     * <p>The value is inserted like in a regular BST and the tree is left
     * in whatever shape results; no balancing is performed. If an equal
     * value is already stored, the tree is left unchanged and {@code null}
     * is returned.</p>
     *
     * Time Complexity: O(h) where h is the height of the tree (O(n) worst
     * case where n is the size).
     * Space Complexity: O(1).
     *
     * @param value the value to insert; must not be null
     * @return the newly inserted node, or {@code null} when the tree
     *         already contains an equal value
     */
    public Node<T> insert(T value) {
        assert value != null;
        Node<T> node = new Node<>(value);
        if (root == null) {
            root = node;
        } else {
            Node<T> prev = root;
            Node<T> curr = root;
            while (curr != null) {
                prev = curr;
                int cmp = comparator.compare(value, curr.value);
                if (cmp == 0) {
                    return null;
                } else if (cmp < 0) {
                    curr = curr.left;
                } else {
                    curr = curr.right;
                }
            }
            if (comparator.compare(value, prev.value) < 0) {
                prev.left = node;
            } else {
                prev.right = node;
            }
        }

        size++;
        return node;
    }

    /**
     * Searches the tree for a node holding the given key.
     *
     * Time Complexity: O(h) where h is the height of the tree (O(n) worst
     * case where n is the size).
     * Space Complexity: O(1).
     *
     * @param key the value to search for; must not be null
     * @return the node holding the key, or {@code null} if not found
     */
    public Node<T> search(T key) {
        assert key != null;
        Node<T> node = root;
        while (node != null) {
            int cmp = comparator.compare(key, node.value);
            if (cmp == 0) {
                return node;
            }
            node = cmp < 0 ? node.left : node.right;
        }
        return null;
    }

    /**
     * Deletes the node holding the given key from the tree.
     *
     * <p>The key is removed like in a regular BST (a node with two children
     * is replaced by its in-order successor) and no balancing is performed.</p>
     *
     * Time Complexity: O(h) where h is the height of the tree (O(n) worst
     * case where n is the size).
     * Space Complexity: O(h) where h is the height of the tree.
     *
     * @param key the value of the node to delete; must not be null
     * @return {@code true} if a node was deleted, {@code false} if the key
     *         was not found
     */
    public boolean delete(T key) {
        assert key != null;
        int before = size;
        root = delete(root, key);
        return size != before;
    }

    /**
     * Recursively deletes {@code key} from the subtree rooted at
     * {@code node}.
     *
     * Time Complexity: O(h) where h is the height of the tree (O(n) worst
     * case where n is the size).
     * Space Complexity: O(h) due to the recursion stack, where h is the
     * height of the tree.
     *
     * @param node the root of the subtree; no-op when null
     * @param key the value to delete
     * @return the root of the subtree
     */
    private Node<T> delete(Node<T> node, T key) {
        if (node == null) {
            return null;
        }
        int cmp = comparator.compare(key, node.value);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }
            Node<T> successor = min(node.right);
            node.value = successor.value;
            node.right = delete(node.right, successor.value);
        }
        return node;
    }

    /**
     * Returns the leftmost (minimum) node of the subtree rooted at
     * {@code node}.
     *
     * Time Complexity: O(h) where h is the height of the tree (O(n) worst
     * case where n is the size).
     * Space Complexity: O(1).
     *
     * @param node the root of the subtree; must not be null
     * @return the node holding the smallest value in the subtree
     */
    private Node<T> min(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Performs an in-order (left, root, right) depth-first traversal.
     *
     * Time Complexity: O(n) where n is the size.
     * Space Complexity: O(n) where n is the size.
     *
     * @return a list of the nodes visited in in-order, or an empty list if
     *         the tree is empty
     */
    public List<Node<T>> inOrderTraversal() {
        List<Node<T>> inOrder = new ArrayList<>();
        inOrder(root, inOrder);
        return inOrder;
    }

    /**
     * Recursively visits {@code node} and its subtree in in-order
     * (left, root, right), appending visited nodes to {@code list}.
     *
     * Time Complexity: O(n) where n is the number of nodes in the subtree.
     * Space Complexity: O(h) due to the recursion stack, where h is the
     * height of the tree.
     *
     * @param node the current node of the recursion; no-op when null
     * @param list the list that visited nodes are appended to
     */
    private void inOrder(Node<T> node, List<Node<T>> list) {
        if (node == null) {
            return;
        }
        inOrder(node.left, list);
        list.add(node);
        inOrder(node.right, list);
    }

    /**
     * Performs a pre-order (root, left, right) depth-first traversal.
     *
     * Time Complexity: O(n) where n is the size.
     * Space Complexity: O(n) where n is the size.
     *
     * @return a list of the nodes visited in pre-order, or an empty list if
     *         the tree is empty
     */
    public List<Node<T>> preOrderTraversal() {
        List<Node<T>> preOrder = new ArrayList<>();
        preOrder(root, preOrder);
        return preOrder;
    }

    /**
     * Recursively visits {@code node} and its subtree in pre-order
     * (root, left, right), appending visited nodes to {@code list}.
     *
     * Time Complexity: O(n) where n is the number of nodes in the subtree.
     * Space Complexity: O(h) due to the recursion stack, where h is the
     * height of the tree.
     *
     * @param node the current node of the recursion; no-op when null
     * @param list the list that visited nodes are appended to
     */
    private void preOrder(Node<T> node, List<Node<T>> list) {
        if (node == null) {
            return;
        }
        list.add(node);
        preOrder(node.left, list);
        preOrder(node.right, list);
    }

    /**
     * Performs a post-order (left, right, root) depth-first traversal.
     *
     * Time Complexity: O(n) where n is the size.
     * Space Complexity: O(n) where n is the size.
     *
     * @return a list of the nodes visited in post-order, or an empty list if
     *         the tree is empty
     */
    public List<Node<T>> postOrderTraversal() {
        List<Node<T>> postOrder = new ArrayList<>();
        postOrder(root, postOrder);
        return postOrder;
    }

    /**
     * Recursively visits {@code node} and its subtree in post-order
     * (left, right, root), appending visited nodes to {@code list}.
     *
     * Time Complexity: O(n) where n is the number of nodes in the subtree.
     * Space Complexity: O(h) due to the recursion stack, where h is the
     * height of the tree.
     *
     * @param node the current node of the recursion; no-op when null
     * @param list the list that visited nodes are appended to
     */
    private void postOrder(Node<T> node, List<Node<T>> list) {
        if (node == null) {
            return;
        }
        postOrder(node.left, list);
        postOrder(node.right, list);
        list.add(node);
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
    public List<Node<T>> levelOrderTraversal() {
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
