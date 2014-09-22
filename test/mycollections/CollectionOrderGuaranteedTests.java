package mycollections;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Additional tests for Collection interface, for testing implementations that guarantee iteration order, like TreeSet, LinkedTreeSet, List
 */
public abstract class CollectionOrderGuaranteedTests extends CollectionTests {

    @Test
    public void toArrayParametrizedArrayProvidedHasTheSameSizeAsTheCollectionOrderGuaranteed() {

        cInt.add(20);
        cInt.add(10);
        cInt.add(30);
        cInt.add(40);

        int originalCollectionSize = cInt.size();

        Integer[] someIntegerArray = new Integer[originalCollectionSize];

        Integer[] expecteds = {20, 10, 30, 40};

        Integer[] resultArray = cInt.toArray(someIntegerArray);
        assertEquals(originalCollectionSize, resultArray.length);
        assertArrayEquals(expecteds, someIntegerArray);
        assertArrayEquals(expecteds, resultArray);
    }

    @Test
    public void toArrayParametrizedArrayProvidedHasBiggerSizeAsTheCollectionOrderGuaranteed() {
        cInt.add(20);
        cInt.add(10);
        cInt.add(30);
        cInt.add(40);

        int originalCollectionSize = cInt.size();

        Integer[] someIntegerArray = new Integer[originalCollectionSize + 1];

        Integer[] resultArray = cInt.toArray(someIntegerArray);
        assertTrue(resultArray.length >= originalCollectionSize);

        assertEquals(Integer.valueOf(20), someIntegerArray[0]);
        assertEquals(Integer.valueOf(10), someIntegerArray[1]);
        assertEquals(Integer.valueOf(30), someIntegerArray[2]);
        assertEquals(Integer.valueOf(40), someIntegerArray[3]);

        assertNull(someIntegerArray[originalCollectionSize]); // null is inserted when copying from original collection ended
    }

    @Test
    public void toArrayParametrizedArrayProvidedHasSmallerSizeAsTheCollectionOrderGuaranteed() {
        cInt.add(20);
        cInt.add(10);
        cInt.add(30);
        cInt.add(40);

        int originalCollectionSize = cInt.size();

        Integer[] someIntegerArray = new Integer[0];

        Integer[] expecteds = {20, 10, 30, 40};

        Integer[] resultArray = cInt.toArray(someIntegerArray);
        assertEquals(originalCollectionSize, resultArray.length);
        assertEquals(0 , someIntegerArray.length);
        assertArrayEquals(expecteds, resultArray);
    }
}
