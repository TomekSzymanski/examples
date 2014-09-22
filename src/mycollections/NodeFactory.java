package mycollections;

/**
 *
 */
class NodeFactory<E> {

    public static <E> Node<E> newNode(E e) {
        return new ReferenceBasedNode(e);
    }

    public static <E> WeightedNode<E> newWeightedNode(E e) {
        return new BalanceFactoredReferenceBasedNode(e);
    }


}
