package mycollections;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Testing BSTTreeSet, using general Collection tests, plus tests for Collection guaranteeing iteration order.
 * Additionally it unit tests methods in this specific implementation.
 */
public class BSTTreeSetTest extends CollectionOrderGuaranteedTests {


    @Override
    protected <E> Collection<E> createEmptyCollection() {
        return new BSTTreeSet<>();
    }

    @Test
    public void findSuccessor() {
        BSTTreeSet<String> c=new BSTTreeSet<>();
        c.add("1");
        c.add("2");
        assertEquals("2", (c.findSuccessor(c.getTreeNodeByValue("1")).getValue()));
    }

    @Test
    public void findSuccessor2() {
        BSTTreeSet<Integer> c=new BSTTreeSet<>();
        c.add(10);
        c.add(5);
        c.add(15);
        c.add(12);
        assertEquals(Integer.valueOf(10), (c.findSuccessor(c.getTreeNodeByValue(5)).getValue()));
        assertEquals(Integer.valueOf(12), (c.findSuccessor(c.getTreeNodeByValue(10)).getValue()));
        assertEquals(Integer.valueOf(15), (c.findSuccessor(c.getTreeNodeByValue(12)).getValue()));
        assertNull(c.findSuccessor(c.getTreeNodeByValue(15)));
    }

    @Test (expected = ClassCastException.class)
    public void typeThatDoesNotHaveNaturalOrdering() {
        BSTTreeSet<Object> c = new BSTTreeSet<>();
        c.add(new Object()); // Object is not Comparable
    }

    @Test
    public void customComparator() {

        Comparator<String> customComparator = (o1, o2) -> o1.length() - o2.length();

        BSTTreeSet<String> c = new BSTTreeSet<>(customComparator);
        c.add("DDDD");
        c.add("XXXXXXXXX");
        c.add("AAAAAAAAAAAAA");
        c.add("BBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        c.add("CCCCCCCCCCCCCCCCCCC");
        Iterator<String> it = c.iterator();
        assertEquals("DDDD", it.next());
        assertEquals("XXXXXXXXX", it.next());
        assertEquals("AAAAAAAAAAAAA", it.next());
        assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBB", it.next());
        assertEquals("CCCCCCCCCCCCCCCCCCC", it.next());
    }

    @Test
    public void customComparator2() {

        Comparator<String> customComparator = (o1, o2) -> o1.length() - o2.length();

        BSTTreeSet<String> c = new BSTTreeSet<>(customComparator);
        c.add("AAAAAAAAAAAAA");
        c.add("BBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        c.add("DDDD");
        c.add("XXXXXXXXX");
        c.add("CCCCCCCCCCCCCCCCCCC");
        Iterator<String> it = c.iterator();
        assertEquals("AAAAAAAAAAAAA", it.next());
        assertEquals("DDDD", it.next());
        assertEquals("XXXXXXXXX", it.next());
        assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBB", it.next());
        assertEquals("CCCCCCCCCCCCCCCCCCC", it.next());
    }

    @Test
    public void equals() {
        BSTTreeSet<Integer> treeOne = new BSTTreeSet<>();
        treeOne.addAll(Arrays.asList(10, 5, 15, 20));

        BSTTreeSet<Integer> treeTwo = new BSTTreeSet<>();
        treeTwo.addAll(Arrays.asList(10, 5, 15, 20));

        assertEquals(treeOne, treeTwo);
    }

    @Test
    public void equalsNot() {
        BSTTreeSet<Integer> treeOne = new BSTTreeSet<>();
        treeOne.add(1);
        treeOne.add(2);
        treeOne.add(3);
        // TODO: for implementing, cannot we just use our pre-order iterator? we cannot use iterators (pre-order) for comparing, as for both trees pre-order iterator would print the same content (while the structure is different)

        BSTTreeSet<Integer> treeTwo = new BSTTreeSet<>();
        treeTwo.add(2);
        treeTwo.add(1);
        treeTwo.add(3);

        assertNotEquals(treeOne, treeTwo);
    }

    @Test
    public void serializeDeserialize() throws IOException, ClassNotFoundException {
        BSTTreeSet<Integer> originalTree = (BSTTreeSet<Integer>) createCollectionOfIntegersWithRandomContent(100000);

        BSTTreeSet<Integer> deserializedTree = (BSTTreeSet<Integer>) serializeAndDeserialize(originalTree);
        assertEquals(originalTree, deserializedTree);
    }

    @Test
    public void serializeDeserialize3() throws IOException, ClassNotFoundException {
        BSTTreeSet<Integer> originalTree = new BSTTreeSet<>();
        originalTree.addAll(Arrays.asList(0, 1, 3, 2));
         /*
                0
                 \
                  1
                   \
                    3
                  /
                2
         */
        BSTTreeSet<Integer> deserializedTree = (BSTTreeSet<Integer>) serializeAndDeserialize(originalTree);
        assertEquals(originalTree, deserializedTree);
    }

    /*
                                     0
                                      \
                                       7
                                     /   \
                                    6     8
                                   /      \
                                  2        10
                                / \       /  \
                               1   5     9    11
                                  /
                                 3
                                  \
                                   4
     */
    @Test
    public void serializeDeserialize4() throws IOException, ClassNotFoundException {
        BSTTreeSet<Integer> originalTree = new BSTTreeSet<>();
        originalTree.addAll(Arrays.asList(0, 7, 6, 2, 8, 1, 10, 11, 5, 3, 4, 9));

        BSTTreeSet<Integer> deserializedTree = (BSTTreeSet<Integer>) serializeAndDeserialize(originalTree);
        assertEquals(originalTree, deserializedTree);
    }

    private <E> List<E> serializeAndDeserializeTreeAsListOfElements(BSTTreeSet<E> tree) throws IOException, ClassNotFoundException {
        BSTTreeSet<E> deserializedTree = (BSTTreeSet<E>) serializeAndDeserialize(tree);
        List<E> deserializedElements = new ArrayList<>();
        for (Iterator<E> iterator = new PreOrderTreeIterator<E>(deserializedTree, true); iterator.hasNext() ; ) {
            deserializedElements.add(iterator.next());
        }
        return deserializedElements;
    }

    private static Object serializeAndDeserialize(Object input) throws IOException, ClassNotFoundException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bytes)) {

            oos.writeObject(input);

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
                return ois.readObject();
            }
        }
    }

    @Test
    public void deserialize1() throws IOException, ClassNotFoundException {
        BSTTreeSet<Integer> originalTree = new BSTTreeSet<>();
        originalTree.addAll(Arrays.asList(2,1,3,0));
        /*
               2
             /  \
            1    3
          /
         0
         will be serialized as (N stands for null): 2, 1, 0, N, N, N, 3, N, N
        */
        List<Integer> deserialized = serializeAndDeserializeTreeAsListOfElements(originalTree);
        assertArrayEquals(new Integer[]{2, 1, 0, null, null, null, 3, null, null}, deserialized.toArray());
    }

    @Test
    public void serializeDeserialize1() throws IOException, ClassNotFoundException {
        BSTTreeSet<Integer> originalTree = new BSTTreeSet<>();
        originalTree.addAll(Arrays.asList(2,1,3,0));

        BSTTreeSet<Integer> deserializedTree = (BSTTreeSet<Integer>) serializeAndDeserialize(originalTree);
        assertEquals(originalTree, deserializedTree);
    }

    /*
                        80
                       /  \
                      20   99
                     /  \
                    13   30
                        /  \
                       21   58
                             \
                              67
     */
    @Test
    public void serializeDeserialize5() throws IOException, ClassNotFoundException {
        BSTTreeSet<Integer> originalTree = new BSTTreeSet<>();
        originalTree.addAll(Arrays.asList(80, 20, 99, 13, 30, 21, 58, 67));

        BSTTreeSet<Integer> deserializedTree = (BSTTreeSet<Integer>) serializeAndDeserialize(originalTree);
        assertEquals(originalTree, deserializedTree);
    }

    @Test
    public void serializeDeserializeOtherComparator() throws IOException, ClassNotFoundException {
        /*
         Comparator must be serialized and deserialized, because for example, we created tree with other comparator
         (like reverse order comparator), and after serialization and deserialization, we try to add new elements.
         Without old comparator elements will be put into incorrect place.
          */
        ReverseOrderComparator reverseOrderComparator = new ReverseOrderComparator();

        BSTTreeSet<Integer> originalTree = new BSTTreeSet<Integer>(reverseOrderComparator);

        originalTree.add(2);
        originalTree.add(1);
        originalTree.add(3);

        BSTTreeSet<Integer> deserializedTree = (BSTTreeSet<Integer>) serializeAndDeserialize(originalTree);
        assertEquals(originalTree, deserializedTree);
    }

    /*
        performs sanity test on tree after every test: check for every node, check if father-son relations are simetric (if father F has son S, then son S must have father F).
    */
    @After
    public void treeSanityCheck() {
        BSTTreeSet tree = (BSTTreeSet)c;
        BSTTreeSet treeInt = (BSTTreeSet)cInt;

        assertTrue(tree.areNodeRelationsFine(tree.getRootNode()));
        assertTrue(treeInt.areNodeRelationsFine(treeInt.getRootNode()));
    }


}
