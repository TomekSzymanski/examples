package mycollections;

import java.io.*;
import java.util.*;

/**
 * Propretary implementation for simple BST tree (no balancing). Currently supports nearly all {@code Set} interface methods.<BR><BR>
 * <a href="http://pl.wikipedia.org/wiki/Binarne_drzewo_poszukiwa%C5%84">http://pl.wikipedia.org/wiki/Binarne_drzewo_poszukiwa%C5%84</a>
 * <BR>
 * <a href="http://en.wikipedia.org/wiki/Binary_search_tree">http://en.wikipedia.org/wiki/Binary_search_tree</a>
 *
 */
public class BSTTreeSet<E> implements Set<E>, Serializable {

    private static final long serialVersionUID = -480104583835245951L;

    /**
     * reference to the root node of the tree. If null then tree is empty.
     */
    protected Node<E> rootNode;

    /**
     * This counter will be incremented with every operation modifying tree, like add or remove.
     * It is needed for Iterator to check if no changes were done since creation of iterator (since calling {@code }iterator()} method on the tree).
     * If iterator detects changes then it will trow ConcurrentModificationException (fail-fast iterator)
     */
    private transient int modificationCounter;

    /**
     * Comparator to be used in compare operations (needed with many tree operations). If not provided in constructor then we will try to use natural ordering of elements.
     */
    private Comparator<? super E> comparator;

    private enum InsertionDirection {LEFT, RIGHT}; // to be used in deserialization method

    /**
     * Constructs new empty set, sorted according to the natural ordering of its elements.
     */
    public BSTTreeSet() {
        this(null);
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified comparator.
     * @param comparator the comparator that will be used to order this set. If null, the natural ordering of the elements will be used.
     */
    public BSTTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns reference to the root node of the tree
     * @return root node of the tree
     */
    Node<E> getRootNode() {
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
    private int subTreeSize(Node<E> subTreeRoot) {
        if (subTreeRoot==null) return 0;
        return 1 + subTreeSize(subTreeRoot.getLeftSon()) + subTreeSize(subTreeRoot.getRightSon());
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
    private Node<E> subTreeContains(E value, Node<E> currentNode) {
        if (currentNode == null) return null;

        if (compare(value, currentNode.getValue()) > 0 ) {
            return subTreeContains(value, currentNode.getRightSon());
        } else if (compare(value, currentNode.getValue()) < 0 ) {
            return subTreeContains(value, currentNode.getLeftSon());
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
        Object[] tmpArray = new Object[size()];
        int i = 0;
        for(Iterator<E> iter = iterator(); iter.hasNext(); i++) {
            tmpArray[i] = iter.next(); // this is shallow copy only. If we require deep copy we would have to let E implement Cloneable
            // Question: how to do deep copy? casting to Cloneable will not help, cannot call copy method anyway. System.arraycopy or Arrays.copyOf do shallow copy only.
            // I does not want to enforce on E to implement copy constructor, custom clone method, or copying factory
        }
        return tmpArray;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length >= size()) {
            Iterator<E> it = iterator();
            int i = 0;
            for(; it.hasNext(); i++) {
                a[i] = (T)it.next();
            }
            if (a.length > size()) {
                a[size()] = null; // add null after all elements copied
            }
            return Arrays.copyOf(a, a.length);
        } else {
            T[] tmpArray = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
            Iterator<E> it = iterator();
            int i = 0;
            for(; it.hasNext(); i++) {
                tmpArray[i] = (T)it.next();
            }
            return Arrays.copyOf(tmpArray, tmpArray.length);


        }
    }

    /**
     * Adds element to the tree. Null values currently not supported.
     * @param e element to be added
     * @return {@code true} if adding changed the tree structure (element was added). {@code false} if add did not change the tree (element was not added, element already existed.).
     */
    @Override
    public boolean add(E e) {
        Node<E> nodeToAdd = NodeFactory.newNode(e);
        return addToSubtree(nodeToAdd , rootNode);
    }

    boolean add(Node<E> e) {
        return addToSubtree(e , rootNode);
    }

    final boolean addToSubtree(Node<E> elementToAdd, Node<E> currentRoot) {
        // check if casting to Comparable is possible for E, or if comparator exists
        compare(elementToAdd.getValue(), elementToAdd.getValue());

        //TODO add support for storing null element
        if (currentRoot == null) { // means the whole three is empty.
            // Later method calls itself recursively but makes sure it will not be called for null subtree.
            // May be called for null only for root element
            rootNode = elementToAdd;
            modificationCounter++;
            return true;
        }
        if (compare(elementToAdd.getValue(), currentRoot.getValue()) > 0) {
            if (currentRoot.hasRightSon()) {
                return addToSubtree(elementToAdd, currentRoot.getRightSon());
            } else {
                currentRoot.setRightSon(elementToAdd);
                modificationCounter++;
                return true;
            }
        }
        if (compare(elementToAdd.getValue(), currentRoot.getValue()) < 0) {
            if (currentRoot.hasLeftSon()) {
                return addToSubtree(elementToAdd, currentRoot.getLeftSon());
            } else {
                currentRoot.setLeftSon(elementToAdd);
                modificationCounter++;
                return true;
            }
        }
        if (compare(elementToAdd.getValue(), currentRoot.getValue()) == 0) { // element exists, structure does not need to change.
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
        Node<E> nodeToRemove = getTreeNodeByValue(valueToRemove);
        return removeItnernal(nodeToRemove);
    }

    /**
     * Removes provided element from the tree.
     * @param o element to be removed
     * @return {@code true} if removing changed the tree structure (element was removed). {@code false} if removal did not change the tree (element was not present in the tree).
     */
    boolean removeItnernal(Node<E> nodeToRemove) {

        if (nodeToRemove == null) { // value not found in set, do not do anything //TODO bound to reference-based imlementation, change
            return false;
        }

        // if leaf node very simple: remove this node without any more transformations in the tree
        if (nodeToRemove.isLeafNode()) {
            if (nodeToRemove.isRootNode()) {
                rootNode = null;
            }
            nodeToRemove.unlinkFromParent();
            modificationCounter++;
            return true;
        }

        if (nodeToRemove.hasOneSonOnly() ) { // remove element and connect its parent with removed element descendant
            Node<E> descendantOfTheNodeToBeRemoved = nodeToRemove.getOnlySon();
            if (nodeToRemove.isLeftSon()) {
                nodeToRemove.getParent().setLeftSon(descendantOfTheNodeToBeRemoved);
            } else if (nodeToRemove.isRightSon()) {
                nodeToRemove.getParent().setRightSon(descendantOfTheNodeToBeRemoved);
            } else if (nodeToRemove.hasNoParent()) {
                rootNode = descendantOfTheNodeToBeRemoved;
            }
            nodeToRemove.unlinkFromParentAndSons();
            modificationCounter++;
            return true;
        }

        if (nodeToRemove.hasBothSons()) { // put the successor node in place of the removed node
            Node<E> successor = findSuccessor(nodeToRemove);

            if (successor.isLeftSon()) {
                // it must mean this successor may have only right son/subtree (otherwise the successor would be in its left subtree)
                assert !successor.hasLeftSon();
                successor.getParent().setLeftSon(successor.getRightSon());

                if (nodeToRemove.isLeftSon()) {
                    nodeToRemove.getParent().setLeftSon(successor);
                } else if (nodeToRemove.isRightSon()) {
                    nodeToRemove.getParent().setRightSon(successor);
                } else if (nodeToRemove == rootNode) {
                    rootNode = successor;
                }
               successor.setRightSon(nodeToRemove.getRightSon());
            } else if (successor.isRightSon()) {
                assert !successor.hasLeftSon();
                assert successor.getParent() == nodeToRemove;

                if (nodeToRemove == rootNode) {
                    rootNode = successor;
                } else {
                    if (nodeToRemove.isLeftSon()){
                        nodeToRemove.getParent().setLeftSon(successor);
                    } else { // right son
                        nodeToRemove.getParent().setRightSon(successor);
                    }
                }
             }

            assert !successor.hasLeftSon();
            successor.setLeftSon(nodeToRemove.getLeftSon());

            nodeToRemove.unlinkFromParentAndSons();

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
    Node<E> findSuccessor(Node<E> node) {
        if (node.hasRightSon()) {
            return findSmallestElement(node.getRightSon());
        }
        else if (node.isLeftSon()) {
            return node.getParent();
        }
        else return null; // if our node is the right node of parent node (and there are no right sons), then there is no successor
    }

    /**
     * Finds smallest element in subtree.
     * If called for leaf node returns that leaf node.
     * @param subtreeRoot
     * @return
     */
    private Node<E> findSmallestElement(Node<E> subtreeRoot) {
        Node<E> tmp = subtreeRoot;
        while (tmp.hasLeftSon()) {
            tmp = tmp.getLeftSon();
        }
        return tmp;
    }

    /**
     * Returns reference to specyfic tree node that contains value provided
     */
    Node<E> getTreeNodeByValue(E value) {
        return subTreeContains(value, rootNode);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean collectionChanged = false;
        for(E element : c) {
            if (add(element)) {
                collectionChanged = true;
            };
        }
        return collectionChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeOrRetainAll(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeOrRetainAll(c, false);
    }

    /*
        If remove is set to true then removes all elements that exist in the provided Collection c.
        If remove is set to false then removes all elements that do not exist in the provided Collection c
        (retains only elements that do exists in provided Collection c).
     */
    private boolean removeOrRetainAll(Collection<?> c, boolean remove) {
        boolean collectionChanged = false;
        Objects.requireNonNull(c);

        if (remove) { // remove mode
            for (Iterator<E> it = iterator(); it.hasNext(); ) {
                if (c.contains(it.next())) {
                    it.remove();
                    collectionChanged = true;
                }
            }
        } else { // retain mode
            for (Iterator<E> it = iterator(); it.hasNext(); ) {
                Object next = it.next();
                if (!c.contains(next)) {
                    it.remove();
                    collectionChanged = true;
                }
            }
        }
        return collectionChanged;
    }

    @Override
    public void clear() {
        rootNode = null; // TODO should not we also remove all references between nodes? How is it implemented in other collections?
    }

    int getCurrentModificationSerialNumber() {
        return modificationCounter;
    }

    /**
     * requires trees to be equal not only in terms of content, but also in terms of structure
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BSTTreeSet that = (BSTTreeSet) o;

        if (comparator != null ? !comparator.equals(that.comparator) : that.comparator != null) return false;
        if (rootNode != null ? !rootNode.equals(that.rootNode) : that.rootNode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rootNode != null ? rootNode.hashCode() : 0;
        result = 31 * result + (comparator != null ? comparator.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(size()*10); // we do not know how many characters will be needed to represent one value. Assuming 10
        forEach(element
                -> {result.append(element);
                    result.append(',');
        });
        return result.toString();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        // serialize comparator first and then all elements, with nulls marking lack of more child elements for every node. See PreOrderTreeIterator(... emitNullsForMissingSons=true)
        oos.writeObject(comparator);
        E nextElement;
        for (Iterator<E> iterator = new PreOrderTreeIterator(this, true); iterator.hasNext(); ) {
            nextElement = iterator.next();
            oos.writeObject(nextElement);
        }
    }

    /**
      Non-recursive version of deserializing tree, which was serialized in pre-order method (including nulls for no more left or right son (guards)).
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        comparator = (Comparator<? super E>)ois.readObject();
        E element = null;
        Node<E> currentNode = rootNode; // start with rootNode
        InsertionDirection direction = InsertionDirection.LEFT; // be default we will be inserting elements as left son, as we serialized with pre-order method (parent, left before right)
        try {
            while(true) {
                element = (E) ois.readObject();

                // we are inserting left sons and we met null, which means no more left sons. Turn right then.
                if (direction.equals(InsertionDirection.LEFT) && element == null) {
                    direction = InsertionDirection.RIGHT;
                    continue;
                }

                // we were going to insert right son, but we met null, which means no more right sons. Then let's move up on level to parent (or more, till we will be able to insert right son).
                if (direction.equals(InsertionDirection.RIGHT) && element == null) {
                    currentNode = currentNode.getParent();
                    // if current node already has right son (so its left son was already considered (in pre-order iteration method),
                    // then surely there is nothing to do here and we have to move to the parent, and continue this operation till we get to the node which does not have right son: it will be our current node, when we will insert next element as a right son
                    for(; currentNode.hasRightSon() && currentNode != rootNode; currentNode = currentNode.getParent());
                    continue;
                }

                // condition only for deserializing the root node
                if (rootNode == null) {
                    rootNode = NodeFactory.newNode(element);
                    currentNode = rootNode;
                    continue;
                }

                // link new read node with current Node
                if (direction.equals(InsertionDirection.RIGHT)) {
                    currentNode.setRightSon(NodeFactory.newNode(element));
                    currentNode = currentNode.getRightSon();
                    direction = InsertionDirection.LEFT;
                } else { // turn left
                    assert !currentNode.hasLeftSon();
                    currentNode.setLeftSon(NodeFactory.newNode(element));
                    currentNode = currentNode.getLeftSon();
                }
            }
        } catch (OptionalDataException e) { // end of input stream
            if (e.eof && e.length == 0) {
                return;
            } else {
                e.printStackTrace();
            }
        }

    }

    /**
     recursive version of deserializing tree, which was serialized in pre-order method (including nulls for no more left or right son (guards)).
     */

    private Node<E> readObjectRecursive(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        E element = null;
        try {
            while(true) {
                element = (E) ois.readObject();
                if (element == null) {
                    break;
                }

                if (rootNode == null) {
                    rootNode =  NodeFactory.newNode(element);
                    rootNode.setLeftSon(readObjectRecursive(ois));
                    rootNode.setRightSon(readObjectRecursive(ois));
                } else {
                    Node<E> tmpElement = NodeFactory.newNode(element);
                    tmpElement.setLeftSon(readObjectRecursive(ois));
                    tmpElement.setRightSon(readObjectRecursive(ois));
                    return tmpElement;
                }

            }
        } catch (OptionalDataException e) { // end of input stream
            if (e.eof && e.length == 0) {}
            else {
                e.printStackTrace();
            }
        }
    return null;
    }

    /*
        performs sanity test on tree after every test: check for every node, check if father-son relations are symetric (if father F has son S, then son S must have father F).
    */
    boolean areNodeRelationsFine(Node<E> node) {
        if (node == null) { //TODO dependency on reference-based implementation
            return true;
        }
        if (node.isLeafNode()) return true;
        if (node.hasBothSons()) {
            if (!node.getRightSon().getParent().equals(node)) return false;
            if (!isRightSonGreater(node)) return false;

            if (!node.getLeftSon().getParent().equals(node)) return false;
            if (!isLeftSonSmaller(node)) return false;

            return (areNodeRelationsFine(node.getLeftSon()) && areNodeRelationsFine(node.getLeftSon()));
        } else if (node.hasRightSon()) {
            if (!node.getRightSon().getParent().equals(node)) return false;
            return areNodeRelationsFine(node.getRightSon());
        } else if (node.hasLeftSon()) {
            if (!node.getLeftSon().getParent().equals(node)) return false;
            return areNodeRelationsFine(node.getLeftSon());
        }
        return false;
    }

    boolean isRightSonGreater(Node<E> node) {
        // right son must be greater than perent
        return (compare(node.getRightSon().getValue(), node.getValue()) > 0)? true : false;
    }

    boolean isLeftSonSmaller(Node<E> node) {
        // left son must be smaller than perent
        return (compare(node.getLeftSon().getValue(), node.getValue()) < 0)? true : false;
    }
}
