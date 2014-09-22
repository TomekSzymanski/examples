package mycollections;

/**
 * Created by SG0892454 on 2014-09-19.
 */
public interface WeightedNode<E> extends Node<E> {

    int getBalanceFactor();

    void setBalanceFactor(int balanceFactor);
}
