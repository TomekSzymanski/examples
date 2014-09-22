package mycollections;

import java.util.*;

/**
 *
 */
class PreOrderTreeIterator<E> implements Iterator {

    private final PreOrderTreeNodeIterator<E> nodeIterator;

    PreOrderTreeIterator(BSTTreeSet tree) {
        nodeIterator = new PreOrderTreeNodeIterator<E>(tree);
    }

    PreOrderTreeIterator(BSTTreeSet tree, boolean emitNullsForMissingSons) {
        nodeIterator = new PreOrderTreeNodeIterator<E>(tree, emitNullsForMissingSons);
    }



    @Override
    public boolean hasNext() {
        return nodeIterator.hasNext();
    }

    @Override
    public Object next() {
        Node node = nodeIterator.next();
        return (node==null)? null : (E)node.getValue();
    }
    @Override
    public void remove() {
        nodeIterator.remove();
    }
}
