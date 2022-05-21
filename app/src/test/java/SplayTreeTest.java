import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class SplayTreeTest {

    @Test
    public void commonTest() {
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(1);
        assertEquals(splayTree1.getRootValue(), 1);
        splayTree1.add(5);
        assertEquals(splayTree1.getRootValue(), 5);
        splayTree1.add(10);
        assertEquals(splayTree1.getRootValue(), 10);
        splayTree1.add(3);
        assertEquals(splayTree1.getRootValue(), 3);
        splayTree1.add(4);
        assertEquals(splayTree1.getRootValue(), 4);
        splayTree1.add(11);
        assertEquals(splayTree1.getRootValue(), 11);
        assertEquals(splayTree1.size(), 6);

        assertTrue(splayTree1.contains(3));
        assertEquals(splayTree1.getRootValue(), 3);

        splayTree1.remove(3);
        assertEquals(splayTree1.getRootValue(), 1);
        splayTree1.remove(10);
        assertEquals(splayTree1.getRootValue(), 5);

        SplayTree<String> splayTree2 = new SplayTree<>();
        splayTree2.add("Ivan");
        splayTree2.add("Vladimir");
        splayTree2.add("Julie");
        splayTree2.add("Michael");
        splayTree2.add("Timur");
        splayTree2.add("Elizaveta");
        assertEquals(splayTree2.size(), 6);
        assertEquals(splayTree2.getRootValue(), "Elizaveta");
        splayTree2.remove("Timur");
        assertEquals(splayTree2.getRootValue(), "Michael");

        splayTree2.add("Timur");
        Set<String> set1 = new HashSet<>();
        set1.add("Elizaveta");
        set1.add("Vladimir");
        set1.add("Michael");
        splayTree2.removeAll(set1);
        assertEquals(splayTree2.size(), 3);
        assertFalse(splayTree2.contains("Michael"));
    }

    @Test
    public void addTest() {
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(20);
        splayTree1.add(3);
        splayTree1.add(11);
        splayTree1.add(7);
        splayTree1.add(4);
        splayTree1.add(8);
        splayTree1.add(13);
        splayTree1.add(6);
        assertEquals(splayTree1.size(), 8);
        assertEquals(splayTree1.getRootValue(), 6);
        splayTree1.clear();
        assertEquals(splayTree1.size(), 0);

        splayTree1.add(11);
        splayTree1.add(7);
        splayTree1.add(4);
        splayTree1.add(8);
        assertEquals(splayTree1.size(), 4);

        Set<Integer> set1 = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            set1.add(i);
        }
        Set<Integer> set2 = new HashSet<>();
        for (int i = 101; i < 200; i++) {
            set2.add(i);
        }

        splayTree1.addAll(set1);
        assertEquals(splayTree1.size(), 100);

        for (Integer element : set1) {
            assertTrue(splayTree1.contains(element));
            assertFalse(splayTree1.add(element));
        }
        for (Integer element : set2) {
            assertFalse(splayTree1.contains(element));
        }
    }

    @Test
    public void removeTest() {
        Set<Double> splayTree1 = new SplayTree<>();
        splayTree1.add(20.0);
        splayTree1.add(3.5);
        splayTree1.add(11.8);
        splayTree1.add(7.45);
        splayTree1.add(4.9);
        splayTree1.add(8.78);
        splayTree1.add(13.5);
        splayTree1.add(6.3);
        assertEquals(splayTree1.size(), 8);

        splayTree1.remove(11.8);
        splayTree1.remove(4.9);
        splayTree1.remove(6.3);
        assertEquals(splayTree1.size(), 5);

        Set<Double> set1 = new HashSet<>();
        for (int i = 0; i < 100.0; i++) {
            set1.add((double) i);
        }
        Set<Double> set2 = new HashSet<>();
        for (int i = 101; i < 200; i++) {
            set2.add((double) i);
        }
        splayTree1.addAll(set1);
        splayTree1.addAll(set2);
        assertEquals(splayTree1.size(), 203);
        for (Double element : set2) {
            assertTrue(splayTree1.contains(element));
        }
        splayTree1.removeAll(set1);
        assertEquals(splayTree1.size(), 103);

        splayTree1.addAll(set1);
        splayTree1.retainAll(set1);
        for (Double element : set2) {
            assertFalse(splayTree1.contains(element));
        }
        assertTrue(splayTree1.containsAll(set1));
    }

    @Test
    public void splayTest() {
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(20);
        splayTree1.add(3);
        splayTree1.add(11);
        splayTree1.add(7);
        splayTree1.add(4);
        splayTree1.add(8);
        splayTree1.add(13);
        splayTree1.add(6);

        assertEquals(splayTree1.getRootValue(), 6);
        splayTree1.root = splayTree1.splay(splayTree1.root, 7);
        assertEquals(splayTree1.getRootValue(), 7);
        splayTree1.root = splayTree1.splay(splayTree1.root, 11);
        assertEquals(splayTree1.getRootValue(), 11);
    }

    @Test
    public void iteratorTest() {
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(20);
        splayTree1.add(3);
        splayTree1.add(11);
        splayTree1.add(7);
        splayTree1.add(4);
        splayTree1.add(8);
        splayTree1.add(13);
        splayTree1.add(6);

        Set<Integer> set1 = new HashSet<>();
        Set<Integer> treeSet = new TreeSet<>(splayTree1);
        Iterator<Integer> iterator = splayTree1.iterator();
        while (iterator.hasNext()) {
            set1.add(iterator.next());
        }

        assertEquals(set1, treeSet);
        assertEquals(set1, splayTree1);
        assertTrue(splayTree1.containsAll(set1));

        Set<Integer> set2 = new HashSet<>();
        Iterator<Integer> iterator2 = splayTree1.iterator();
        while (iterator2.hasNext()) {
            set2.add(iterator2.next());
            iterator2.remove();
        }
        assertEquals(splayTree1.size(), 0);
        assertTrue(splayTree1.isEmpty());
        splayTree1.addAll(set2);
        assertEquals(splayTree1.size(), 8);
        splayTree1.clear();
        assertEquals(splayTree1.size(), 0);
    }

    @Test
    public void toArrayTest() {
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(20);
        splayTree1.add(3);
        splayTree1.add(11);
        splayTree1.add(7);
        splayTree1.add(4);
        splayTree1.add(8);
        splayTree1.add(13);
        splayTree1.add(6);

        Object[] array1 = splayTree1.toArray();
        for (Object o : array1) {
            assertTrue(splayTree1.contains(o));
        }

        Object[] array2 = new Object[8];
        splayTree1.toArray(array2);
        for (Object o : array2) {
            assertTrue((splayTree1.contains(o)));
        }
    }
}
