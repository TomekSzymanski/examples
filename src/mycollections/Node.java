package mycollections;

/**
 * Utility structure representing Node of a binary tree
 * @param <E>
 */
class Node<E> {

    final E value;
    Node left;
    Node right;
    Node parent;

    /**
     * Creates node with value provided, this node has all parent, left and right son unset (set to null).
     * @param value
     */
    public Node(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    public boolean isLeafNode() {
        return (!hasRightSon() && !hasLeftSon())? true : false;
    }

    public boolean hasLeftSon() { return (left!=null)? true : false;
    }

    public boolean isLeftSon() {
        if (!hasParent()) return false;
        return (parent.left == this )? true : false;
    }

    public boolean hasParent() {
        return (parent != null )? true : false;
    }

    public boolean isRightSon() {
        if (!hasParent()) return false;
        return (parent.right == this )? true : false;
    }


    public boolean hasRightSon() { return (right!=null)? true : false;
    }

    public boolean hasOneSonOnly() {
        return ((left==null && right != null) || (left!=null && right == null))? true : false;
    }

    public boolean hasBothSons() {
        return (hasRightSon() && hasLeftSon())? true : false;
    }

    public Node<E> getOnlySon() {
        if (hasOneSonOnly()) {
            if (hasRightSon()) {
                return right;
            } else if (hasLeftSon()) {
                return left;
            }
        }
        throw new IllegalStateException("Cannot call getOnlySon on node which has both sons");
    }

}
