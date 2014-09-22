package mycollections;

/**
 * <a href="http://en.wikipedia.org/wiki/AVL_tree">Wiki article on AVL tree</a>
 *
 */
class MyAVLTreeSet<E> extends BSTTreeSet<E> {

    @Override
    public boolean add(E e) {
        WeightedNode<E> nodeToAdd = NodeFactory.newWeightedNode(e);
        return addToSubtree(nodeToAdd, getRootNode());
    }

    void rotateRight(Node<E> root) {
        Node<E> oldLeftSonRightSubtree = root.getLeftSon().getRightSon();
        Node<E> oldParentOfRoot = root.getParent();

        root.getLeftSon().setRightSon(root);

        if (oldParentOfRoot!=null && root.hasLeftSon()) {
            oldParentOfRoot.setLeftSon(root.getLeftSon());
        }


        if (getRootNode() == root) {
            rootNode = root.getLeftSon();
        }

        root.setLeftSon(oldLeftSonRightSubtree);
    }

    void rotateLeft(ReferenceBasedNode root) {
        Node<E> oldRightSonLeftSubtree = root.getRightSon().getLeftSon();
        Node<E> oldParentOfRoot = root.getParent();

        root.getRightSon().setLeftSon(root);

        if (oldParentOfRoot!= null && root.hasRightSon()) {
            oldParentOfRoot.setRightSon(root.getRightSon());
        }

        if (rootNode == root) {
            rootNode  = root.getRightSon();
        }

        root.setRightSon(oldRightSonLeftSubtree);
    }

}
