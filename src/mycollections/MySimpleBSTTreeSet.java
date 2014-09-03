package mycollections;

import java.util.*;

/**
 * Created by SG0892454 on 2014-09-01.
 * propretary implementation for simple BST tree (no balancing)
 * http://pl.wikipedia.org/wiki/Binarne_drzewo_poszukiwa%C5%84
 *
 */
public class MySimpleBSTTreeSet<E> implements Set<E>{

    /**
     * Constructs new empty set, sorted according to the natural ordering of its elements.
     */
    public MySimpleBSTTreeSet() {
        comparator = null;
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified comparator.
     * @param comparator
     */
    public MySimpleBSTTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }


    private Node rootNode;
    private int modificationSerialNumber;

    private final Comparator<? super E> comparator;

    public Node getRootNode() {
        return rootNode;
    }

    @Override
    public int size() {
        return subTreeSize(rootNode);
    }

    private int subTreeSize(Node subTreeRoot) {
        if (subTreeRoot==null) return 0;
        return 1 + subTreeSize(subTreeRoot.left) + subTreeSize(subTreeRoot.right);
    }

    @Override
    public boolean isEmpty() {
        return (rootNode == null)? true: false;
    }

    @Override
    public boolean contains(Object e) {
        return (subTreeContains((E)e, rootNode) != null)? true : false;
    }

    /*
    returns node reference if found. If not found returns null
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

    @Override
    public boolean add(E e) {
        return addToSubtree(e, rootNode);
    }

    private boolean addToSubtree(E elementToAdd, Node<E> currentRoot) {
        // check if Comparable or comparator exists
        compare(elementToAdd, elementToAdd);

        //TODO add support for storing null element
        if (currentRoot == null) { // means the whole three is empty.
            // Later method calls itself recursively but makes sure it will not be called for null subtree.
            // May be called for null only for root element
            rootNode = new Node(elementToAdd);
            modificationSerialNumber++;
            return true;
        }
        if (compare(elementToAdd, currentRoot.value) > 0) {
            if (currentRoot.hasRightSon()) {
                return addToSubtree(elementToAdd, currentRoot.right);
            } else {
                currentRoot.right = new Node(elementToAdd);
                currentRoot.right.parent = currentRoot;
                modificationSerialNumber++;
                return true;
            }
        }
        if (compare(elementToAdd, currentRoot.value) < 0) {
            if (currentRoot.hasLeftSon()) {
                return addToSubtree(elementToAdd, currentRoot.left);
            } else {
                currentRoot.left = new Node(elementToAdd);
                currentRoot.left.parent = currentRoot;
                modificationSerialNumber++;
                return true;
            }
        }
        if (compare(elementToAdd, currentRoot.value) == 0) { // element exists, structure does not need to change.
            return false;
        }
        return false;
    }

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
            modificationSerialNumber++;
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
            modificationSerialNumber++;
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
            modificationSerialNumber++;
            return true;
        }
        return false;
    }

    /*
    finds successor for provided element.
    Successor is the next element (following the provided element) when traversing tree in the in-order method
    if there is no successor then returns null
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

    /*
    finds smallest element for subtree.
    If called for leaf node returns that leaf node
     */
    private Node findSmallestElement(Node node) {
        Node tmp = node;
        while (tmp.hasLeftSon()) {
            tmp = tmp.left;
        }
        return tmp;
    }

    /*
    returns reference to specyfic tree node that contains value provided
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
        return modificationSerialNumber;
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
