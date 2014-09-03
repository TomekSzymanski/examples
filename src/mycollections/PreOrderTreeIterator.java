package mycollections;

import java.util.*;

/**
 * Created by SG0892454 on 2014-09-02.
 */
class PreOrderTreeIterator<E> implements Iterator {

    private final Stack<Node<E>> stack = new Stack<Node<E>>();
    private final MySimpleBSTTreeSet<E> tree; // needed only to query for modification number

    private final int originalModificationSerialNumber;

    // TODO add thread safety

    public PreOrderTreeIterator(MySimpleBSTTreeSet tree) {
        Node<E> rootNode = tree.getRootNode();
        if (rootNode != null) {
            stack.push(rootNode);
        }
        this.originalModificationSerialNumber = tree.getCurrentModificationSerialNumber();
        this.tree = tree;
    }

    private Node<E> getNextInOrder() {
        Node<E> iterationCurrentNode;
        if (hasNext()) {

            iterationCurrentNode = stack.pop();

            if (iterationCurrentNode.hasRightSon()) {
                stack.push(iterationCurrentNode.right);
            }
            if (iterationCurrentNode.hasLeftSon()) {
                stack.push(iterationCurrentNode.left);
            }
            return iterationCurrentNode;
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
        return (!stack.isEmpty()) ;
    }

    @Override
    public E next() {
        if (tree.getCurrentModificationSerialNumber() != originalModificationSerialNumber) {
            throw new ConcurrentModificationException("Modification of collection not allowed when iterating. Iterator is fail-fast");
        }
        return getNextInOrder().value;
    }
}
