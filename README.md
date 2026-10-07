# Assignment 5: Heaps and BST to Heap Transformation

Java implementation of the two tasks for SWE 240 Assignment 5: a
`HeapBuilder` that builds min-heaps and max-heaps from a list of
integers, and a `BSTToHeapTransformer` that converts a binary search
tree into a min-heap or max-heap. The `Node`, `Heap`, and `BST` classes
carried over from Assignment 4 are reused. Each task includes JUnit 5
sample test cases. No built-in heap or tree APIs are used.

A heap is a binary tree where each node can have at most two children,
the left child and the right child. For a min-heap, the value of each
node must be less than or equal to the values of its children (i.e.,
the minimum value is at the root). For a max-heap, the value of each
node must be greater than or equal to the values of its children (i.e.,
the maximum value is at the root). Due to the heap order property, the
root of the heap is always the minimum (in a min-heap) or maximum (in a
max-heap) element, making it easy to access the most significant
element without having to search through the entire data structure.

[TOC]

## Reused classes from Assignment 4

The following classes are carried over unchanged from Assignment 4:

*   `src/main/java/Node.java` - a generic binary tree node holding a
    `value` and `left`/`right` child references.
*   `src/main/java/Heap.java` - a generic binary heap built as an
    implicit binary tree of `Node`s. A `Comparator` defines the heap
    invariant (natural ordering by default, `Comparator.reverseOrder()`
    for a max-heap); `insert` and `pop` are O(log n). The next leaf
    position for insertion and the last node for removal are derived
    from the binary representation of the size, so no parent pointers
    are needed.
*   `src/main/java/BST.java` - a generic binary search tree of `Node`s
    with `insert`, `search`, `delete`, and in-order, pre-order,
    post-order, and level-order traversals. It is a plain BST: no
    balancing is performed.

## Task 1: Build a heap from a list of integers

Given a list of integers, write a class `HeapBuilder` that takes the
integer list, creates a heap, and returns that heap as a binary tree.
Both max-heaps and min-heaps must be implemented.

```java
class Node {
    int data;
    Node left;
    Node right;

    Node(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

class HeapBuilder {
    // return the root of the binary tree
    Node createMinHeap(List<Integer> values);

    // return the root of the binary tree
    Node createMaxHeap(List<Integer> values);
}
```

Implementation: `src/main/java/HeapBuilder.java` is a generic utility
class whose static `createMinHeap(List<T>)` and
`createMaxHeap(List<T>)` methods insert every value into a `Heap`
(natural ordering for min-heap, reversed comparator for max-heap) and
return the root node of the resulting heap. An empty list returns
`null`. Each insert is O(log n), so building a heap of n values is
O(n log n) with O(n) space.

### Task 1 sample test cases

`src/test/java/HeapBuilderTests.java` validates:

*   Empty lists return `null` for both min-heap and max-heap.
*   A single-element list returns a root with no children.
*   Unsorted lists produce a root holding the minimum (min-heap) or
    maximum (max-heap) value.
*   Sorted ascending and descending inputs produce correct roots.
*   Duplicate values are all preserved; all-equal values keep the
    complete-tree shape.
*   Negative values, zero, and `Integer.MIN_VALUE`/`Integer.MAX_VALUE`
    boundary values.
*   The returned tree always satisfies the heap property and is a
    complete (left-packed) binary tree.
*   500 randomized values match the model: root is the true
    minimum/maximum, all values are present, and the heap and
    complete-tree properties hold.

The underlying `Heap` class is additionally covered by
`src/test/java/HeapTests.java`, which validates empty-heap operations,
insertion order, `pop()` draining min-heaps ascending and max-heaps
descending, interleaved insert/pop sequences, complete-tree structure,
and randomized stress sequences.

## Task 2: Transform a BST into a min-heap or max-heap

Write a class `BSTToHeapTransformer` that takes the root node of a BST
and converts it into a min-heap and a max-heap. The `BST` class from
Assignment 4 must be used.

```java
class BSTToHeapTransformer {
    List<Node> bstToMinHeap(BST bst);
    List<Node> bstToMaxHeap(BST bst);
}
```

Implementation: `src/main/java/BSTToHeapTransformer.java` reads the
tree's values in in-order traversal (ascending order), inserts them
into a `Heap` (in ascending order for the min-heap, descending order
for the max-heap, so no bubbling up is needed), and returns the nodes
of the resulting heap in level-order. An empty tree returns an empty
list. The transformation is O(n log n) with O(n) space.

### Task 2 sample test cases

`src/test/java/BSTToHeapTransformerTests.java` validates:

*   Empty BSTs return an empty list.
*   A single-node BST returns that value.
*   Balanced BSTs produce a min-heap whose root is the minimum and a
    max-heap whose root is the maximum; popping drains the values in
    sorted (ascending or descending) order.
*   Skewed BSTs are handled correctly.
*   Duplicate inserts into the BST are kept only once, since the BST
    stores unique values.
*   Negative values, zero, and `Integer.MIN_VALUE`/`Integer.MAX_VALUE`
    boundary values.
*   The returned level-order node list satisfies the heap property and
    contains the values in sorted pop order.
*   100 randomized values match the model: the heap property holds and
    the values pop in fully sorted order.

## Build and test

Requires JDK 25 or newer. Build and run the tests with the Gradle
wrapper:

```shell
./gradlew build
./gradlew test
```

## Project layout

```text
src/main/java/Node.java
src/main/java/BST.java
src/main/java/Heap.java
src/main/java/HeapBuilder.java
src/main/java/BSTToHeapTransformer.java
src/test/java/HeapTests.java
src/test/java/HeapBuilderTests.java
src/test/java/BSTToHeapTransformerTests.java
```
