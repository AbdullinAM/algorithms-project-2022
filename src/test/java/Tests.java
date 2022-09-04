import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Tests {

    @Test
    public void insert0() {
        final SkipList<Integer> patient = new SkipList<>();
        assertEquals(0, patient.size());
    }

    @Test
    public void insert1() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        assertTrue(patient.contains(1));
        assertEquals(1, patient.size());
    }

    @Test
    public void insert2() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        patient.insert(2);
        assertTrue(patient.contains(1));
        assertTrue(patient.contains(2));
        assertEquals(2, patient.size());
    }

    @Test
    public void insert3() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        patient.insert(2);
        patient.insert(3);
        assertTrue(patient.contains(1));
        assertTrue(patient.contains(2));
        assertTrue(patient.contains(3));
        assertEquals(3, patient.size());
    }

    @Test
    public void insert4() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        patient.insert(2);
        patient.insert(3);
        patient.insert(4);
        assertTrue(patient.contains(1));
        assertTrue(patient.contains(2));
        assertTrue(patient.contains(3));
        assertTrue(patient.contains(4));
        assertEquals(4, patient.size());
    }

    @Test
    public void insert5() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        patient.insert(2);
        patient.insert(3);
        patient.insert(4);
        patient.insert(5);
        assertTrue(patient.contains(1));
        assertTrue(patient.contains(2));
        assertTrue(patient.contains(3));
        assertTrue(patient.contains(4));
        assertTrue(patient.contains(5));
        assertEquals(5, patient.size());
    }

    @Test
    public void insert10() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(10);
        patient.insert(9);
        patient.insert(8);
        patient.insert(7);
        patient.insert(6);
        patient.insert(5);
        patient.insert(4);
        patient.insert(3);
        patient.insert(2);
        patient.insert(1);
        assertEquals("1 <-> 2 <-> 3 <-> 4 <-> 5 <-> 6 <-> 7 <-> 8 <-> 9 <-> 10\n", patient.toString());
    }

    @Test
    public void delete1() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        assertTrue(patient.delete(1));
        assertFalse(patient.contains(1));
        assertEquals(0, patient.size());
    }

    @Test
    public void delete2() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        assertFalse(patient.delete(10));
        assertTrue(patient.contains(1));
        assertEquals(1, patient.size());
    }

    @Test
    public void delete3() {
        final SkipList<Integer> patient = new SkipList<>();
        patient.insert(1);
        patient.insert(2);
        assertTrue(patient.delete(1));
        assertFalse(patient.contains(1));
        assertEquals(2, patient.first());
        assertEquals(1, patient.size());
    }

}
