package mycollections;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by SG0892454 on 2014-09-13.
 */
public class AVLTreeSetTests  extends CollectionOrderGuaranteedTests {

    @Override
    protected <E> Collection<E> createEmptyCollection() {
        return new MyAVLTreeSet<>();
    }

    @Test
    public void rotateRightTest3ElementsRooted() {
        MyAVLTreeSet<Integer> tree = new MyAVLTreeSet<>();
        ReferenceBasedNode<Integer> n = new ReferenceBasedNode(5);
        tree.add(n);
        tree.add(2);
        tree.add(1);

        tree.rotateRight(n);

        Integer[] expecteds = new Integer[] {2, 1, 5};
        assertArrayEquals(expecteds, tree.toArray());

        assertEquals(Integer.valueOf(2), tree.getRootNode().getValue());
    }

    @Test
    public void rotateRightTest4ElementsNotRoot() {
        MyAVLTreeSet<Integer> tree = new MyAVLTreeSet<>();
        tree.add(7);
        ReferenceBasedNode<Integer> n = new ReferenceBasedNode(5);
        tree.add(n);
        tree.add(2);
        tree.add(1);

        tree.rotateRight(n);

        Integer[] expecteds = new Integer[] {7, 2, 1, 5};
        assertArrayEquals(expecteds, tree.toArray());
    }


    @Test
    public void rotateLeftTest3ElementsRooted() {
        MyAVLTreeSet<Integer> tree = new MyAVLTreeSet<>();
        ReferenceBasedNode<Integer> n = new ReferenceBasedNode(1);
        tree.add(n);
        tree.add(2);
        tree.add(3);

        tree.rotateLeft(n);

        Integer[] expecteds = new Integer[] {2, 1, 3};
        assertArrayEquals(expecteds, tree.toArray());

        assertEquals(Integer.valueOf(2), tree.getRootNode().getValue());
    }

    @Test
    public void rotateLeftTest4ElementsNotRoot() {
        MyAVLTreeSet<Integer> tree = new MyAVLTreeSet<Integer>();
        tree.add(1);
        ReferenceBasedNode<Integer> n = new ReferenceBasedNode(2);
        tree.add(n);
        tree.add(3);
        tree.add(4);

        tree.rotateLeft(n);

        Integer[] expecteds = new Integer[] {1, 3, 2, 4};
        assertArrayEquals(expecteds, tree.toArray());
    }


}
