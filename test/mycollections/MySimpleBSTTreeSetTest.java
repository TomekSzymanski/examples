package mycollections;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by SG0892454 on 2014-09-01.
 */
public class MySimpleBSTTreeSetTest extends SetTests {

    @Before @Override
    public void resetCollection() {
        c = new MySimpleBSTTreeSet<String>();
    }

    @Test
    public void findSuccessor() {
        MySimpleBSTTreeSet<String> c=new MySimpleBSTTreeSet<String>();
        c.add("1");
        c.add("2");
        assertEquals("2", (c.findSuccessor(c.getTreeNodeByValue("1")).value));
    }

    @Test
    public void findSuccessor2() {
        MySimpleBSTTreeSet<Integer> c=new MySimpleBSTTreeSet<Integer>();
        c.add(10);
        c.add(5);
        c.add(15);
        c.add(12);
        Node n = c.findSuccessor(c.getTreeNodeByValue(5));
        assertEquals(Integer.valueOf(10), (c.findSuccessor(c.getTreeNodeByValue(5)).value));
        assertEquals(Integer.valueOf(12), (c.findSuccessor(c.getTreeNodeByValue(10)).getValue()));
        assertEquals(Integer.valueOf(15), (c.findSuccessor(c.getTreeNodeByValue(12)).value));
        assertNull(c.findSuccessor(c.getTreeNodeByValue(15)));
    }

    @Test (expected = ClassCastException.class)
    public void typeThatDoesNotHaveNaturalOrdering() {
        MySimpleBSTTreeSet<Object> c = new MySimpleBSTTreeSet<>();
        c.add(new Object()); // Object is not Comparable
    }

    @Test
    public void customComparator() {

        Comparator<String> customComparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        };

        MySimpleBSTTreeSet<String> c = new MySimpleBSTTreeSet<>(customComparator);
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

        Comparator<String> customComparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        };

        MySimpleBSTTreeSet<String> c = new MySimpleBSTTreeSet<>(customComparator);
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

}
