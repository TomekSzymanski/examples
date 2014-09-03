package mycollections;

/**
 * Created by SG0892454 on 2014-09-02.
 */
public class Node<E> {

    final E value;
    Node left;
    Node right;
    Node parent;

    public E getValue() {
        return value;
    }

    public Node(E value) {
        this.value = value;
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
