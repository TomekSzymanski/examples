package mycollections;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by SG0892454 on 2014-09-02.
 */
public class PreOrderTreeIteratorTest {


    @Test
    public void simpleTree() {
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
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
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
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
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
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
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
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
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
        Iterator<Integer> it = tree.iterator();
        assert !it.hasNext();
        it.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationExceptionThrownWhenCollectionModifiedWhileIterating() {
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
        tree.add(1);
        tree.add(2);
        Iterator<Integer> it = tree.iterator();
        it.next();
        tree.add(10); // concurrent modification
        it.next();
    }

    @Test
    public void emptyTree() {
        MySimpleBSTTreeSet<Integer> tree = new MySimpleBSTTreeSet<Integer>();
        Iterator<Integer> it = tree.iterator();
        assertFalse(it.hasNext());
    }

}
