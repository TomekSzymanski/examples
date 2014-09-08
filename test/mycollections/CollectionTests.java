package mycollections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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

    @Test
    public void toArray() {
        Collection<Integer> c = new MySimpleBSTTreeSet<Integer>();
        c.add(10);
        c.add(20);
        c.add(30);
        c.add(1);
        c.add(2);
        c.add(3);
        c.add(100);
        c.add(200);

        Object[] array = c.toArray();

        Object[] expecteds = new Object[] {10, 1, 2, 3, 20, 30, 100, 200};

        assertTrue(Arrays.binarySearch(array, 20) >= 0);
        assertTrue(Arrays.binarySearch(array, 100) >= 0);
        assertTrue(Arrays.binarySearch(array, 200) >= 0);

        assertArrayEquals(expecteds, array);

    }

    @Test
    public void toArrayArrayIsNotReferencedByCollection() {

        class SimplestMutableClass implements Comparable {
            int value;
            boolean modifiedBit = false;

            SimplestMutableClass(int value) {
                this.value = value;
            }

            @Override
            public int compareTo(Object o) {
                return value - ((SimplestMutableClass)o).value;
            }
        };

        Collection<SimplestMutableClass> c = new MySimpleBSTTreeSet<SimplestMutableClass>();
        // Collection<SimplestMutableClass> c = new HashSet<SimplestMutableClass>();

        SimplestMutableClass one = new SimplestMutableClass(1);
        SimplestMutableClass two = new SimplestMutableClass(2);
        SimplestMutableClass three = new SimplestMutableClass(3);
        c.add(one);
        c.add(two);
        c.add(three);

        Object[] array = c.toArray();

        assertTrue(Arrays.binarySearch(array, two) >= 0);

        two.modifiedBit = true; // this change should not be reflected in the copied array contents

        // ??? assertFalse(((SimplestMutableClass)array[Arrays.binarySearch(array, two)]).modifiedBit);
        // it does not work for HashSet either
    }

    /**
     * this is test version for collection not guaranteeing order, like HashMap
     */
    @Test @Ignore // not necessary to test HashMap..
    public void toArrayParametrizedArrayProvidedHasTheSameSizeAsTheCollectionNoOrder() {
        Collection<Integer> c = new HashSet<Integer>();
        c.add(20);
        c.add(10);
        c.add(30);
        c.add(40);

        int originalCollectionSize = c.size();

        Integer[] someIntegerArray = new Integer[originalCollectionSize];

        Integer[] expecteds = {20, 10, 30, 40}; // order not important

        Integer[] resultArray = c.toArray(someIntegerArray);
        assertEquals(originalCollectionSize, resultArray.length);
        Arrays.sort(expecteds); // to compare content irrespective of order
        Arrays.sort(someIntegerArray);
        Arrays.sort(resultArray);
        assertArrayEquals(expecteds, someIntegerArray);
        assertArrayEquals(expecteds, resultArray);
    }

    /**
     * this is test version for collection that guarantees order
     */
    @Test
    public void toArrayParametrizedArrayProvidedHasTheSameSizeAsTheCollectionOrderGuaranteed() {
        Collection<Integer> c = new MySimpleBSTTreeSet<>();
        c.add(20);
        c.add(10);
        c.add(30);
        c.add(40);

        int originalCollectionSize = c.size();

        Integer[] someIntegerArray = new Integer[originalCollectionSize];

        Integer[] expecteds = {20, 10, 30, 40};

        Integer[] resultArray = c.toArray(someIntegerArray);
        assertEquals(originalCollectionSize, resultArray.length);
        assertArrayEquals(expecteds, someIntegerArray);
        assertArrayEquals(expecteds, resultArray);
    }

    @Test
    public void toArrayParametrizedArrayProvidedHasBiggerSizeAsTheCollectionOrderGuaranteed() {
        Collection<Integer> c = new MySimpleBSTTreeSet<>();
        c.add(20);
        c.add(10);
        c.add(30);
        c.add(40);

        int originalCollectionSize = c.size();

        Integer[] someIntegerArray = new Integer[originalCollectionSize + 1];

        Integer[] resultArray = c.toArray(someIntegerArray);
        assertTrue(resultArray.length >= originalCollectionSize);

        assertEquals(Integer.valueOf(20), someIntegerArray[0]);
        assertEquals(Integer.valueOf(10), someIntegerArray[1]);
        assertEquals(Integer.valueOf(30), someIntegerArray[2]);
        assertEquals(Integer.valueOf(40), someIntegerArray[3]);

        assertNull(someIntegerArray[originalCollectionSize]); // null is inserted when copying from original collection ended
    }

    @Test
    public void toArrayParametrizedArrayProvidedHasSmallerSizeAsTheCollectionOrderGuaranteed() {
        Collection<Integer> c = new MySimpleBSTTreeSet<>();
        c.add(20);
        c.add(10);
        c.add(30);
        c.add(40);

        int originalCollectionSize = c.size();

        Integer[] someIntegerArray = new Integer[0];

        Integer[] expecteds = {20, 10, 30, 40};

        Integer[] resultArray = c.toArray(someIntegerArray);
        assertEquals(originalCollectionSize, resultArray.length);
        assertEquals(0 , someIntegerArray.length);
        assertArrayEquals(expecteds, resultArray);
    }

    @Test (expected = ArrayStoreException.class)
    public void toArrayParametrizedArrayIncompatibleTypes() {
        Collection<Integer> c = new MySimpleBSTTreeSet<>();
        c.add(20);

        // not lets try to convert it to array of Strings
        c.toArray(new String[1]);
    }
}

