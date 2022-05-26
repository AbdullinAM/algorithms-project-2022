import org.testng.annotations.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;


public class Tests {

    @Test
    public void addNodes() {
        SkipList<Integer> skipList = new SkipList<>();
        SortedSet<Integer> set = new TreeSet<>();

        set.add(6);
        set.add(15);
        set.add(17);
        set.add(3);
        set.add(21);
        set.add(44);

        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);

        assertEquals(skipList.getKeys(), set);
        assertEquals(skipList.size(), set.size());

    }




    @Test
    public void subs() {
        SkipList<Integer> skipList = new SkipList<>();
        SortedSet<Integer> set = new TreeSet<>();

        set.add(13);
        set.add(6);
        set.add(2);
        set.add(17);
        set.add(21);
        set.add(5);
        set.add(55);

        skipList.skipInsert(13);
        skipList.skipInsert(6);
        skipList.skipInsert(2);
        skipList.skipInsert(17);
        skipList.skipInsert(21);
        skipList.skipInsert(5);
        skipList.skipInsert(55);


        assertEquals(skipList.subSet(6, 21), set.subSet(6, 21));
        assertEquals(skipList.tailSet(18), set.tailSet(19));
        assertEquals(skipList.headSet(6), set.headSet(6));

//        System.out.println(skipList.subSet(6, 21));
//        System.out.println(set.subSet(6, 21));
//        System.out.println(skipList.tailSet(18));
//        System.out.println(set.tailSet(18));
//        System.out.println(skipList.headSet(6));
//        System.out.println(set.headSet(6));
//        System.out.println(skipList.search(7));

    }

    @Test
    public void firstAndLast() {
        SkipList<Integer> skipList = new SkipList<>();
        SortedSet<Integer> set = new TreeSet<>();

        set.add(6);
        set.add(15);
        set.add(17);
        set.add(3);
        set.add(21);
        set.add(44);
        set.add(12);

        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);
        skipList.skipInsert(12);

        assertEquals(set.first(), skipList.first());
        assertEquals(set.last(), skipList.last());

        skipList.delete(3);
        skipList.delete(44);
        assertNotEquals(set.first(), skipList.first());

    }


    @Test
    public void removeNodes() {
        SkipList<Integer> skipList = new SkipList<>();
        SortedSet<Integer> set = new TreeSet<>();

        set.add(6);
        set.add(15);
        set.add(17);
        set.add(3);
        set.add(21);
        set.add(44);
        set.add(12);

        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);
        skipList.skipInsert(12);
        skipList.skipInsert(4); // отличающийся элемент

        skipList.delete(4);

        //skipList.levelPrint();

//        System.out.println(skipList.getKeys());
//        System.out.println(skipList.getKeys().size());
//        System.out.println(set.size());

        assertEquals(set, skipList.getKeys());
        assertEquals(skipList.getKeys().size(), set.size());

//        skipList.remove(15);
//        skipList.remove(17);
//        skipList.remove(12);

        //skipList.levelPrint();

//        assertEquals(skipList.size(), 1);
//        assertNotEquals(skipList.size(), 5);
//        assertNotEquals(skipList, set);
    }



    @Test
    public void insertDuplicates() {
        SkipList<Integer> skipList = new SkipList<>();
        SortedSet<Integer> set = new TreeSet<>();

        set.add(6);
        set.add(15);
        set.add(17);
        set.add(3);
        set.add(21);
        set.add(44);

        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);
        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);
        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);
        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);


        //skipList.levelPrint();
        assertEquals(skipList.getKeys(), set);
        assertEquals(skipList.size(), set.size());
    }

    @Test
    public void manyNodes() {
        SkipList<Integer> skipList = new SkipList<>();
        SortedSet<Integer> set = new TreeSet<>();

        skipList.skipInsert(6);
        skipList.skipInsert(15);
        skipList.skipInsert(17);
        skipList.skipInsert(3);
        skipList.skipInsert(21);
        skipList.skipInsert(44);
        skipList.skipInsert(12);
        skipList.skipInsert(63);
        skipList.skipInsert(153);
        skipList.skipInsert(173);
        skipList.skipInsert(43);
        skipList.skipInsert(33);
        skipList.skipInsert(213);
        skipList.skipInsert(443);
        skipList.skipInsert(123);
        skipList.skipInsert(65);
        skipList.skipInsert(155);
        skipList.skipInsert(175);
        skipList.skipInsert(45);
        skipList.skipInsert(35);
        skipList.skipInsert(215);
        skipList.skipInsert(445);
        skipList.skipInsert(125);
        skipList.skipInsert(60);
        skipList.skipInsert(150);
        skipList.skipInsert(170);
        skipList.skipInsert(40);
        skipList.skipInsert(30);
        skipList.skipInsert(210);
        skipList.skipInsert(440);
        skipList.skipInsert(120);
        //skipList.levelPrint();

        assertEquals(skipList.size(), 31);
    }

}
