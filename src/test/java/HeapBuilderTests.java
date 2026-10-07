import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HeapBuilder}.
 *
 * <p>Verifies {@link HeapBuilder#createMinHeap(List)} and
 * {@link HeapBuilder#createMaxHeap(List)} for empty, single-element,
 * unsorted, sorted, duplicate, negative, and boundary-value lists, and
 * that the returned roots satisfy the heap and complete-tree properties.</p>
 */
public class HeapBuilderTests {

    // ------------------------------------------------------------------
    // empty list
    // ------------------------------------------------------------------

    @Test
    public void testCreateMinHeap_EmptyList_ReturnsNull() {
        assertNull(HeapBuilder.createMinHeap(List.<Integer>of()));
    }

    @Test
    public void testCreateMaxHeap_EmptyList_ReturnsNull() {
        assertNull(HeapBuilder.createMaxHeap(List.<Integer>of()));
    }

    // ------------------------------------------------------------------
    // single element
    // ------------------------------------------------------------------

    @Test
    public void testCreateMinHeap_SingleElement_IsLeafRoot() {
        Node<Integer> root = HeapBuilder.createMinHeap(List.of(42));
        assertNotNull(root);
        assertEquals(42, root.value);
        assertNull(root.left);
        assertNull(root.right);
    }

    @Test
    public void testCreateMaxHeap_SingleElement_IsLeafRoot() {
        Node<Integer> root = HeapBuilder.createMaxHeap(List.of(42));
        assertNotNull(root);
        assertEquals(42, root.value);
        assertNull(root.left);
        assertNull(root.right);
    }

    // ------------------------------------------------------------------
    // ordering
    // ------------------------------------------------------------------

    @Test
    public void testCreateMinHeap_UnsortedList_RootIsMinimum() {
        Node<Integer> root = HeapBuilder.createMinHeap(List.of(9, 3, 7, 1, 5, 8, 2, 6, 4));
        assertEquals(1, root.value);
        assertMinHeapProperty(root);
        assertCompleteTree(root);
        assertEquals(9, countNodes(root));
    }

    @Test
    public void testCreateMaxHeap_UnsortedList_RootIsMaximum() {
        Node<Integer> root = HeapBuilder.createMaxHeap(List.of(9, 3, 7, 1, 5, 8, 2, 6, 4));
        assertEquals(9, root.value);
        assertMaxHeapProperty(root);
        assertCompleteTree(root);
        assertEquals(9, countNodes(root));
    }

    @Test
    public void testCreateMinHeap_SortedAscending_RootIsFirst() {
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            values.add(i);
        }
        Node<Integer> root = HeapBuilder.createMinHeap(values);
        assertEquals(1, root.value);
        assertMinHeapProperty(root);
        assertCompleteTree(root);
        assertEquals(10, countNodes(root));
    }

    @Test
    public void testCreateMaxHeap_SortedDescending_RootIsFirst() {
        List<Integer> values = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            values.add(i);
        }
        Node<Integer> root = HeapBuilder.createMaxHeap(values);
        assertEquals(10, root.value);
        assertMaxHeapProperty(root);
        assertCompleteTree(root);
        assertEquals(10, countNodes(root));
    }

    @Test
    public void testCreateMaxHeap_SortedAscendingInput_RootIsLast() {
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            values.add(i);
        }
        Node<Integer> root = HeapBuilder.createMaxHeap(values);
        assertEquals(10, root.value);
        assertMaxHeapProperty(root);
    }

    // ------------------------------------------------------------------
    // duplicates
    // ------------------------------------------------------------------

    @Test
    public void testCreateMinHeap_Duplicates_Preserved() {
        List<Integer> values = List.of(4, 4, 1, 2, 2, 1);
        Node<Integer> root = HeapBuilder.createMinHeap(values);
        assertEquals(1, root.value);
        assertMinHeapProperty(root);
        assertEquals(6, countNodes(root));
    }

    @Test
    public void testCreateMaxHeap_Duplicates_Preserved() {
        List<Integer> values = List.of(4, 4, 1, 2, 2, 1);
        Node<Integer> root = HeapBuilder.createMaxHeap(values);
        assertEquals(4, root.value);
        assertMaxHeapProperty(root);
        assertEquals(6, countNodes(root));
    }

    @Test
    public void testCreateMinHeap_AllEqualValues() {
        List<Integer> values = List.of(3, 3, 3, 3, 3);
        Node<Integer> root = HeapBuilder.createMinHeap(values);
        assertEquals(3, root.value);
        assertEquals(5, countNodes(root));
        assertCompleteTree(root);
    }

    // ------------------------------------------------------------------
    // edge values
    // ------------------------------------------------------------------

    @Test
    public void testCreateMinHeap_NegativeValues_RootIsSmallest() {
        Node<Integer> root = HeapBuilder.createMinHeap(List.of(-5, -1, -10, 0, 3, -7));
        assertEquals(-10, root.value);
        assertMinHeapProperty(root);
        assertCompleteTree(root);
    }

    @Test
    public void testCreateMaxHeap_NegativeValues_RootIsLargest() {
        Node<Integer> root = HeapBuilder.createMaxHeap(List.of(-5, -1, -10, 0, 3, -7));
        assertEquals(3, root.value);
        assertMaxHeapProperty(root);
        assertCompleteTree(root);
    }

    @Test
    public void testCreateMinHeap_ZeroAndBoundaryValues() {
        List<Integer> values = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        Node<Integer> root = HeapBuilder.createMinHeap(values);
        assertEquals(Integer.MIN_VALUE, root.value);
        assertMinHeapProperty(root);
        assertEquals(3, countNodes(root));
    }

    @Test
    public void testCreateMaxHeap_ZeroAndBoundaryValues() {
        List<Integer> values = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        Node<Integer> root = HeapBuilder.createMaxHeap(values);
        assertEquals(Integer.MAX_VALUE, root.value);
        assertMaxHeapProperty(root);
        assertEquals(3, countNodes(root));
    }

    @Test
    public void testCreateMinHeap_OnlyMinValue() {
        Node<Integer> root = HeapBuilder.createMinHeap(List.of(Integer.MIN_VALUE));
        assertNotNull(root);
        assertEquals(Integer.MIN_VALUE, root.value);
    }

    // ------------------------------------------------------------------
    // randomized
    // ------------------------------------------------------------------

    @Test
    public void testCreateMinHeap_RandomValues_MatchesModel() {
        Random random = new Random(42);
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            values.add(random.nextInt(1001) - 500);
        }
        Node<Integer> root = HeapBuilder.createMinHeap(values);
        assertEquals(values.stream().min(Integer::compareTo).orElseThrow(), root.value);
        assertMinHeapProperty(root);
        assertCompleteTree(root);
        assertEquals(values.size(), countNodes(root));
    }

    @Test
    public void testCreateMaxHeap_RandomValues_MatchesModel() {
        Random random = new Random(99);
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            values.add(random.nextInt(1001) - 500);
        }
        Node<Integer> root = HeapBuilder.createMaxHeap(values);
        assertEquals(values.stream().max(Integer::compareTo).orElseThrow(), root.value);
        assertMaxHeapProperty(root);
        assertCompleteTree(root);
        assertEquals(values.size(), countNodes(root));
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

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

    /**
     * Asserts that the tree is a complete binary tree (left-packed) by
     * performing a level-order traversal that also visits null children.
     *
     * @param root the root of the tree to verify; may be null
     */
    private void assertCompleteTree(Node<Integer> root) {
        Queue<Node<Integer>> queue = new LinkedList<>();
        queue.add(root);
        boolean sawNull = false;
        while (!queue.isEmpty()) {
            Node<Integer> node = queue.remove();
            if (node == null) {
                sawNull = true;
            } else {
                assertFalse(sawNull, "node appears after a missing child; tree is not complete");
                queue.add(node.left);
                queue.add(node.right);
            }
        }
    }

    /**
     * Counts the nodes in the tree rooted at the given node.
     *
     * @param root the root of the tree; may be null
     * @return the number of nodes
     */
    private int countNodes(Node<Integer> root) {
        if (root == null) {
            return 0;
        }
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

}
