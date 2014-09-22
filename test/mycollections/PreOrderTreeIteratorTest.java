package mycollections;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by SG0892454 on 2014-09-02.
 * Iterates tree in pre-order method:
 * <ol>
 *  <li></>Visit the root.
 *  <li>Traverse the left subtree.
 *  <li>Traverse the right subtree.
 * <ol/>
 */
public class PreOrderTreeIteratorTest {

    @Test
    public void emptyTree() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<Integer>();
        Iterator<Integer> it = tree.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void simpleTree() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<Integer>();
        tree.add(2);
        tree.add(1);
        tree.add(3);
        Iterator<Integer> it = tree.iterator();
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(2), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(1), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(3), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void bugWithIteratorNotWorkingOnOneElementTree() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(10);
        Iterator<Integer> it = tree.iterator();
        assertEquals(Integer.valueOf(10), it.next());
        assertFalse(it.hasNext());
        try {
            it.next();
        } catch (NoSuchElementException e) {return;}
        fail("NoSuchElementException should be thrown, no more elements");
    }



    @Test
    public void complexTree() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<Integer>();
        tree.add(10);
        tree.add(2);
        tree.add(20);
        tree.add(25);
        tree.add(11);

        Iterator<Integer> it = tree.iterator();
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(10), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(2), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(20), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(11), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(25), it.next());
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementExceptionThrownWhenNoMoreElements() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(2);
        tree.add(1);
        Iterator<Integer> it = tree.iterator();
        try {
            it.next();
            it.next();
        } catch (NoSuchElementException e) {
            fail();
        }
        it.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testProperExceptionThrownWhenNoMoreElementsOnEmptyTree() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        Iterator<Integer> it = tree.iterator();
        assert !it.hasNext();
        it.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationExceptionThrownWhenCollectionModifiedWhileIterating() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(1);
        tree.add(2);
        Iterator<Integer> it = tree.iterator();
        it.next();
        tree.add(10); // concurrent modification
        it.next();
    }

    @Test
    public void testEmittingNulls() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(2);
        tree.add(1);
        tree.add(3);
        tree.add(0);
        List<Integer> iterated = new ArrayList<>();
        for (Iterator<Integer> it = new PreOrderTreeIterator<Integer>(tree, true); it.hasNext(); ) {
            iterated.add(it.next());
        }
        assertArrayEquals(new Integer[]{2, 1, 0, null, null, null, 3, null, null}, iterated.toArray());
    }

    @Test
    public void testEmittingNulls2() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();

        tree.add(0);
        tree.add(1);
        tree.add(3);
        tree.add(2);
        /*
                0
                 \
                  1
                   \
                    3
                  /
                2
         */
        List<Integer> iterated = new ArrayList<>();
        for (Iterator<Integer> it = new PreOrderTreeIterator<Integer>(tree, true); it.hasNext(); ) {
            iterated.add(it.next());
        }
        assertArrayEquals(new Integer[]{0, null, 1, null, 3, 2,  null, null, null}, iterated.toArray());
    }

    @Test
    public void remove() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(2);
        tree.add(1);
        tree.add(3);
        tree.add(4);
        Iterator<Integer> it = tree.iterator();
        it.next(); // 2;

        it.next(); // 1;
        it.remove();

        assertTrue(tree.contains(2));
        assertFalse(tree.contains(1)); // removed
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(4));
    }

    @Test (expected = IllegalStateException.class)
    public void onlyOneCallToRemoveAllowedPerOneNextCall() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(2);
        tree.add(1);
        tree.add(3);

        Iterator<Integer> it = tree.iterator();
        it.next();

        it.remove();
        it.remove();
    }

    @Test (expected = IllegalStateException.class)
    public void removeCalledButPreviouslyNextWasNotCalled() {
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(2);
        tree.add(1);
        tree.add(3);

        Iterator<Integer> it = tree.iterator();

        it.remove();
    }

    @Test
    public void callsToNextCallsAfterRemoveWorkCorrectly_RemovedHadBothSons() {
        /*
            Version for removed node had both sons
            we will remove 40 from this tree. Successor of 40 is 42, so 42 will be put in place of 40.
                    80
                   /
                 40
                /  \
              20    44
                   /
                  42
         */
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(80);
        tree.add(40);
        tree.add(20);
        tree.add(44);
        tree.add(42);

        Iterator<Integer> it = tree.iterator();

        // scroll iterator to node 40
        it.next(); // 80
        it.next(); // 40

        // remove 40, by calling iterator
        it.remove();
        /*
            our tree becomes now:
                    80
                   /
                 42
                /  \
              20    44
         */

        // now iterator should still return, in this very order:
        assertEquals(Integer.valueOf(42),it.next());
        assertEquals(Integer.valueOf(20),it.next());
        assertEquals(Integer.valueOf(44),it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void callsToNextCallsAfterRemoveWorkCorrectly_RemovedHadBothSons_WithEmittingNulls() {
        /*
            Version for removed node had both sons
            we will remove 40 from this tree. Successor of 40 is 42, so 42 will be put in place of 40.
                    80
                   /  \
                 40    N
                /  \
              20    44
             / \   /  \
            N  N  42   N
                 /  \
                N    N
         */
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(80);
        tree.add(40);
        tree.add(20);
        tree.add(44);
        tree.add(42);

        Iterator<Integer> it =  new PreOrderTreeIterator<Integer>(tree, true);

        // scroll iterator to node 40
        it.next(); // 80
        it.next(); // 40

        // remove 40, by calling iterator. Before remove stack contained: N, 44, 20
        it.remove();
        /*
            our tree becomes now:
                    80
                   /  \
                 42    N
                /  \
              20    44
             / \   /  \
            N   N  N   N
         */

        // after tree reorganization and stack adjustment, stack should contain: N, 42
        // now iterator should still return, in this very order:
        assertEquals(Integer.valueOf(42),it.next());
        assertEquals(Integer.valueOf(20),it.next());
        assertNull(it.next()); // left N for 20
        assertNull(it.next()); // right N for 20
        assertEquals(Integer.valueOf(44),it.next());
        assertNull(it.next()); // left N for 44
        assertNull(it.next()); // right N for 44
        assertNull(it.next());  // N for 80
        assertFalse(it.hasNext());
    }

    @Test
    public void callsToNextCallsAfterRemoveWorkCorrectly_RemovedHadOnlyRightSon() {
        /*
            we will remove 40 from this tree. As 40 had only one son, we just put its only son (44) in its place
                    80
                   /
                 40
                   \
                    44
                   /
                  42
         */
        BSTTreeSet<Integer> tree = new BSTTreeSet<>();
        tree.add(80);
        tree.add(40);
        tree.add(44);
        tree.add(42);

        Iterator<Integer> it = tree.iterator();

        // scroll iterator to node 40
        it.next(); // 80
        it.next(); // 40

        // remove 40, by calling iterator
        it.remove();
        /*
            our tree becomes now:
                    80
                   /
                 44
                /
              42
         */

        // now iterator should still return, in this very order:
        assertEquals(Integer.valueOf(44),it.next());
        assertEquals(Integer.valueOf(42),it.next());
        assertFalse(it.hasNext());
    }
}
