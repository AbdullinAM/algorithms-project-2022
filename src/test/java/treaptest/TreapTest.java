package treaptest;

import org.junit.Test;
import treap.Treap;

import java.util.*;

import static org.junit.Assert.*;

public class TreapTest {

    @Test
    public void addtest() {
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000, 0, 1000).toArray();
        for (double i : rand) {
            treap.add(i);
        }
        for (double i : rand) {
            assertNotEquals(treap.find(i), null);
        }
    }

    @Test
    public void deletetest() {
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000, 0, 1000).toArray();
        for (double i : rand) {
            treap.add(i);
        }
        assertEquals(1000, treap.size());
        int[] toDelete = new Random().ints(50, 0, 1000).toArray();
        ArrayList<Double> deleted = new ArrayList<>();
        HashSet<Integer> deletedIndexes = new HashSet<>();
        for (int i : toDelete) {
            Double toRemove = rand[i];
            deleted.add(toRemove);
            treap.remove(toRemove);
            deletedIndexes.add(i);
        }
        for (double i : deleted) {
            assertFalse(treap.contains(i));
        }
        for (int i = 0; i < 1000; i++) {
            if (!deletedIndexes.contains(i))
                assertTrue(treap.contains(rand[i]));
        }
        HashSet<Treap.Node> hashSet = new HashSet<>();
        hashSet.add(treap.root);
        ScanLevels(hashSet);
    }

    @Test
    public void deleteRootTest() {
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000, 0, 1000).toArray();
        HashSet<Double> deleted = new HashSet<>();
        for (double i : rand) {
            treap.add(i);
        }
        for (int i = 0; i < 100; i++) {
            double oldRoot = treap.root.x;
            deleted.add(oldRoot);
            treap.remove(treap.root.x);
            assertNotEquals(treap.root.x, oldRoot);
        }
        for (Double node : rand) {
            if (!deleted.contains(node))
                assertTrue(treap.contains(node));
            else
                assertFalse(treap.contains(node));
        }
    }

    public HashSet<Treap.Node> ScanLevels(HashSet<Treap.Node> nodes) {
        HashSet<Treap.Node> result = new HashSet<>();
        for (Treap.Node node : nodes) {
            if (node.right != null) {
                result.add(node.right);
                assertTrue(node.right.y <= node.y);
            }
            if (node.left != null) {
                result.add(node.left);
                assertTrue(node.left.y <= node.y);
            }
            if (node.left != null && node.right != null) {
                assertTrue((Double) node.left.x <= (Double) node.right.x);
                assertTrue((Double) node.left.x <= (Double) node.x);
                assertTrue((Double) node.right.x >= (Double) node.x);
            }
        }
        if (!result.isEmpty()) {
            ScanLevels(result);
        }
        return result;
    }

    @Test
    public void isTreap() {
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000, 0, 1000).toArray();
        for (double i : rand) {
            treap.add(i);
        }
        HashSet<Treap.Node> hashSet = new HashSet<>();
        hashSet.add(treap.root);
        ScanLevels(hashSet);
    }

    @Test
    public void iterator() {
        double[] rand = new Random().doubles(1000, 0, 1000).toArray();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            HashSet<Integer> controlSet = new HashSet<>();
            for (int j = 0; j < 100; j++) {
                controlSet.add(random.nextInt(100));
            }
            Treap<Double> treap = new Treap<>();
            assertFalse(treap.iterator().hasNext());
            for (Integer element : controlSet) {
                treap.add(element);
            }
            Iterator iterator1 = treap.iterator();
            Iterator iterator2 = treap.iterator();
            while (iterator1.hasNext()) {
                assertEquals(iterator2.next(), iterator1.next());
            }
            Iterator controlIter = controlSet.iterator();
            Iterator binaryIter = treap.iterator();
            while (controlIter.hasNext()) {
                assertEquals(controlIter.next(), binaryIter.next());
            }
            boolean test = false;
            try {
                binaryIter.next();
            } catch (NoSuchElementException e) {
                test = true;
            }
            assertTrue(test);
        }
    }

    @Test
    public void other() {
        Treap<Double> treap = new Treap<>();
        assertTrue(treap.isEmpty());
        double[] rand = new Random().doubles(1000, 0, 1000).toArray();
        ArrayList<Double> array = new ArrayList<>();
        for (Double element : rand) {
            array.add(element);
        }
        treap.addAll(array);
        for (Double elements : array) {
            assertTrue(treap.contains(elements));
        }
        Object[] comparison = treap.stream().toArray();
        for (Object element : comparison) {
            assertTrue(treap.contains(element));
        }
        assertTrue(treap.containsAll(array));
        assertFalse(treap.isEmpty());
        treap.removeAll(array);
        for (Double elements : array) {
            assertFalse(treap.contains(elements));
        }
        assertTrue(treap.isEmpty());
        assertEquals(0, treap.size());
        HashSet<Double> hashSet = new HashSet<>();
        double[] ran = new Random().doubles(100, 0, 1000).toArray();
        HashSet<Double> toRetain = new HashSet<>();
        for (Double element : rand) {
            hashSet.add(element);
        }
        for (Double element : ran) {
            toRetain.add(element);
        }
        treap.addAll(hashSet);
        treap.retainAll(toRetain);
        hashSet.removeAll(toRetain);
        for (Double element : hashSet) {
            assertFalse(treap.contains(element));
        }
    }
}
