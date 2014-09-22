package mycollections;

/**
 * Created by SG0892454 on 2014-09-12.
 */
class BalanceFactoredReferenceBasedNode<E> extends ReferenceBasedNode<E> implements WeightedNode<E> {

    private int balanceFactor;

    @Override
    public int getBalanceFactor() {
        return balanceFactor;
    }

    @Override
    public void setBalanceFactor(int balanceFactor) {
        assert balanceFactor >= -2 && balanceFactor <= 2;
           // throw new IllegalArgumentException("Balance factor in AVL tree cannot be other than -2, -1, 0, 1, 2. Tried to set balance to " + balanceFactor);
        this.balanceFactor = balanceFactor;
    }

    BalanceFactoredReferenceBasedNode(E value) {
        super(value);
    }
}
