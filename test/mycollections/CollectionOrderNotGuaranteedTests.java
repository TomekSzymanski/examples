package mycollections;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * Additional tests for Collection interface, for testing implementations that do not guarantee iteration order, like HashMap
 */
public abstract class CollectionOrderNotGuaranteedTests extends CollectionTests {
    @Test
    public void toArrayParametrizedArrayProvidedHasTheSameSizeAsTheCollectionNoOrder() {
        Collection<Integer> c = new BSTTreeSet<>();
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
}
