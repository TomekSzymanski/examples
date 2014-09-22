package mycollections;

/**
 * Utility structure representing Node of a binary tree
 * @param <E>
 */
class ReferenceBasedNode<E> implements Node<E> {

    private static final long serialVersionUID = 7522960547854287545L;

    private final E value;
    private ReferenceBasedNode<E> left;
    private ReferenceBasedNode right;
    private ReferenceBasedNode parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferenceBasedNode node = (ReferenceBasedNode) o;

        if (getLeftSon() != null ? !getLeftSon().equals(node.getLeftSon()) : node.getLeftSon() != null) return false;
        if (getRightSon() != null ? !getRightSon().equals(node.getRightSon()) : node.getRightSon() != null) return false;
        if (value != null ? !value.equals(node.value) : node.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (getLeftSon() != null ? getLeftSon().hashCode() : 0);
        result = 31 * result + (getRightSon() != null ? getRightSon().hashCode() : 0);
        return result;
    }

    public ReferenceBasedNode<E> getParent() {
        return parent;
    }

    /** sets to provided node without checking if already has right son (overriding it) */
    @Override
    public void setRightSon(Node<E> newRightSon) {
        right = (ReferenceBasedNode<E>)newRightSon;
        if (newRightSon!=null) { // this setters may be called also for null node (method is called generally, for leftsubtree, in particular it may be null)
            ((ReferenceBasedNode<E>)newRightSon).parent = this;
        }
    }

    @Override
    public boolean hasLeftSon() { return (getLeftSon() !=null)? true : false;
    }

    public Node<E> getLeftSon() {
        return left;
    }

    public ReferenceBasedNode getRightSon() {
        return right;
    }

    @Override
    public void setLeftSon(Node<E> newLeftSon) {
        left = (ReferenceBasedNode<E>)newLeftSon;
        if (newLeftSon!=null) {
            ((ReferenceBasedNode<E>)newLeftSon).parent = this;
        }

    }

    /**
     * Creates node with value provided, this node has all parent, left and right son unset (set to null).
     * @param value
     */
    public ReferenceBasedNode(E value) {
        this.value = value;
    }

    @Override
    public E getValue() {
        return value;
    }

    @Override
    public boolean isLeftSon() {
        if (hasNoParent()) return false;
        return (parent.getLeftSon() == this )? true : false;
    }

    @Override
    public boolean hasNoParent() {
        return (parent == null )? true : false;
    }

    @Override
    public boolean isRightSon() {
        if (hasNoParent()) return false;
        return (parent.getRightSon() == this )? true : false;
    }


    @Override
    public boolean hasRightSon() { return (getRightSon() !=null)? true : false;
    }


    @Override
    public Node<E> getOnlySon() {
        if (hasOneSonOnly()) {
            if (hasRightSon()) {
                return getRightSon();
            } else if (hasLeftSon()) {
                return getLeftSon();
            }
        }
        throw new IllegalStateException("Cannot call getOnlySon on node which has both sons");
    }

    @Override
    public boolean isRootNode() {
        return (parent==null)? true : false;
    }

    public void unlinkFromParentAndSons() {
        if (hasRightSon() && right.parent == this) {right.parent = null;}
        right = null;

        if (hasLeftSon() && left.parent == this) {left.parent = null;}
        left = null;
        if (isLeftSon()) {
            parent.left = null;
        } else if (isRightSon()) {
            parent.right = null;
        }
        parent = null;
    }

    public void unlinkFromParent() {
        if (isRightSon()) {
            parent.right = null;
        } else if (isLeftSon()) {
            parent.left = null;
        }
        parent = null;
    }


    @Override
    public String toString() {
        BSTTreeSet<E> tmpTree = new BSTTreeSet();
        tmpTree.add(this);
        return tmpTree.toString();
    }
}
