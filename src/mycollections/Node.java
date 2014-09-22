package mycollections;

import java.io.Serializable;

/**
 * Interface for a node of any binary tree
 */
public interface Node<E> extends Serializable {

    E getValue();

    Node<E> getOnlySon();

    Node<E> getLeftSon();

    Node<E> getRightSon();

    Node<E> getParent();

    void setRightSon(Node<E> newRightSon);

    void setLeftSon(Node<E> newLeftSon);

    default boolean isLeafNode()  {
        return (!hasRightSon() && !hasLeftSon())? true : false;
    }

    boolean hasLeftSon();

    boolean isLeftSon();

    boolean hasNoParent();

    boolean isRightSon();

    boolean hasRightSon();

    public void unlinkFromParent();

    public void unlinkFromParentAndSons();

    default boolean hasOneSonOnly() {
        return ((hasLeftSon() && !hasRightSon()) || (!hasLeftSon() && hasRightSon()))? true : false;
    }

    default boolean hasBothSons() {
        return (hasRightSon() && hasLeftSon())? true : false;
    }

    boolean isRootNode();
}
