package mycollections;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by SG0892454 on 2014-09-01.
 */
public abstract class CollectionTests  {

    protected Collection<String> c;

    /*
    prepares concrete implementation of Collection for tests.
    For every test creates new (clean) instance of concrete class that implements Collection
    test classes implementing this method must assign new collection instance to the c Collection<String> reference
     */
    @Before
    public abstract void resetCollection();

    @Test
    public void collectionInitiallyEmpty() {
        assertEquals(0, c.size());
    }

    @Test
    public void addOneElement() {
        assert c.size() == 0;
        boolean result = c.add("one");
        assertTrue("if collection was modified add() must return true", result);
        assertEquals(1, c.size());
    }

    @Test
    public void addTwoElements() {
        assert c.size() == 0;
        c.add("one");
        boolean result = c.add("two");
        assertTrue("if collection was modified add() must return true", result);
        assertEquals(2, c.size());
    }

    @Test
    public void addElementThatAlreadyExists() {
        assert c.size() == 0;
        c.add("one");
        boolean result = c.add("one"); // try to add the same again
        assertFalse("element already exists, add() must return false", result);
        assertEquals(1, c.size());
    }

    @Test
    public void contains() {
        assert c.size() == 0;
        c.add("one");
        assertTrue(c.contains("one"));
        assertFalse(c.contains("not_existent"));
    }

    @Test
    public void containsTwoElements() {
        assert c.size() == 0;
        c.add("one");
        c.add("two");
        assertTrue(c.contains("one"));
        assertTrue(c.contains("two"));
        assertFalse(c.contains("not_existent"));
    }

    @Test
    public void removeSimple() {
        c.add("one");
        c.add("two");
        boolean result = c.remove("one");

        assertEquals(1, c.size());
        assertFalse(c.contains("one"));
        assertTrue(c.contains("two"));
        assertTrue(result);
    }

    @Test
    public void removeSimpleFalseReturnedWhenNoModification() {
        c.add("one");
        c.add("two");
        boolean result = c.remove("non_existent");

        assertEquals(2, c.size());
        assertTrue(c.contains("one"));
        assertTrue(c.contains("two"));
        assertFalse(result);
    }


    @Test
    public void remove1() {
        c.add("10");
        c.add("20");
        c.add("30");
        c.add("1");
        c.add("2");
        c.add("3");
        c.add("100");
        c.add("200");

        c.remove("30");
        c.remove("100");
        c.remove("1");

        assertEquals(5, c.size());

        assertTrue(c.contains("10"));
        assertTrue(c.contains("20"));
        assertTrue(c.contains("2"));
        assertTrue(c.contains("3"));
        assertTrue(c.contains("200"));

        assertFalse(c.contains("1"));
        assertFalse(c.contains("30"));
        assertFalse(c.contains("100"));
    }


}

