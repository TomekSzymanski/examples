package mycollections;

import com.sun.istack.internal.NotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for any classes implementing Collection interface. They test all methods specified in Collection interface.
 * To use this tests concrete implementations of Collection interface must derive from this class
 * and implement method createEmptyCollection() which must create new empty instance of concrete class.
 * createEmptyCollection() method must create new instance every time it is called, it cannot reuse the same instance.
 *
 * Tests are for all methods defined in Collection interface, also optional ones.
 *
 * Please note that bigNumberOfElements() initializes Collection with random numbers, so it is not deterministic. However should pass for any random numbers.
 */
public abstract class CollectionTests  {

    protected Collection<String> c;
    protected Collection<Integer> cInt;

    /*
    prepares concrete implementation of Collection for tests.
    For every test creates new (clean) instance of concrete class that implements Collection
    test classes implementing this method must assign new collection instance to the c Collection<String> reference
     */
    @Before
    public void resetCollection() {
        c = createEmptyCollection();
        cInt = createEmptyCollection();
    }

    protected abstract <E> Collection<E> createEmptyCollection();

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

    Collection<Integer> createCollectionOfIntegersWithRandomContent(int numberOfElements) {
        Collection<Integer> c = createEmptyCollection();
        Integer[] integerArray = new Integer[numberOfElements];

        for (int i=0; i<numberOfElements; i++) {
            integerArray[i] = i;
        }
        List<Integer> shuffled = Arrays.asList(integerArray);
        Collections.shuffle(shuffled);
        for (Integer i : shuffled) {
            c.add(i);
        }
        return c;
    }

    /*
    brute force test
     */
    @Test
    public void bigNumberOfElements() throws IOException, ClassNotFoundException {
        int NUMBER_OF_ELEMENTS = 500000;
        cInt = createCollectionOfIntegersWithRandomContent(NUMBER_OF_ELEMENTS);

        assertEquals(NUMBER_OF_ELEMENTS, cInt.size());

        cInt.remove(20);
        assertFalse(cInt.contains(20));
        assertEquals(NUMBER_OF_ELEMENTS - 1,  cInt.size());

        cInt.remove(30);
        assertFalse(cInt.contains(30));
        assertEquals(NUMBER_OF_ELEMENTS - 2,  cInt.size());

        cInt.remove(40);
        assertFalse(cInt.contains(40));
        assertEquals(NUMBER_OF_ELEMENTS - 3,  cInt.size());

        cInt.remove(50);
        assertFalse(cInt.contains(50));
        assertEquals(NUMBER_OF_ELEMENTS - 4,  cInt.size());

        cInt.remove(10);
        assertFalse(cInt.contains(10));
        assertEquals(NUMBER_OF_ELEMENTS - 5,  cInt.size());

        cInt.remove(1000);
        assertFalse(cInt.contains(1000));
        assertEquals(NUMBER_OF_ELEMENTS - 6,  cInt.size());

        cInt.remove(2000);
        assertFalse(cInt.contains(2000));
        assertEquals(NUMBER_OF_ELEMENTS - 7,  cInt.size());

        cInt.remove(3000);
        assertFalse(cInt.contains(3000));
        assertEquals(NUMBER_OF_ELEMENTS - 8,  cInt.size());

        cInt.remove(4000);
        assertFalse(cInt.contains(4000));
        assertEquals(NUMBER_OF_ELEMENTS - 9,  cInt.size());
    }

    private <E> Collection<E> initializeCollectionWithArrayContents(E[] inputDataArray) {
        Collection<E> c = createEmptyCollection();
        Collections.addAll(c, inputDataArray);
        return c;
    }

    @Test
    public void bugWithRemove() {
        Integer[] input = new Integer[]{77, 44, 85, 94, 30, 14, 66, 48, 55, 42, 84, 49, 17, 6, 63, 89, 23, 4, 5, 22, 36, 47, 78, 26, 99, 37, 16, 34, 81, 93, 10, 75, 67, 11, 96, 58, 98, 45, 80, 56, 60, 38, 62, 50, 97, 1, 46, 40, 90, 41, 7, 69, 76, 35, 70, 25, 13, 24, 79, 71, 61, 12, 15, 21, 92, 86, 88, 54, 83, 33, 87, 52, 39, 95, 59, 2, 72, 29, 0, 64, 31, 51, 65, 43, 53, 3, 28, 9, 18, 68, 74, 19, 8, 73, 32, 57, 82, 20, 27, 91};
        Collection<Integer> cInt = initializeCollectionWithArrayContents(input);

        int NUMBER_OF_ELEMENTS = input.length;

        cInt.remove(20);
        assertFalse(cInt.contains(20));
        assertEquals(NUMBER_OF_ELEMENTS - 1,  cInt.size());

        cInt.remove(30);
        assertFalse(cInt.contains(30));
        assertTrue(cInt.contains(32));
        assertEquals(NUMBER_OF_ELEMENTS - 2,  cInt.size());

        cInt.remove(40);
        assertFalse(cInt.contains(40));
        assertEquals(NUMBER_OF_ELEMENTS - 3,  cInt.size());

        cInt.remove(50);
        assertFalse(cInt.contains(50));
        assertEquals(NUMBER_OF_ELEMENTS - 4,  cInt.size());

        cInt.remove(10);
        assertFalse(cInt.contains(10));
        assertEquals(NUMBER_OF_ELEMENTS - 5,  cInt.size());
    }


    @Test
    public void bugWithRemove2() {
        Integer[] input = new Integer[]{55, 0, 92, 67, 24, 8, 43, 73, 81, 26, 22, 5, 65, 23, 20, 58, 89, 74, 94, 62, 52, 57, 54, 9, 78, 19, 39, 46, 45, 61, 83, 95, 4, 2, 77, 7, 47, 75, 6, 79, 88, 13, 64, 68, 40, 12, 41, 51, 50, 16, 29, 48, 38, 90, 56, 30, 59, 17, 3, 11, 18, 63, 80, 87, 91, 32, 53, 34, 60, 98, 15, 44, 86, 10, 84, 71, 82, 42, 21, 72, 27, 96, 14, 1, 37, 49, 35, 93, 36, 99, 97, 70, 66, 33, 25, 85, 28, 69, 31, 76};
        Collection<Integer> cInt = initializeCollectionWithArrayContents(input);

        int NUMBER_OF_ELEMENTS = input.length;

        cInt.remove(20);
        assertFalse(cInt.contains(20));
        assertEquals(NUMBER_OF_ELEMENTS - 1, cInt.size());
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
    public void containsAllPositive() {
        cInt.addAll(Arrays.asList(7,1,6,9,2,3,8,4,5));
        assertTrue(cInt.containsAll(Arrays.asList(2, 4, 6, 8)));

    }

    @Test
    public void containsAllNegative() {
        cInt.addAll(Arrays.asList(7,1,6,9,2,3,8,4,5));
        assertFalse(cInt.containsAll(Arrays.asList(2, 4, 6, 8, 10000)));
    }


    @Test
    public void removeFromOneElementTree() {
        c.add("one");
        boolean result = c.remove("one");
        assertTrue(result);
        assertEquals(0, c.size());
        assertFalse(c.contains("one"));
    }

    @Test
    public void removeSimple1() {
        cInt.add(77);
        cInt.add(44);
        boolean result = cInt.remove(77);
        assertTrue(result);
        assertEquals(1, cInt.size());
        assertFalse(cInt.contains(77));
        assertTrue(cInt.contains(44));
    }

    @Test
    public void removeSimple2() {
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
    public void removeRootElementFrom3ElementTree() {
        cInt.add(20);
        cInt.add(10);
        cInt.add(30);
        cInt.remove(20);
        assertFalse(cInt.contains(20));
    }

    @Test
    public void removeNonRootNodeWithLeafSuccessor() {
        cInt.add(1);
        cInt.add(20);
        cInt.add(10);
        cInt.add(30);
        /*
                1                               1
                  \                               \
                    20              =>             30
                  /   \                           /
                10     30                       10
         */

        cInt.remove(20);
        assertFalse(cInt.contains(20));
    }

    @Test
    public void toArray() {
        cInt.add(10);
        cInt.add(20);
        cInt.add(30);
        cInt.add(1);
        cInt.add(2);
        cInt.add(3);
        cInt.add(100);
        cInt.add(200);

        Object[] array = cInt.toArray();

        Object[] expecteds = new Object[] {10, 1, 2, 3, 20, 30, 100, 200};

        assertTrue(Arrays.binarySearch(array, 20) >= 0);
        assertTrue(Arrays.binarySearch(array, 100) >= 0);
        assertTrue(Arrays.binarySearch(array, 200) >= 0);

        assertArrayEquals(expecteds, array);

    }

    @Test @Ignore
    public void toArrayArrayIsNotReferencedByCollection() {

        class SimplestMutableClass implements Comparable {
            int value;
            boolean modifiedBit;

            SimplestMutableClass(int value) {
                this.value = value;
            }

            @Override
            public int compareTo(@NotNull Object o) {
                return value - ((SimplestMutableClass)o).value;
            }
        }

        Collection<SimplestMutableClass> c = createEmptyCollection();

        SimplestMutableClass one = new SimplestMutableClass(1);
        SimplestMutableClass two = new SimplestMutableClass(2);
        SimplestMutableClass three = new SimplestMutableClass(3);

        c.add(one);
        c.add(two);
        c.add(three);

        Object[] array = c.toArray();

        assert Arrays.binarySearch(array, two) >= 0;

        two.modifiedBit = true; // this change should not be reflected in the copied array contents

        assertFalse(((SimplestMutableClass)array[Arrays.binarySearch(array, two)]).modifiedBit);
        // it does not work for HashSet either
    }

    @Test (expected = ArrayStoreException.class)
    public void toArrayParametrizedArrayIncompatibleTypes() {
        cInt.add(20);

        // not lets try to convert it to array of Strings
        cInt.toArray(new String[1]);
    }

    @Test
    public void removeAllSimple() {
        Integer[] input = new Integer[]{77, 44};
        cInt.addAll(Arrays.asList(input));

        Integer[] toBeRemoved= new Integer[]{77, 44};
        boolean removed = cInt.removeAll(Arrays.asList(toBeRemoved));

        assertTrue(removed);
        for (Integer element : toBeRemoved) {
            assertFalse(cInt.contains(element));
        }
        assertEquals(input.length - toBeRemoved.length, cInt.size());
    }

    @Test
    public void removeAllMany() {
        Integer[] input = new Integer[]{77, 44, 85, 94, 30, 14, 66, 48, 55, 42, 84, 49, 17, 6, 63, 89, 23, 4, 5, 22, 36, 47, 78, 26, 99, 37, 16, 34, 81, 93, 10, 75, 67, 11, 96, 58, 98, 45, 80, 56, 60, 38, 62, 50, 97, 1, 46, 40, 90, 41, 7, 69, 76, 35, 70, 25, 13, 24, 79, 71, 61, 12, 15, 21, 92, 86, 88, 54, 83, 33, 87, 52, 39, 95, 59, 2, 72, 29, 0, 64, 31, 51, 65, 43, 53, 3, 28, 9, 18, 68, 74, 19, 8, 73, 32, 57, 82, 20, 27, 91};
        cInt.addAll(Arrays.asList(input));

        Integer[] toBeRemoved= new Integer[]{77, 49, 23, 4, 81, 93, 62, 50, 91};
        boolean removed = cInt.removeAll(Arrays.asList(toBeRemoved));

        assertTrue(removed);
        for (Integer element : toBeRemoved) {
            assertFalse(cInt.contains(element));
        }
        assertEquals(input.length - toBeRemoved.length, cInt.size());
    }

    @Test
    public void retainAll() {
        cInt.addAll(Arrays.asList(1,2,3,4,5));

        // retain only even numbers
        boolean retained = cInt.retainAll(Arrays.asList(2,4));
        assertTrue(retained);

        assertFalse(cInt.contains(1));
        assertTrue(cInt.contains(2));
        assertFalse(cInt.contains(3));
        assertTrue(cInt.contains(4));

        assertEquals(2, cInt.size());
    }

    @Test
    public void retainAllIncludingRoot() {
        cInt.addAll(Arrays.asList(1,2,3,4));

        // retain only odd numbers
        boolean retained = cInt.retainAll(Arrays.asList(1,3));
        assertTrue(retained);

        assertTrue(cInt.contains(1));
        assertFalse(cInt.contains(2));
        assertTrue(cInt.contains(3));
        assertFalse(cInt.contains(4));

        assertEquals(2, cInt.size());
    }


    @Test
    public void retainAllMany() {
        Integer[] input = new Integer[]{77, 44, 85, 94, 30, 14, 66, 48, 55, 42, 84, 49, 17, 6, 63, 89, 23, 4, 5, 22, 36, 47, 78, 26, 99, 37, 16, 34, 81, 93, 10, 75, 67, 11, 96, 58, 98, 45, 80, 56, 60, 38, 62, 50, 97, 1, 46, 40, 90, 41, 7, 69, 76, 35, 70, 25, 13, 24, 79, 71, 61, 12, 15, 21, 92, 86, 88, 54, 83, 33, 87, 52, 39, 95, 59, 2, 72, 29, 0, 64, 31, 51, 65, 43, 53, 3, 28, 9, 18, 68, 74, 19, 8, 73, 32, 57, 82, 20, 27, 91};
        cInt.addAll(Arrays.asList(input));

        // retain only multipliers of 20
        Integer[] toBeRetained= new Integer[]{20, 40, 60, 80};
        boolean retained = cInt.retainAll(Arrays.asList(toBeRetained));

        assertTrue(retained);
        for (Integer element : toBeRetained) {
            assertTrue(cInt.contains(element));
        }
        assertEquals(toBeRetained.length, cInt.size());
    }

}

