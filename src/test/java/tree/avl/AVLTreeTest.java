package tree.avl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class AVLTreeTest {

    @Test
    void iterator() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        Set<Integer> set = new HashSet<>();
        for (Integer it : avlTree) {
            set.add(it);
        }
        Assertions.assertTrue(avlTree.containsAll(set));
        Assertions.assertEquals(avlTree.size(), set.size());
    }

    @Test
    void subSet() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        avlTree.add(1100);
        avlTree.add(15522);
        avlTree.add(1552);
        avlTree.add(110);
        avlTree.add(1526);
        //110,155,1100,1526 (1552 не вошло так как toElement должен быть строго больше)
        SortedSet<Integer> sub = avlTree.subSet(110, 1552);
        Assertions.assertEquals(4, sub.size());
        Assertions.assertTrue(avlTree.containsAll(sub));
    }

    @Test
    void headSet() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        avlTree.add(1100);
        avlTree.add(15522);
        avlTree.add(1552);
        avlTree.add(110);
        avlTree.add(1526);
        //5,15,11,110,155
        SortedSet<Integer> sub = avlTree.headSet(1100);
        Assertions.assertEquals(5, sub.size());
        Assertions.assertTrue(avlTree.containsAll(sub));
    }

    @Test
    void tailSet() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        avlTree.add(1100);
        avlTree.add(15522);
        avlTree.add(1552);
        avlTree.add(110);
        avlTree.add(1526);
        //15522,1552,1526
        SortedSet<Integer> sub = avlTree.tailSet(1100);
        Assertions.assertEquals(3, sub.size());
        Assertions.assertTrue(avlTree.containsAll(sub));
    }

    @Test
    void toArray() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        Object[] arr = avlTree.toArray();
        for (Object it : arr) {
            Assertions.assertTrue(avlTree.contains(it));
        }
        Assertions.assertEquals(avlTree.size(), arr.length);
    }

    @Test
    void testToArray() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        Integer[] arr = new Integer[4];
        for (Integer it : avlTree.toArray(arr)) {
            Assertions.assertTrue(avlTree.contains(it));
        }
        arr = new Integer[6];
        int count = 0;
        for (Integer it : avlTree.toArray(arr)) {
            if(count > 3){
                Assertions.assertNull(arr[count]);
                continue;
            }
            Assertions.assertTrue(avlTree.contains(it));
            count++;
        }
    }

    @Test
    void addRemoveIsEmptyClearContainsSizeLastFirst() {
        //add
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(5);
        avlTree.add(155);
        Assertions.assertTrue(avlTree.contains(5));
        Assertions.assertTrue(avlTree.contains(15));
        Assertions.assertTrue(avlTree.contains(11));
        Assertions.assertTrue(avlTree.contains(155));
        Assertions.assertFalse(avlTree.add(155));
        //last
        Assertions.assertEquals(155, (int) avlTree.last());
        //first
        Assertions.assertEquals(5, (int) avlTree.first());
        //size
        Assertions.assertEquals(4, avlTree.size());
        //isEmpty
        Assertions.assertFalse(avlTree.isEmpty());
        //remove
        avlTree.remove(5);
        avlTree.remove(15);
        avlTree.remove(11);
        avlTree.remove(155);
        Assertions.assertFalse(avlTree.contains(5));
        Assertions.assertFalse(avlTree.contains(15));
        Assertions.assertFalse(avlTree.contains(11));
        Assertions.assertFalse(avlTree.contains(155));
        //isEmpty
        Assertions.assertTrue(avlTree.isEmpty());
        avlTree.add(5);
        avlTree.add(15);
        //clear
        avlTree.clear();
        Assertions.assertTrue(avlTree.isEmpty());
        //size
        Assertions.assertEquals(0, avlTree.size());
    }

    @Test
    void containsAll() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        avlTree.add(1100);
        avlTree.add(15522);
        avlTree.add(1552);
        avlTree.add(110);
        avlTree.add(1526);
        List<Integer> list = new ArrayList<>();
        list.add(155);
        list.add(1100);
        list.add(15522);
        list.add(1552);
        Assertions.assertTrue(avlTree.containsAll(list));
        list.add(155222);
        Assertions.assertFalse(avlTree.containsAll(list));
    }

    @Test
    void addAll() {
        List<Integer> list = new ArrayList<>();
        list.add(155);
        list.add(1100);
        list.add(15522);
        list.add(1552);
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.addAll(list);
        Assertions.assertTrue(avlTree.containsAll(list));
    }

    @Test
    void retainAll() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        avlTree.add(1100);
        List<Integer> list = new ArrayList<>();
        list.add(155);
        list.add(1100);
        avlTree.retainAll(list);
        for (Integer it : avlTree) {
            System.out.println(it);
        }
        Assertions.assertEquals(2, avlTree.size());
        Assertions.assertTrue(avlTree.contains(155));
        Assertions.assertTrue(avlTree.contains(1100));
    }

    @Test
    void removeAll() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(15);
        avlTree.add(11);
        avlTree.add(155);
        avlTree.add(1100);
        List<Integer> list = new ArrayList<>();
        list.add(155);
        list.add(1100);
        list.add(11);
        list.add(15);
        avlTree.removeAll(list);
        Assertions.assertFalse(avlTree.contains(155));
        Assertions.assertFalse(avlTree.contains(11));
        Assertions.assertFalse(avlTree.contains(1100));
        Assertions.assertFalse(avlTree.contains(15));
        Assertions.assertTrue(avlTree.contains(5));
    }
}
