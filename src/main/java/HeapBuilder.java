import java.util.Comparator;
import java.util.List;

/**
 * Builds binary {@link Heap}s from lists of values.
 *
 * <p>Provides factory methods for creating min-heaps and max-heaps from a
 * list of values, returning the root node of the resulting heap.</p>
 */
public class HeapBuilder {

    /**
     * Private so that this utility class cannot be instantiated; use
     * {@link #createMinHeap(List)} or {@link #createMaxHeap(List)}.
     *
     * Time Complexity: O(1).
     * Space Complexity: O(1).
     */
    private HeapBuilder() {}

    /**
     * Inserts all of the given values into the given heap and returns the
     * root node of the resulting heap.
     *
     * Time Complexity: O(n logn) where n is the number of values (each
     * insert is O(logn)).
     * Space Complexity: O(n) where n is the number of values, for the heap
     * storage.
     *
     * @param values the values to insert into the heap
     * @param heap   the heap to insert the values into
     * @param <T>    the type of values; must be {@link Comparable}
     * @return the root node of the resulting heap
     */
    private static <T extends Comparable<T>> Node<T> createHeap(List<T> values, Heap<T> heap) {
        values.forEach(heap::insert);
        return heap.getRoot();
    }

    /**
     * Creates a min-heap containing the given values and returns its root
     * node.
     *
     * Time Complexity: O(n logn) where n is the number of values.
     * Space Complexity: O(n) where n is the number of values.
     *
     * @param values the values to store in the min-heap
     * @param <T>    the type of values; must be {@link Comparable}
     * @return the root node of the resulting min-heap
     */
    public static <T extends Comparable<T>> Node<T> createMinHeap(List<T> values) {
        Heap<T> heap = new Heap<>();
        return createHeap(values, heap);
    }

    /**
     * Creates a max-heap containing the given values and returns its root
     * node.
     *
     * Time Complexity: O(n logn) where n is the number of values.
     * Space Complexity: O(n) where n is the number of values.
     *
     * @param values the values to store in the max-heap
     * @param <T>    the type of values; must be {@link Comparable}
     * @return the root node of the resulting max-heap
     */
    public static <T extends Comparable<T>> Node<T> createMaxHeap(List<T> values) {
        Comparator<T> comparator = Comparable::compareTo;
        Heap<T> heap = new Heap<>(comparator.reversed());
        return createHeap(values, heap);
    }
}
