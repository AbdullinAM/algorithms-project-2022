package treaptest;
import org.junit.Test;
import treap.Treap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.*;
public class TreapTest {

    @Test
    public void addtest() {
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000,0,1000).toArray();
        for (double i: rand) {
            treap.add(i);
        }
        for (double i: rand) {
            assertNotEquals(treap.find(i), null);
        }
    }

    @Test
    public void deletetest(){
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000,0,1000).toArray();
        for (double i: rand) {
            treap.add(i);
        }
        int[] toDelete = new Random().ints(50,0,1000).toArray();
        ArrayList<Double>deleted = new ArrayList<>();
        HashSet<Integer>deletedIndexes= new HashSet<>();
        for (int i: toDelete) {
            Double toRemove=rand[i];
            deleted.add(toRemove);
           treap.remove(treap.root,toRemove);
           deletedIndexes.add(i);
        }
        for (double i: deleted) {
            assertNull(treap.find(i));
        }
        for (int i=0; i<1000; i++) {
            if (!deletedIndexes.contains(i))
                assertNotNull(treap.find(rand[i]));
        }
        double oldRoot = treap.root.x;
        treap.remove(treap.root,treap.root.x);
        assertNotEquals(treap.root.x,oldRoot);
        HashSet<Treap.Node> hashSet = new HashSet<>();
        hashSet.add(treap.root);
        ScanLevels(hashSet);
    }

    public HashSet<Treap.Node> ScanLevels (HashSet<Treap.Node> nodes) {
        HashSet<Treap.Node> result = new HashSet<>();
        for (Treap.Node node: nodes) {
            if (node.right != null) {
                result.add(node.right);
                assertTrue(node.right.y<=node.y);
            }
            if (node.left != null) {
                result.add(node.left);
                assertTrue(node.left.y<=node.y);
            }
            if (node.left != null && node.right != null) {
                assertTrue((Double) node.left.x<= (Double) node.right.x);
                assertTrue((Double) node.left.x<= (Double) node.x);
                assertTrue((Double) node.right.x>= (Double) node.x);
            }
        }
        if (!result.isEmpty()) {
            ScanLevels(result);
        }
        return result;
    }

    @Test
    public void isTreap(){
        Treap<Double> treap = new Treap<>();
        double[] rand = new Random().doubles(1000,0,1000).toArray();
        for (double i: rand) {
            treap.add(i);
        }
        HashSet<Treap.Node> hashSet = new HashSet<>();
        hashSet.add(treap.root);
        ScanLevels(hashSet);
    }
}
