package mycollections;

import java.util.*;

/**
 * Created by SG0892454 on 2014-09-14.
 */
public class PreOrderTreeNodeIterator<E> implements Iterator {
    private final Deque<Node<E>> stack = new LinkedList();
    private final BSTTreeSet<E> tree; // needed only to query for modification number
    private boolean emitNullsForMissingSons;

    private E prevNodeReturnedValue; // need to remember where we are to support delete operation
    private Node<E> successorOfPrevNode; // need to remember it, in case remove() is called and we have to adjust the stack after remove
    private boolean prevNodeHadBothSons; // need to remember it, in case remove() is called and we have to adjust the stack after remove

    private boolean callToRemoveLegal; // need to remember if remove was already called per last next

    private int originalModificationSerialNumber;

    // TODO add thread safety

    PreOrderTreeNodeIterator(BSTTreeSet tree) {
        Node<E> rootNode = tree.getRootNode();
        if (rootNode != null) {
            stack.push(rootNode);
        }
        this.originalModificationSerialNumber = tree.getCurrentModificationSerialNumber();
        this.tree = tree;
    }

    /**
     * this version will emit nulls for every missing sons (for leaf nodes it will emit the leaf node and two nulls). Needed for serialization.
     * @param tree
     * @param emitNullsForMissingSons: when set to true will ..
     */
    PreOrderTreeNodeIterator(BSTTreeSet tree, boolean emitNullsForMissingSons) {
        this(tree);
        this.emitNullsForMissingSons = emitNullsForMissingSons;
    }

    private Node<E> getNextPreOrder() {
        Node<E> iterationCurrentNode;
        if (hasNext()) {

            iterationCurrentNode = stack.pop();

            if (iterationCurrentNode == null) {
                return iterationCurrentNode;
            }

            if (iterationCurrentNode.hasRightSon()) {
                stack.push(iterationCurrentNode.getRightSon());
            } else if (emitNullsForMissingSons) {
                stack.push(null);
            }
            if (iterationCurrentNode.hasLeftSon()) {
                stack.push(iterationCurrentNode.getLeftSon());
            } else if (emitNullsForMissingSons) {
                stack.push(null);
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
    public Node<E> next() {
        if (tree.getCurrentModificationSerialNumber() != originalModificationSerialNumber) {
            throw new ConcurrentModificationException("Modification of collection not allowed when iterating. Iterator is fail-fast");
        }
        callToRemoveLegal = true;
        Node<E> next = getNextPreOrder();
        if (!emitNullsForMissingSons || (emitNullsForMissingSons && (next != null))) { // if in mode of emitting nulls, then we cannot check if null contains sons
            prevNodeHadBothSons = next.hasBothSons();

            if (prevNodeHadBothSons) {
                successorOfPrevNode = tree.findSuccessor(next); /* When this node has both sons, and then remove() will be called, we will have to reorganize the tree, pushing onto the stack the successor or removed note.
                                                                So unfortunatelly, we have to have this penalty of searching for successor for every next call for any node that has two sons. But complexity of finding successor is linear */
            }
        }
        prevNodeReturnedValue = (next == null) ? null : next.getValue();  // TODO impl dependent on references, remove
        return next;
    }

    /*  Please note, in the specification of Collection interface, for remove() method that:
        The behavior of an iterator is unspecified if the underlying collection is modified while the iteration is in progress in any way other than by calling this method.

        However we will take care of our tree being modified and its structure changed. You have to care so that iterator after remove does not skip or the same nodes two times. It is done in the adjustStackAfterRemove().

        So next() calls after remove are legal and guarantted to work correctly.
     */
    @Override
    public void remove() {
        if (!callToRemoveLegal) {
            throw new IllegalStateException("Cannot call remove() more than once per one next() call.");
        }
        callToRemoveLegal = false;
        originalModificationSerialNumber = tree.getCurrentModificationSerialNumber(); // remember serial number before remove
        if (tree.remove(prevNodeReturnedValue)) { // we cannot store reference to pointer and call removeInternal as it would bound to reference implementation. Also as we are doing remove we would have to also cut references in prevNodeReturnedValue (outside of remove function)
            originalModificationSerialNumber++; // increment our reference modification number by one, with next call to next() it will be compared with current tree modificatioin number. Must be the same (no other changes in the meantime, like from other threads)
            adjustStackAfterRemove();
        }
        prevNodeReturnedValue = null;
    }

    /**
     * after call to remove, if removed node had two sons, tree gets reorganized. For examples, for this tree:
     *          80
               /
             40
           /  \
         20    44
              /
            42

     when we remove 40 from the tree, it will get reorganized into:
            80
           /
          42
         /  \
        20   44
     After removing 40, on our stact we had (looking from the bottom of the stack): 20, 44
     So if we continue with the stack we had, we would skip 42 (it is not longer son to 44).

     So we have to pop two elements from the stack (old sons of removed node), and push the node which replaced removed node in the tree.
     And this node is the successor of the removed node. Successor, after tree reorganization after delete, will have both sons of removed son attached, so iteration will include them properly
     *
     */
    private void adjustStackAfterRemove() {
        if (prevNodeHadBothSons) {
            stack.pop();
            stack.pop();
            stack.push(successorOfPrevNode);
        }
    }
}
