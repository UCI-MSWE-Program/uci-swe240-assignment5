import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Heap}.
 *
 * <p>Verifies min-heap behavior under the natural ordering, max-heaps via
 * {@link Comparator#reverseOrder()}, empty-heap operations, single-element
 * heaps, duplicates, negative and boundary values, interleaved insert/pop
 * sequences, complete-tree structure, and randomized stress sequences.</p>
 */
public class HeapTests {

    // ------------------------------------------------------------------
    // empty heap
    // ------------------------------------------------------------------

    @Test
    public void testGetRoot_EmptyHeap_ReturnsNull() {
        Heap<Integer> heap = new Heap<>();
        assertNull(heap.getRoot());
    }

    @Test
    public void testPop_EmptyHeap_ReturnsNull() {
        assertNull(new Heap<Integer>().pop());
    }

    @Test
    public void testPop_MaxHeapEmpty_ReturnsNull() {
        assertNull(new Heap<Integer>(Comparator.reverseOrder()).pop());
    }

    // ------------------------------------------------------------------
    // insert()
    // ------------------------------------------------------------------

    @Test
    public void testInsert_SingleElement_BecomesRootWithNoChildren() {
        Heap<Integer> heap = new Heap<>();
        Node<Integer> node = heap.insert(42);
        assertNotNull(node);
        assertEquals(42, node.value);
        assertEquals(42, heap.getRoot().value);
        assertNull(heap.getRoot().left);
        assertNull(heap.getRoot().right);
    }

    @Test
    public void testInsert_InsertedValueIsPresentInTree() {
        Heap<Integer> heap = new Heap<>();
        for (int value : new int[]{5, 1, 9, 3, 7, 0, -2, 4}) {
            Node<Integer> node = heap.insert(value);
            assertNotNull(node);
            assertTrue(contains(heap.getRoot(), value),
                    "tree does not contain inserted value " + value);
        }
        assertMinHeapProperty(heap.getRoot());
    }

    @Test
    public void testInsert_MinHeap_PutsSmallestAtRoot() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(10);
        heap.insert(5);
        heap.insert(20);
        assertEquals(5, heap.getRoot().value);
    }

    @Test
    public void testInsert_MaxHeap_PutsLargestAtRoot() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        heap.insert(10);
        heap.insert(5);
        heap.insert(20);
        assertEquals(20, heap.getRoot().value);
    }

    @Test
    public void testInsert_AscendingValues_MinHeapRootIsSmallest() {
        Heap<Integer> heap = new Heap<>();
        for (int i = 1; i <= 10; i++) {
            heap.insert(i);
        }
        assertEquals(1, heap.getRoot().value);
        assertMinHeapProperty(heap.getRoot());
    }

    @Test
    public void testInsert_DescendingValues_MaxHeapRootIsLargest() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        for (int i = 10; i >= 1; i--) {
            heap.insert(i);
        }
        assertEquals(10, heap.getRoot().value);
        assertMaxHeapProperty(heap.getRoot());
    }

    @Test
    public void testInsert_Duplicates_MinHeapPreservesAll() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(3);
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        heap.insert(2);
        assertEquals(5, countNodes(heap.getRoot()));
        assertEquals(1, heap.getRoot().value);
        assertEquals(List.of(1, 2, 2, 3, 3), drain(heap));
    }

    @Test
    public void testInsert_Duplicates_MaxHeapPreservesAll() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        heap.insert(3);
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        heap.insert(2);
        assertEquals(3, heap.getRoot().value);
        assertEquals(List.of(3, 3, 2, 2, 1), drain(heap));
    }

    @Test
    public void testInsert_ManyValues_KeepsCompleteTreeAndHeapProperty() {
        Heap<Integer> heap = new Heap<>();
        int[] values = {15, 3, 8, 1, 27, 11, 5, 19, 0, 33, 7, 22, 13, 4, 30};
        for (int value : values) {
            heap.insert(value);
            assertCompleteTree(heap.getRoot());
            assertMinHeapProperty(heap.getRoot());
        }
        assertEquals(values.length, countNodes(heap.getRoot()));
    }

    // ------------------------------------------------------------------
    // pop()
    // ------------------------------------------------------------------

    @Test
    public void testPop_SingleElement_EmptiesHeap() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(7);
        Node<Integer> popped = heap.pop();
        assertNotNull(popped);
        assertEquals(7, popped.value);
        assertNull(heap.getRoot());
        assertNull(heap.pop());
    }

    @Test
    public void testPop_MinHeap_DrainsAscending() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(5);
        heap.insert(1);
        heap.insert(4);
        heap.insert(2);
        heap.insert(3);
        assertEquals(List.of(1, 2, 3, 4, 5), drain(heap));
        assertNull(heap.getRoot());
    }

    @Test
    public void testPop_MaxHeap_DrainsDescending() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        heap.insert(5);
        heap.insert(1);
        heap.insert(4);
        heap.insert(2);
        heap.insert(3);
        assertEquals(List.of(5, 4, 3, 2, 1), drain(heap));
        assertNull(heap.getRoot());
    }

    @Test
    public void testPop_ReturnsDetachedNode() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        Node<Integer> popped = heap.pop();
        assertEquals(1, popped.value);
        assertNull(popped.left);
        assertNull(popped.right);
        assertEquals(2, countNodes(heap.getRoot()));
    }

    @Test
    public void testPop_AfterDrain_HeapCanBeReused() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(9);
        heap.insert(4);
        heap.insert(6);
        assertEquals(List.of(4, 6, 9), drain(heap));
        assertNull(heap.getRoot());

        heap.insert(2);
        heap.insert(8);
        heap.insert(5);
        assertEquals(List.of(2, 5, 8), drain(heap));
    }

    @Test
    public void testInsertPop_Interleaved_MaintainsMinOrder() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(5);
        assertEquals(5, heap.pop().value);

        heap.insert(1);
        heap.insert(9);
        assertEquals(1, heap.pop().value);

        heap.insert(3);
        assertEquals(3, heap.pop().value);
        assertEquals(9, heap.pop().value);
        assertNull(heap.pop());
    }

    @Test
    public void testPop_AfterEachPop_HeapPropertyHolds() {
        Heap<Integer> heap = new Heap<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 35, 45, 65, 75};
        for (int value : values) {
            heap.insert(value);
        }
        List<Integer> popped = new ArrayList<>();
        Node<Integer> node;
        while ((node = heap.pop()) != null) {
            popped.add(node.value);
            assertMinHeapProperty(heap.getRoot());
        }
        popped.sort(Integer::compareTo);
        List<Integer> expected = new ArrayList<>();
        for (int value : values) {
            expected.add(value);
        }
        expected.sort(Integer::compareTo);
        assertEquals(expected, popped);
    }

    // ------------------------------------------------------------------
    // edge values
    // ------------------------------------------------------------------

    @Test
    public void testInsert_NegativeValues_MinHeapDrainsAscending() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(-5);
        heap.insert(-1);
        heap.insert(-10);
        heap.insert(0);
        heap.insert(3);
        heap.insert(-7);
        assertEquals(List.of(-10, -7, -5, -1, 0, 3), drain(heap));
    }

    @Test
    public void testInsert_NegativeValues_MaxHeapDrainsDescending() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        heap.insert(-5);
        heap.insert(-1);
        heap.insert(-10);
        heap.insert(0);
        heap.insert(3);
        heap.insert(-7);
        assertEquals(List.of(3, 0, -1, -5, -7, -10), drain(heap));
    }

    @Test
    public void testInsert_ZeroAndBoundaryValues_MinHeap() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(Integer.MIN_VALUE);
        heap.insert(Integer.MAX_VALUE);
        heap.insert(0);
        assertEquals(Integer.MIN_VALUE, heap.getRoot().value);
        assertEquals(List.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE), drain(heap));
    }

    @Test
    public void testInsert_ZeroAndBoundaryValues_MaxHeap() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        heap.insert(Integer.MIN_VALUE);
        heap.insert(Integer.MAX_VALUE);
        heap.insert(0);
        assertEquals(Integer.MAX_VALUE, heap.getRoot().value);
        assertEquals(List.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), drain(heap));
    }

    @Test
    public void testInsert_OnlyMinValue_MinHeap() {
        Heap<Integer> heap = new Heap<>();
        heap.insert(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, heap.getRoot().value);
        assertEquals(List.of(Integer.MIN_VALUE), drain(heap));
    }

    @Test
    public void testInsert_AllEqualValues_DrainKeepsAll() {
        Heap<Integer> heap = new Heap<>();
        for (int i = 0; i < 7; i++) {
            heap.insert(3);
        }
        assertEquals(7, countNodes(heap.getRoot()));
        List<Integer> drained = drain(heap);
        assertEquals(7, drained.size());
        for (int value : drained) {
            assertEquals(3, value);
        }
    }

    // ------------------------------------------------------------------
    // randomized stress
    // ------------------------------------------------------------------

    @Test
    public void testRandomized_InterleavedInsertPop_MatchesModel() {
        Heap<Integer> heap = new Heap<>();
        List<Integer> model = new ArrayList<>();
        Random random = new Random(42);
        for (int i = 0; i < 2000; i++) {
            if (random.nextBoolean()) {
                int value = random.nextInt(201) - 100;
                heap.insert(value);
                model.add(value);
            } else {
                Node<Integer> popped = heap.pop();
                if (model.isEmpty()) {
                    assertNull(popped);
                } else {
                    int min = model.stream().min(Integer::compareTo).orElseThrow();
                    assertNotNull(popped);
                    assertEquals(min, popped.value);
                    model.remove((Integer) min);
                }
            }
        }
        List<Integer> drained = drain(heap);
        model.sort(Integer::compareTo);
        assertEquals(model, drained);
        assertNull(heap.getRoot());
    }

    @Test
    public void testRandomized_MaxHeapInterleavedInsertPop_MatchesModel() {
        Heap<Integer> heap = new Heap<>(Comparator.<Integer>reverseOrder());
        List<Integer> model = new ArrayList<>();
        Random random = new Random(7);
        for (int i = 0; i < 2000; i++) {
            if (random.nextBoolean()) {
                int value = random.nextInt(201) - 100;
                heap.insert(value);
                model.add(value);
            } else {
                Node<Integer> popped = heap.pop();
                if (model.isEmpty()) {
                    assertNull(popped);
                } else {
                    int max = model.stream().max(Integer::compareTo).orElseThrow();
                    assertNotNull(popped);
                    assertEquals(max, popped.value);
                    model.remove((Integer) max);
                }
            }
        }
        List<Integer> drained = drain(heap);
        model.sort(Comparator.reverseOrder());
        assertEquals(model, drained);
        assertNull(heap.getRoot());
    }

    @Test
    public void testRandomized_InsertManyThenDrain_SortedAscending() {
        Heap<Integer> heap = new Heap<>();
        Random random = new Random(123);
        List<Integer> inserted = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(1000) - 500;
            heap.insert(value);
            inserted.add(value);
        }
        assertEquals(1000, countNodes(heap.getRoot()));
        assertMinHeapProperty(heap.getRoot());
        inserted.sort(Integer::compareTo);
        assertEquals(inserted, drain(heap));
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    /**
     * Pops the heap until it is empty, collecting the popped values.
     *
     * @param heap the heap to drain
     * @return the popped values in pop order
     */
    private List<Integer> drain(Heap<Integer> heap) {
        List<Integer> result = new ArrayList<>();
        Node<Integer> node;
        while ((node = heap.pop()) != null) {
            result.add(node.value);
        }
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

    /**
     * Returns whether the tree rooted at the given node holds the value.
     *
     * @param root the root of the tree; may be null
     * @param value the value to search for
     * @return true when a node holds the value
     */
    private boolean contains(Node<Integer> root, int value) {
        if (root == null) {
            return false;
        }
        return root.value == value || contains(root.left, value) || contains(root.right, value);
    }

}
