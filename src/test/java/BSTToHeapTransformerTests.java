import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BSTToHeapTransformer}.
 *
 * <p>Verifies {@link BSTToHeapTransformer#bstToMinHeap(BST)} and
 * {@link BSTToHeapTransformer#bstToMaxHeap(BST)} for empty, single-node,
 * balanced, skewed, duplicate, negative, and boundary-value BSTs, and that
 * the returned level-order node lists satisfy the heap property and contain
 * the values in sorted pop order.</p>
 */
public class BSTToHeapTransformerTests {

    // ------------------------------------------------------------------
    // empty BST
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_EmptyBst_ReturnsEmptyList() {
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(new BST<Integer>());
        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testBstToMaxHeap_EmptyBst_ReturnsEmptyList() {
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(new BST<Integer>());
        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }

    // ------------------------------------------------------------------
    // single node
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_SingleNode() {
        BST<Integer> bst = new BST<>();
        bst.insert(5);
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(5, nodes.get(0).value);
        assertEquals(List.of(5), values(nodes));
    }

    @Test
    public void testBstToMaxHeap_SingleNode() {
        BST<Integer> bst = new BST<>();
        bst.insert(5);
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertEquals(5, nodes.get(0).value);
        assertEquals(List.of(5), values(nodes));
    }

    // ------------------------------------------------------------------
    // balanced trees
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_BalancedTree_PopsAscending() {
        BST<Integer> bst = new BST<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(20, nodes.get(0).value);
        assertMinHeapProperty(nodes.get(0));
        assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), valuesSortedAscending(nodes));
    }

    @Test
    public void testBstToMaxHeap_BalancedTree_PopsDescending() {
        BST<Integer> bst = new BST<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertEquals(80, nodes.get(0).value);
        assertMaxHeapProperty(nodes.get(0));
        assertEquals(List.of(80, 70, 60, 50, 40, 30, 20), valuesSortedDescending(nodes));
    }

    @Test
    public void testBstToMinHeap_BalancedTree_ContainsAllValues() {
        BST<Integer> bst = new BST<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        List<Integer> expected = new ArrayList<>();
        for (int value : values) {
            expected.add(value);
        }
        expected.sort(Integer::compareTo);
        assertEquals(expected, valuesSortedAscending(nodes));
    }

    // ------------------------------------------------------------------
    // skewed trees
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_SkewedTree_PopsAscending() {
        BST<Integer> bst = new BST<>();
        for (int i = 1; i <= 10; i++) {
            bst.insert(i);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(1, nodes.get(0).value);
        assertMinHeapProperty(nodes.get(0));
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), valuesSortedAscending(nodes));
    }

    @Test
    public void testBstToMaxHeap_SkewedTree_PopsDescending() {
        BST<Integer> bst = new BST<>();
        for (int i = 1; i <= 10; i++) {
            bst.insert(i);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertEquals(10, nodes.get(0).value);
        assertMaxHeapProperty(nodes.get(0));
        assertEquals(List.of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1), valuesSortedDescending(nodes));
    }

    // ------------------------------------------------------------------
    // duplicates in BST are skipped
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_DuplicateInserts_KeptOnlyOnce() {
        BST<Integer> bst = new BST<>();
        int[] values = {5, 5, 1, 1, 3, 3, 2, 2};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(4, nodes.size());
        assertEquals(List.of(1, 2, 3, 5), valuesSortedAscending(nodes));
    }

    @Test
    public void testBstToMaxHeap_DuplicateInserts_KeptOnlyOnce() {
        BST<Integer> bst = new BST<>();
        int[] values = {5, 5, 1, 1, 3, 3, 2, 2};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertEquals(4, nodes.size());
        assertEquals(List.of(5, 3, 2, 1), valuesSortedDescending(nodes));
    }

    // ------------------------------------------------------------------
    // edge values
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_NegativeAndBoundaryValues_PopsAscending() {
        BST<Integer> bst = new BST<>();
        int[] values = {-10, 0, Integer.MIN_VALUE, 5, Integer.MAX_VALUE, -3};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(Integer.MIN_VALUE, nodes.get(0).value);
        assertMinHeapProperty(nodes.get(0));
        assertEquals(List.of(Integer.MIN_VALUE, -10, -3, 0, 5, Integer.MAX_VALUE), valuesSortedAscending(nodes));
    }

    @Test
    public void testBstToMaxHeap_NegativeAndBoundaryValues_PopsDescending() {
        BST<Integer> bst = new BST<>();
        int[] values = {-10, 0, Integer.MIN_VALUE, 5, Integer.MAX_VALUE, -3};
        for (int value : values) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertEquals(Integer.MAX_VALUE, nodes.get(0).value);
        assertMaxHeapProperty(nodes.get(0));
        assertEquals(List.of(Integer.MAX_VALUE, 5, 0, -3, -10, Integer.MIN_VALUE), valuesSortedDescending(nodes));
    }

    @Test
    public void testBstToMinHeap_OnlyMinValue() {
        BST<Integer> bst = new BST<>();
        bst.insert(Integer.MIN_VALUE);
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(Integer.MIN_VALUE, nodes.get(0).value);
        assertEquals(List.of(Integer.MIN_VALUE), values(nodes));
    }

    @Test
    public void testBstToMaxHeap_OnlyMinValue() {
        BST<Integer> bst = new BST<>();
        bst.insert(Integer.MIN_VALUE);
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertEquals(Integer.MIN_VALUE, nodes.get(0).value);
        assertEquals(List.of(Integer.MIN_VALUE), values(nodes));
    }

    @Test
    public void testBstToMinHeap_ZeroValue() {
        BST<Integer> bst = new BST<>();
        bst.insert(0);
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertEquals(0, nodes.get(0).value);
        assertEquals(List.of(0), values(nodes));
    }

    // ------------------------------------------------------------------
    // randomized
    // ------------------------------------------------------------------

    @Test
    public void testBstToMinHeap_RandomValues_PopsSorted() {
        BST<Integer> bst = new BST<>();
        Set<Integer> inserted = new HashSet<>();
        Random random = new Random(42);
        while (inserted.size() < 100) {
            inserted.add(random.nextInt(1000) - 500);
        }
        for (int value : inserted) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMinHeap(bst);
        assertMinHeapProperty(nodes.get(0));

        List<Integer> expected = new ArrayList<>(inserted);
        Collections.sort(expected);
        assertEquals(expected, valuesSortedAscending(nodes));
    }

    @Test
    public void testBstToMaxHeap_RandomValues_PopsSortedDescending() {
        BST<Integer> bst = new BST<>();
        Set<Integer> inserted = new HashSet<>();
        Random random = new Random(7);
        while (inserted.size() < 100) {
            inserted.add(random.nextInt(1000) - 500);
        }
        for (int value : inserted) {
            bst.insert(value);
        }
        List<Node<Integer>> nodes = BSTToHeapTransformer.bstToMaxHeap(bst);
        assertMaxHeapProperty(nodes.get(0));

        List<Integer> expected = new ArrayList<>(inserted);
        expected.sort(Comparator.reverseOrder());
        assertEquals(expected, valuesSortedDescending(nodes));
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    /**
     * Collects the values of the given level-order nodes in traversal order.
     *
     * @param nodes the level-order node list
     * @return the node values in list order
     */
    private List<Integer> values(List<Node<Integer>> nodes) {
        List<Integer> result = new ArrayList<>();
        for (Node<Integer> node : nodes) {
            result.add(node.value);
        }
        return result;
    }

    /**
     * Collects and sorts the node values in ascending order, mirroring the
     * order values would be popped from a min heap.
     *
     * @param nodes the level-order node list
     * @return the node values sorted ascending
     */
    private List<Integer> valuesSortedAscending(List<Node<Integer>> nodes) {
        List<Integer> result = values(nodes);
        Collections.sort(result);
        return result;
    }

    /**
     * Collects and sorts the node values in descending order, mirroring the
     * order values would be popped from a max heap.
     *
     * @param nodes the level-order node list
     * @return the node values sorted descending
     */
    private List<Integer> valuesSortedDescending(List<Node<Integer>> nodes) {
        List<Integer> result = values(nodes);
        result.sort(Comparator.reverseOrder());
        return result;
    }

    /**
     * Asserts that every parent is not greater than its children.
     *
     * @param root the root of the tree to verify; may be null
     */
    private void assertMinHeapProperty(Node<Integer> root) {
        Queue<Node<Integer>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<Integer> node = queue.remove();
            if (node == null) {
                continue;
            }
            if (node.left != null) {
                assertTrue(node.value <= node.left.value,
                        "parent " + node.value + " is greater than left child " + node.left.value);
                queue.add(node.left);
            }
            if (node.right != null) {
                assertTrue(node.value <= node.right.value,
                        "parent " + node.value + " is greater than right child " + node.right.value);
                queue.add(node.right);
            }
        }
    }

    /**
     * Asserts that every parent is not smaller than its children.
     *
     * @param root the root of the tree to verify; may be null
     */
    private void assertMaxHeapProperty(Node<Integer> root) {
        Queue<Node<Integer>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<Integer> node = queue.remove();
            if (node == null) {
                continue;
            }
            if (node.left != null) {
                assertTrue(node.value >= node.left.value,
                        "parent " + node.value + " is smaller than left child " + node.left.value);
                queue.add(node.left);
            }
            if (node.right != null) {
                assertTrue(node.value >= node.right.value,
                        "parent " + node.value + " is smaller than right child " + node.right.value);
                queue.add(node.right);
            }
        }
    }

}
