package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tree.SplayTree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class SplayTreeTest {

    @Test
    public void size() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        //Тест, что 2 когда добавили 2
        Assertions.assertEquals(2, splayTree.size());
        splayTree.add(25);
        //Тест, что размер увеличился на 1 при добавлении нового элемента
        Assertions.assertEquals(3, splayTree.size());
        splayTree.add(25);
        //Тест, что не увеличилось при добавлении элемента который уже был в коллекции
        Assertions.assertEquals(3, splayTree.size());
        //Тест, что 0 когда все удалил
        splayTree.remove(10);
        splayTree.remove(15);
        splayTree.remove(25);
        Assertions.assertEquals(0, splayTree.size());
    }

    @Test
    public void isEmpty() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        //Тест, что коллекция не пустая, когда там есть элементы
        Assertions.assertFalse(splayTree.isEmpty());
        //Тест, что коллекция пустая, когда все удалил
        splayTree.remove(10);
        splayTree.remove(15);
        splayTree.remove(25);
        Assertions.assertTrue(splayTree.isEmpty());
    }

    @Test
    public void contains() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        //Тест, что коллекция содержит элемент который в ней есть
        Assertions.assertTrue(splayTree.contains(25));
        Assertions.assertTrue(splayTree.contains(15));
        Assertions.assertTrue(splayTree.contains(10));
        //Тест, что коллекция не содержит элемент, который был удален
        splayTree.remove(10);
        Assertions.assertFalse(splayTree.contains(10));
    }

    @Test
    public void add() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        //Тест, что было добавлено 3 элементов
        Assertions.assertEquals(3, splayTree.size());
        Assertions.assertTrue(splayTree.contains(25));
        Assertions.assertTrue(splayTree.contains(15));
        Assertions.assertTrue(splayTree.contains(10));
    }

    @Test
    public void remove() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        //Тест, что не было 3 элементов
        Assertions.assertEquals(3, splayTree.size());
        splayTree.remove(25);
        //Тест, что remove работает корректно
        Assertions.assertFalse(splayTree.contains(25));
        splayTree.remove(15);
        splayTree.remove(10);
        //Тест, что было удаленно все
        Assertions.assertEquals(0, splayTree.size());
        //Тест, попытка удалить когда пусто
        splayTree.remove(10);
        Assertions.assertEquals(0, splayTree.size());
    }

    @Test
    public void iterator() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        splayTree.add(35);
        splayTree.add(5);
        splayTree.add(2);
        Set<Integer> set = new HashSet<>();
        Iterator<Integer> iterator = splayTree.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            set.add(num);
            count++;
        }
        //Тест, что прошлось по всем элементам
        Assertions.assertEquals(count, splayTree.size());
        Assertions.assertTrue(set.containsAll(splayTree));
        //Тест, что все удалилось
        iterator = splayTree.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        Assertions.assertEquals(0, splayTree.size());
        Assertions.assertTrue(splayTree.isEmpty());
        //Тесты на исключения в iterator
        Iterator<Integer> finalIterator = iterator;
        //Тест на исключение в next
        Assertions.assertThrows(NoSuchElementException.class, finalIterator::next);
        //Тест на исключение в remove
        Assertions.assertThrows(IllegalStateException.class, finalIterator::remove);
    }

    @Test
    public void toArray() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        splayTree.add(35);
        splayTree.add(5);
        splayTree.add(2);

        Object[] array = splayTree.toArray();
        int count = 0;
        for (Object temp : array) {
            //Тест, что элементы массива находятся в дереве
            Assertions.assertTrue(splayTree.contains(temp));
            count++;
        }
        //Тест, что не было пропущено элементов
        Assertions.assertEquals(count, splayTree.size());
    }

    @Test
    public void addAll() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(35);
        splayTree.add(5);
        splayTree.add(2);
        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(15);
        set.add(25);

        //Тест, что все добавилось
        splayTree.addAll(set);
        Assertions.assertEquals(6, splayTree.size());
        Assertions.assertTrue(splayTree.containsAll(set));
    }

    @Test
    public void clear() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(125);
        Assertions.assertEquals(3, splayTree.size());
        splayTree.clear();
        //Тест, после очистки размер должен быть равен 0
        Assertions.assertEquals(0, splayTree.size());
        //Тест, считаем через for-each сколько элементов в дереве, и если там 0, то все работает корректно
        int count = 0;
        for (Integer integer : splayTree) {
            count++;
        }
        Assertions.assertEquals(0, count);
    }

    @Test
    public void removeAll() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        splayTree.add(35);
        splayTree.add(5);
        splayTree.add(2);
        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(15);
        set.add(25);

        splayTree.removeAll(set);
        //Тест, что размер уменьшился при удалении
        Assertions.assertEquals(3, splayTree.size());
        //Тест, что удаляемые элементы точно не содержаться в дереве
        for (Integer temp : set) {
            Assertions.assertFalse(splayTree.contains(temp));
        }
        //Тест, что передается null
        Assertions.assertFalse(splayTree.removeAll(null));
        //Тест, что пытаемся удалить то его нет и все норм работает
        splayTree.removeAll(set);
        Assertions.assertEquals(3, splayTree.size());
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(2);
        splayTree1.add(5);
        splayTree1.add(6);
        splayTree1.add(7);
        splayTree1.add(9);
        Set<Integer> set1 = new HashSet<>();
        set1.add(4);
        set1.add(5);
        set1.add(7);
        set1.add(8);
        set1.add(9);

        //Тест на проверку, что разность множеств работает корректно
        //U={2,5,6,7,9}
        //A={4,5,7,8,9}
        //U\A={2,6}
        splayTree1.removeAll(set1);
        Assertions.assertTrue(splayTree1.contains(2));
        Assertions.assertTrue(splayTree1.contains(6));
        Assertions.assertEquals(2, splayTree1.size());
    }

    @Test
    public void retainAll() {
        SplayTree<Integer> splayTree1 = new SplayTree<>();
        splayTree1.add(0);
        splayTree1.add(1);
        splayTree1.add(2);
        splayTree1.add(3);
        splayTree1.add(4);
        splayTree1.add(5);
        splayTree1.add(6);
        splayTree1.add(7);
        splayTree1.add(8);
        splayTree1.add(9);
        Set<Integer> set1 = new HashSet<>();
        set1.add(2);
        set1.add(4);
        set1.add(6);
        set1.add(8);
        set1.add(10);

        //Тест на проверку, что пересечение множеств работает корректно
        //U={1,2,3,4,5,6,7,8,9}
        //A={2,4,6,8,10}
        //U^A={2,4,6,8}
        splayTree1.retainAll(set1);
        Assertions.assertTrue(splayTree1.contains(2));
        Assertions.assertTrue(splayTree1.contains(4));
        Assertions.assertTrue(splayTree1.contains(6));
        Assertions.assertTrue(splayTree1.contains(8));
        Assertions.assertEquals(4, splayTree1.size());
    }


    @Test
    public void containsAll() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        splayTree.add(35);
        splayTree.add(5);
        splayTree.add(2);
        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(15);
        set.add(25);

        //Тест на то что элементы из set входят в дерево
        Assertions.assertTrue(splayTree.containsAll(set));
        //Тест на то что set имеет больше элементов чем дерево
        set.add(10);
        set.add(152);
        set.add(252);
        set.add(101);
        set.add(15);
        set.add(25);
        set.add(105);
        Assertions.assertFalse(splayTree.containsAll(set));
        //Тест на то что один из элементов set не входит в дерево
        set.clear();
        set.add(10);
        set.add(15);
        set.add(2051);
        Assertions.assertFalse(splayTree.containsAll(set));
    }

    @Test
    public void toArrayParamArray() {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.add(10);
        splayTree.add(15);
        splayTree.add(25);
        splayTree.add(152);
        Integer[] testArr = new Integer[5];
        //Тест, что скопировалось и расширилось и последний элемент null так как у нас было всего 5 элементов в коллекции
        Object[] res = splayTree.toArray(testArr);
        Assertions.assertEquals(5, res.length);
        Assertions.assertNull(res[4]);
        //Тест, что скопировалось с правильным размером
        splayTree.remove(10);
        testArr = new Integer[3];
        res = splayTree.toArray(testArr);
        Assertions.assertEquals(3, res.length);
    }
}

