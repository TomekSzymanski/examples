package mycollections;

import java.util.*;

/**
 * Propretary implementation for simple BST tree (no balancing). Currently supports nearly all {@code Set} interface methods.<BR><BR>
 * <a href="http://pl.wikipedia.org/wiki/Binarne_drzewo_poszukiwa%C5%84">http://pl.wikipedia.org/wiki/Binarne_drzewo_poszukiwa%C5%84</a>
 * <BR>
 * <a href="http://en.wikipedia.org/wiki/Binary_search_tree">http://en.wikipedia.org/wiki/Binary_search_tree</a>
 *
 */
public class MySimpleBSTTreeSet<E> implements Set<E>{

    private Node rootNode;

    /**
     * This counter will be incremented with every operation modifying tree, like add or remove.
     * It is needed for Iterator to check if no changes were done since creation of iterator (since calling {@code }iterator()} method on the tree).
     * If iterator detects changes then it will trow ConcurrentModificationException (fail-fast iterator)
     */
    private int modificationCounter;

    /**
     * Comparator to be used in compare operations (needed with many tree operations). If not provided in constructor then we will try to use natural ordering of elements.
     */
    private final Comparator<? super E> comparator;

    /**
     * Constructs new empty set, sorted according to the natural ordering of its elements.
     */
    public MySimpleBSTTreeSet() {
        comparator = null;
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified comparator.
     * @param comparator the comparator that will be used to order this set. If null, the natural ordering of the elements will be used.
     */
    public MySimpleBSTTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns reference to the root node of the tree
     * @return root node of the tree
     */
    public Node getRootNode() {
        return rootNode;
    }

    @Override
    public int size() {
        return subTreeSize(rootNode);
    }

    /**
     * calculates size (number of elements) of subtree
     * @param subTreeRoot subtree root to start
     * @return numbr of elements in subtree
     */
    private int subTreeSize(Node subTreeRoot) {
        if (subTreeRoot==null) return 0;
        return 1 + subTreeSize(subTreeRoot.left) + subTreeSize(subTreeRoot.right);
    }

    @Override
    public boolean isEmpty() {
        return (rootNode == null)? true: false;
    }

    /**
     * Returns true if this set contains the specified element.
     * @param e object to be checked for containment in this set
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean contains(Object e) {
        return (subTreeContains((E)e, rootNode) != null)? true : false;
    }

    /**
     * Returns reference to the node containing given value. If no such node found found returns {@code null}.
     * @param value value to search for
     * @param currentNode node to start search with
     * @return reference to the node containing given value
     */
    private Node subTreeContains(E value, Node<E> currentNode) {
        if (currentNode == null) return null;

        if (compare(value, currentNode.value) > 0 ) {
            return subTreeContains(value, currentNode.right);
        } else if (compare(value, currentNode.value) < 0 ) {
            return subTreeContains(value, currentNode.left);
        } else { // elements the same value
            return currentNode;
        }
    }

    private int compare(E first, E second) {
        return (comparator == null) ?
            ((Comparable<? super E>)first).compareTo(second)
                : comparator.compare(first, second);
    }


    /**
     * Returns pre-order tree iterator (visit first parent, then left subtree (recursively), then right subtree (recursively)).
     * Iterator is <i>fail-fast</i>, which means that it will throw a {@link ConcurrentModificationException} if detected that tree was changed, since creation of iterator.
     * @return iterator over tree elements
     */
    @Override
    public Iterator<E> iterator() {
        return new PreOrderTreeIterator<E>(this);
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /**
     * Adds element to the tree. Null values currently not supported.
     * @param e element to be added
     * @return {@code true} if adding changed the tree structure (element was added). {@code false} if add did not change the tree (element was not added, element already existed.).
     */
    @Override
    public boolean add(E e) {
        return addToSubtree(e, rootNode);
    }

    private boolean addToSubtree(E elementToAdd, Node<E> currentRoot) {
        // check if casting to Comparable is possible for E, or if comparator exists
        compare(elementToAdd, elementToAdd);

        //TODO add support for storing null element
        if (currentRoot == null) { // means the whole three is empty.
            // Later method calls itself recursively but makes sure it will not be called for null subtree.
            // May be called for null only for root element
            rootNode = new Node(elementToAdd);
            modificationCounter++;
            return true;
        }
        if (compare(elementToAdd, currentRoot.value) > 0) {
            if (currentRoot.hasRightSon()) {
                return addToSubtree(elementToAdd, currentRoot.right);
            } else {
                currentRoot.right = new Node(elementToAdd);
                currentRoot.right.parent = currentRoot;
                modificationCounter++;
                return true;
            }
        }
        if (compare(elementToAdd, currentRoot.value) < 0) {
            if (currentRoot.hasLeftSon()) {
                return addToSubtree(elementToAdd, currentRoot.left);
            } else {
                currentRoot.left = new Node(elementToAdd);
                currentRoot.left.parent = currentRoot;
                modificationCounter++;
                return true;
            }
        }
        if (compare(elementToAdd, currentRoot.value) == 0) { // element exists, structure does not need to change.
            return false;
        }
        return false;
    }

    /**
     * Removes provided element from the tree.
     * @param o element to be removed
     * @return {@code true} if removing changed the tree structure (element was removed). {@code false} if removal did not change the tree (element was not present in the tree).
     */
    @Override
    public boolean remove(Object o) {

        E valueToRemove;
        try {
            valueToRemove = (E) o;
        } catch (Exception e) {
            throw new ClassCastException("Cannot cast from " + o.getClass() + " to E: "+ e.getMessage()); // type ereasure, do not know E
        }
        Node nodeToRemove = getTreeNodeByValue(valueToRemove);
        if (nodeToRemove == null) { // value not found in set, do not do anything
            return false;
        }

        // if leaf node very simple: remove this node without any more transformations in the tree
        if (nodeToRemove.isLeafNode()) {
            if (nodeToRemove.isLeftSon()) {
                nodeToRemove.parent.left = null;
            } else if (nodeToRemove.isRightSon()) {
                nodeToRemove.parent.right = null;
            }
            nodeToRemove.parent = null;
            modificationCounter++;
            return true;
        }

        if (nodeToRemove.hasOneSonOnly() ) { // remove element and connect its parent with removed element descendant
            Node<E> descendantOfTheNodeToBeRemoved = nodeToRemove.getOnlySon();
            if (nodeToRemove.isLeftSon()) {
                nodeToRemove.parent.left = descendantOfTheNodeToBeRemoved;
            } else if (nodeToRemove.isRightSon()) {
                nodeToRemove.parent.right = descendantOfTheNodeToBeRemoved;
            } else if (!nodeToRemove.hasParent()) {
                rootNode = descendantOfTheNodeToBeRemoved;
            }
            descendantOfTheNodeToBeRemoved.parent = nodeToRemove.parent;
            nodeToRemove.right = null;
            nodeToRemove.parent = null;
            modificationCounter++;
            return true;
        }

        if (nodeToRemove.hasBothSons()) { // put the successor node in place of the removed node
            Node successor = findSuccessor(nodeToRemove);
            assert (successor.isLeafNode() && successor.isLeftSon());

            successor.parent.left = null;

            if (nodeToRemove.isLeftSon()) {
                nodeToRemove.parent.left = successor;
            } else if (nodeToRemove.isRightSon()) {
                nodeToRemove.parent.right = successor;//
            }

            successor.parent = nodeToRemove.parent;

            successor.left = nodeToRemove.left;
            nodeToRemove.left.parent = successor;

            successor.right = nodeToRemove.right;
            nodeToRemove.right.parent = successor;

            nodeToRemove.right = null;
            nodeToRemove.left = null;
            nodeToRemove.parent = null;
            modificationCounter++;
            return true;
        }
        return false;
    }

    /**
     * Finds successor for provided element.
     * Successor is the next element (following the provided element) when traversing tree in the in-order method if there is no successor then returns null
     * @param node the node to find successor for
     * @return successor node
     */
    Node<E> findSuccessor(Node node) {
        if (node.hasRightSon()) {
            return findSmallestElement(node.right);
        }
        else if (node.isLeftSon()) {
            return node.parent;
        }
        else return null; // if our node is the right node of parent node (and there are no right sons), then there is no successor
    }

    /**
     * Finds smallest element in subtree.
     * If called for leaf node returns that leaf node.
     * @param subtreeRoot
     * @return
     */
    private Node findSmallestElement(Node subtreeRoot) {
        Node tmp = subtreeRoot;
        while (tmp.hasLeftSon()) {
            tmp = tmp.left;
        }
        return tmp;
    }

    /**
     * Returns reference to specyfic tree node that contains value provided
     */
    Node getTreeNodeByValue(E value) {
        return subTreeContains(value, rootNode);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        rootNode = null;
    }

    public int getCurrentModificationSerialNumber() {
        return modificationCounter;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (E element : this ) {
            result.append(element);
            result.append(',');
        }
        return result.toString();
    }
}
