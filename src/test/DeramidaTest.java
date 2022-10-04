package src.test;

import src.main.Deramida;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DeramidaTest {
    @Test
    public void testAdd() {

        Deramida deramida = new Deramida<>();

        assertTrue(deramida.isEmpty());

        assertTrue(deramida.add(1));
        assertTrue(deramida.add(8));
        assertTrue(deramida.add(9));

        assertEquals(3, deramida.size());

        assertTrue(deramida.add(15));
        assertTrue(deramida.add(7));
        assertTrue(deramida.add(21));
        assertTrue(deramida.add(11));

        assertEquals(7, deramida.size());
    }
    @Test
    public void testRemove() {
        Deramida deramida = new Deramida<>();

        deramida.add(3);
        deramida.add(7);
        deramida.add(18);
        deramida.add(76);
        deramida.add(20);

        assertEquals(5, deramida.size());

        assertTrue(deramida.remove(3));
        assertTrue(deramida.remove(20));
        assertTrue(deramida.remove(76));
        assertFalse(deramida.remove(55));

        assertEquals(2, deramida.size());

        assertTrue(deramida.remove(7));
        assertTrue(deramida.remove(18));

        assertTrue(deramida.isEmpty());
    }

    @Test
    public void testContains() {
        Deramida deramida = new Deramida<>();

        deramida.add(31);
        deramida.add(27);
        deramida.add(138);

        assertTrue(deramida.contains(31));
        assertTrue(deramida.contains(27));
        assertTrue(deramida.contains(138));
        assertFalse(deramida.contains(0));
        assertFalse(deramida.contains(13));

        deramida.remove(31);
        assertFalse(deramida.contains(31));
    }

    @Test
    public void testFirst() {
        Deramida deramida = new Deramida<>();

        deramida.add(3);

        assertEquals(3, deramida.first());

        deramida.add(1);
        deramida.add(10);

        assertEquals(1, deramida.first());

        deramida.remove(1);
        deramida.remove(3);

        assertEquals(10, deramida.first());
    }

    @Test
    public void testLast() {
        Deramida deramida = new Deramida<>();
        deramida.add(11);

        assertEquals(11, deramida.last());

        deramida.add(1);
        deramida.add(23);

        assertEquals(23, deramida.last());

        deramida.remove(11);
        deramida.remove(23);

        assertEquals(1, deramida.last());
    }

    @Test
    public void testAddAll() {
        Deramida deramida = new Deramida<>();
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        assertTrue(deramida.isEmpty());

        list.add(1);
        list.add(10);
        list.add(16);
        list.add(4);
        list.add(45);

        list2.add(1);
        list2.add(45);

        deramida.addAll(list);

        assertTrue(deramida.contains(1));
        assertTrue(deramida.contains(10));
        assertTrue(deramida.contains(16));
        assertTrue(deramida.contains(4));
        assertTrue(deramida.contains(45));
        assertEquals(5, deramida.size());

        deramida.addAll(list1);
        assertEquals(5, deramida.size());

        deramida.addAll(list2);
        assertEquals(5, deramida.size());
    }

    @Test
    public void testContainsAll() {
        Deramida deramida = new Deramida<>();
        List<Integer> list = new ArrayList<>();

        deramida.add(106);
        deramida.add(687);
        deramida.add(224);
        deramida.add(1566);

        list.add(106);
        list.add(687);
        list.add(224);

        assertTrue(deramida.containsAll(list));

        list.add(1566);

        assertTrue(deramida.containsAll(list));

        list.remove(3);
        assertTrue(deramida.containsAll(list));

        list.add(136);
        assertFalse(deramida.containsAll(list));
    }

    @Test
    public void testRemoveAll() {
        Deramida deramida = new Deramida<>();
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();

        deramida.add(125);
        deramida.add(246);
        deramida.add(688);
        deramida.add(2536);

        list.add(125);
        list.add(246);

        list1.add(688);
        list1.add(2536);

        deramida.removeAll(list);

        assertTrue(deramida.contains(688));
        assertTrue(deramida.contains(2536));
        assertEquals(2, deramida.size());

        deramida.removeAll(list1);
        assertTrue(deramida.isEmpty());
    }

    @Test
    public void testRetainAll() {
        Deramida deramida = new Deramida<>();
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();

        deramida.add(4);
        deramida.add(978);
        deramida.add(468);
        deramida.add(24);
        deramida.add(987);
        deramida.add(534);

        list.add(534);
        list.add(978);

        deramida.retainAll(list);
        assertEquals(list.size(), deramida.size());
        assertTrue(deramida.contains(534));
        assertTrue(deramida.contains(978));

        deramida.retainAll(list1);
        assertTrue(deramida.isEmpty());
    }

    @Test
    public void testClear() {
        Deramida deramida = new Deramida<>();

        deramida.add(4);
        deramida.add(978);
        deramida.add(468);
        deramida.add(24);

        deramida.clear();
        assertTrue(deramida.isEmpty());

        deramida.add(1);
        deramida.clear();
        assertTrue(deramida.isEmpty());
    }

    @Test
    public void testIterator() {
        Deramida deramida = new Deramida<>();
        Set expectedSet = new HashSet<>();

        deramida.add(4);
        deramida.add(14);
        deramida.add(1);
        deramida.add(35);
        deramida.add(7);
        deramida.add(13);
        deramida.add(789);
        deramida.add(90);
        deramida.add(55);

        expectedSet.add(4);
        expectedSet.add(14);
        expectedSet.add(1);
        expectedSet.add(35);
        expectedSet.add(7);
        expectedSet.add(13);
        expectedSet.add(789);
        expectedSet.add(90);
        expectedSet.add(55);


        Iterator<Integer> iterator1 = deramida.iterator();
        Iterator<Integer> iterator2 = deramida.iterator();
        Iterator<Integer> iterator3 = deramida.iterator();

        while (iterator1.hasNext()) {
            assertEquals(iterator2.next(), iterator1.next());
        }

        while (iterator3.hasNext()) {
            assertTrue(expectedSet.contains(iterator3.next()));
            iterator3.remove();
         }

        assertTrue(deramida.isEmpty());
    }

    @Test
    public void testSubSet() {
        Deramida deramida = new Deramida<>();

        deramida.add(7);
        deramida.add(2);
        deramida.add(0);
        deramida.add(5);
        deramida.add(10);
        deramida.add(14);

        SortedSet<Integer> subTree = deramida.subSet(5, 11);

        assertFalse(subTree.contains(0));
        assertFalse(subTree.contains(2));
        assertFalse(subTree.contains(14));
        assertTrue(subTree.contains(5));
        assertTrue(subTree.contains(7));
        assertTrue(subTree.contains(10));

        SortedSet<Integer> subTree2 = deramida.subSet(3, 10);

        assertFalse(subTree2.contains(0));
        assertFalse(subTree2.contains(2));
        assertFalse(subTree2.contains(10));
        assertFalse(subTree2.contains(14));
        assertTrue(subTree2.contains(5));
        assertTrue(subTree2.contains(7));
    }

    @Test
    public void testTailSet() {
        Deramida deramida = new Deramida<>();

        deramida.add(6);
        deramida.add(1);
        deramida.add(0);
        deramida.add(15);
        deramida.add(18);
        deramida.add(34);

        SortedSet<Integer> subTree = deramida.tailSet(15);

        assertFalse(subTree.contains(0));
        assertFalse(subTree.contains(1));
        assertFalse(subTree.contains(6));
        assertTrue(subTree.contains(15));
        assertTrue(subTree.contains(18));
        assertTrue(subTree.contains(34));

        SortedSet<Integer> subTree2 = deramida.tailSet(3);

        assertFalse(subTree2.contains(0));
        assertFalse(subTree2.contains(1));
        assertTrue(subTree2.contains(6));
        assertTrue(subTree2.contains(15));
        assertTrue(subTree2.contains(18));
        assertTrue(subTree2.contains(34));
    }

    @Test
    public void testHeadSet() {
        Deramida deramida = new Deramida<>();

        deramida.add(6);
        deramida.add(1);
        deramida.add(0);
        deramida.add(15);
        deramida.add(18);
        deramida.add(34);

        SortedSet<Integer> subTree = deramida.headSet(15);

        assertTrue(subTree.contains(0));
        assertTrue(subTree.contains(1));
        assertTrue(subTree.contains(6));
        assertTrue(!subTree.contains(15));
        assertFalse(subTree.contains(18));
        assertFalse(subTree.contains(34));

        SortedSet<Integer> subTree2 = deramida.headSet(3);

        assertTrue(subTree2.contains(0));
        assertTrue(subTree2.contains(1));
        assertFalse(subTree2.contains(6));
        assertFalse(subTree2.contains(15));
        assertFalse(subTree2.contains(18));
        assertFalse(subTree2.contains(34));
    }
}
