import java.util.Comparator;
import java.util.List;

/**
 * Transforms a {@link BST} into a binary {@link Heap} containing the same
 * values.
 *
 * <p>Provides static methods for converting a binary search tree into a
 * min-heap or a max-heap. The tree's values are read in in-order traversal
 * and inserted into the heap, which rearranges them so the root holds the
 * smallest (min-heap) or largest (max-heap) value.</p>
 *
 * @param <T> the type of values stored in the tree and heap; must be
 *            {@link Comparable} so values can be ordered
 */
public class BSTToHeapTransformer<T extends Comparable<T>> {

    /**
     * Private so that this utility class cannot be instantiated; use
     * {@link #bstToMinHeap(BST)} or {@link #bstToMaxHeap(BST)}.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     */
    private BSTToHeapTransformer() {}

    /**
     * Converts the given tree into a min-heap and returns its nodes in
     * level-order.
     *
     * <p>Values are added in ascending order for slight optimization (they
     * do not have to bubble up).</p>
     *
     * Time Complexity: O(n logn) where n is the number of nodes in the tree.
     * Space Complexity: O(n) where n is the number of nodes in the tree.
     *
     * @param bst the tree to convert
     * @param <T> the type of values; must be {@link Comparable}
     * @return the nodes of the resulting min-heap in level-order
     */
    public static <T extends Comparable<T>> List<Node<T>> bstToMinHeap(BST<T> bst) {
        assert bst != null;
        // Add values in ascending order for slight optimization (do not have to bubble up).
        Heap<T> heap = new Heap<>();
        List<Node<T>> nodeList = bst.inOrderTraversal();
        nodeList.forEach(node -> {
            heap.insert(node.value);
        });
        return heap.toLevelOrder();
    }

    /**
     * Converts the given tree into a max-heap and returns its nodes in
     * level-order.
     *
     * <p>Values are added in descending order for slight optimization (they
     * do not have to bubble up).</p>
     *
     * Time Complexity: O(n logn) where n is the number of nodes in the tree.
     * Space Complexity: O(n) where n is the number of nodes in the tree.
     *
     * @param bst the tree to convert
     * @param <T> the type of values; must be {@link Comparable}
     * @return the nodes of the resulting max-heap in level-order
     */
    public static <T extends Comparable<T>> List<Node<T>> bstToMaxHeap(BST<T> bst) {
        assert bst != null;
        // Add values in descending order for slight optimization (do not have to bubble up).
        Comparator<T> comparator = Comparable::compareTo;
        Heap<T> heap = new Heap<>(comparator.reversed());
        List<Node<T>> nodeList = bst.inOrderTraversal();
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            heap.insert(nodeList.get(i).value);
        }
        return heap.toLevelOrder();
    }

}
