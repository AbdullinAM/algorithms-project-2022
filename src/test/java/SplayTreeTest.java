import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SplayTreeTest {
    private AbstractSet<Integer> tree1 = new SplayTree<>();
    private SplayTree<Integer> tree2 = new SplayTree<>();
    private SplayTree<Integer> tree3 = new SplayTree<>();

    @Test
    public void testAddAddWithSplitContainsFind() {
        tree1 = new SplayTree<>();
        tree2 = new SplayTree<>();
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 5, 15, 1, 3, 10, 2);

        assertTrue(tree1.addAll(list));
        assertEquals(tree1.size(), 6);
        assertFalse(tree1.isEmpty());

        assertTrue(tree1.contains(5));
        assertTrue(tree1.contains(15));
        assertTrue(tree1.contains(10));
        assertTrue(tree1.contains(2));
        assertFalse(tree1.contains(6));
        assertFalse(tree1.contains(7));
        assertFalse(tree1.contains(0));
        assertFalse(tree1.contains(16));

        list.clear();
        Collections.addAll(list, 1, 15, 3, 5, 10, 2);

        assertTrue(tree3.addAll(list));
        assertEquals(tree3.size(), 6);
        assertFalse(tree3.isEmpty());

        assertTrue(tree2.addWithSplit(1));
        assertTrue(tree2.addWithSplit(15));
        assertTrue(tree2.addWithSplit(3));
        assertTrue(tree2.addWithSplit(5));
        assertTrue(tree2.addWithSplit(10));
        assertTrue(tree2.addWithSplit(2));
        assertEquals(tree2.size(), 6);
        assertFalse(tree2.isEmpty());

        assertTrue(tree2.contains(5));
        assertEquals(tree2.getRootValue(), 5);
        assertTrue(tree2.contains(10));
        assertEquals(tree2.getRootValue(), 10);
        assertTrue(tree2.contains(2));
        assertEquals(tree2.getRootValue(), 2);
        assertTrue(tree2.contains(3));
        assertEquals(tree2.getRootValue(), 3);

        assertFalse(tree2.contains(6));
        assertFalse(tree2.contains(7));
        assertFalse(tree2.contains(0));
        assertFalse(tree2.contains(16));

        assertEquals(tree1, tree2);
        assertTrue(tree2.contains(15));
        assertEquals(tree2.getRootValue(), 15);
        assertTrue(tree2.contains(2));
        assertEquals(tree2.getRootValue(), 2);
    }


    @Test
    public void testRemoveRemoveWitSplit() {
        tree1 = new SplayTree<>();
        tree2 = new SplayTree<>();
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 5, 15, 1, 3, 10, 2);

        assertTrue(tree1.addAll(list));

        assertTrue(tree2.addWithSplit(1));
        assertTrue(tree2.addWithSplit(15));
        assertTrue(tree2.addWithSplit(3));
        assertTrue(tree2.addWithSplit(5));
        assertTrue(tree2.addWithSplit(10));
        assertTrue(tree2.addWithSplit(2));

        assertEquals(tree1, tree2);
        assertFalse(tree1.remove(0));
        assertTrue(tree1.contains(5));

        assertTrue(tree1.remove(5));
        assertFalse(tree1.remove(5));
        assertFalse(tree1.contains(5));

        assertNotEquals(tree1, tree2);
        assertTrue(tree1.remove(1));
        assertFalse(tree1.remove(1));
        assertTrue(tree1.remove(15));
        assertFalse(tree1.remove(15));
        assertEquals(tree1.size(), 3);
        assertFalse(tree1.isEmpty());

        list.clear();
        Collections.addAll(list, 2, 3, 10);

        assertTrue(tree1.removeAll(list));
        assertTrue(tree1.isEmpty());

        assertFalse(tree2.removeWithSplit(0));
        assertTrue(tree2.contains(5));

        assertTrue(tree2.removeWithSplit(5));
        assertFalse(tree2.removeWithSplit(5));
        assertFalse(tree2.contains(5));

        assertNotEquals(tree1, tree2);
        assertTrue(tree2.removeWithSplit(1));
        assertFalse(tree2.removeWithSplit(1));
        assertTrue(tree2.removeWithSplit(15));
        assertFalse(tree2.removeWithSplit(15));
        assertEquals(tree2.size(), 3);
        assertFalse(tree2.isEmpty());

        assertTrue(tree2.remove(2));
        assertTrue(tree2.remove(3));
        assertTrue(tree2.remove(10));
        assertTrue(tree2.isEmpty());

        assertEquals(tree1, tree2);
    }

    @Test
    public void testSplitMergeFindMaxFindMinVisualization() {
        tree2 = new SplayTree<>();
        SplayTree.Pair res;
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 11, 1, 15, 5, 2, 6, 10, 13, 25, 4, 21, 7);

        assertTrue(tree2.addAll(list));

        res = tree2.split(15);
        tree2.merge(res.left, res.right);
        tree2.clear();

        list.clear();
        Collections.addAll(list, 11, 13, 25, 4, 10, 1, 15, 5, 2, 6 ,21);

        assertTrue(tree2.addAll(list));

        res = tree2.split(6);
        tree2.merge(res.left, res.right);
    }

    @Test
    public void testIterator() {
        tree1 = new SplayTree<>();
        tree2 = new SplayTree<>();
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 15, 2, 5, 1, 3, 10);

        assertTrue(tree1.addAll(list));

        list.clear();
        Collections.addAll(list, 1, 15, 3, 5, 10, 2);

        assertTrue(tree2.addAll(list));

        assertEquals(tree1.size(), tree2.size());

        Iterator<Integer> iterator1 = tree1.iterator();
        List<Integer> check1 = new ArrayList<>();
        Integer c1;
        Iterator<Integer> iterator2 = tree2.iterator();
        List<Integer> check2 = new ArrayList<>();
        Integer c2;

        while (iterator1.hasNext() && iterator2.hasNext()) {
            c1 = iterator1.next();
            c2 = iterator2.next();
            check1.add(c1);
            check2.add(c2);
        }
        assertEquals(tree1, tree2);
        assertEquals(check1, check2);
    }
}