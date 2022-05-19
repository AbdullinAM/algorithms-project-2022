import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
public class BloomTests {
    @Test
    void test() {
        int numOfExpElements = 40000;
        int size = 100000;
        BloomFilter myFilter = new BloomFilter(size, numOfExpElements);
        String arr[] = new String[numOfExpElements];
        for (int i = 0;i<numOfExpElements;i++){
            String a = RandomStringUtils.random(20,true,true);
            myFilter.add(a);
            arr[i] = a;
        }
        for (int i = 0;i<numOfExpElements;i++){
            Assertions.assertTrue(myFilter.contains(arr[i]));
        }
        Assertions.assertEquals(size,myFilter.size());
        myFilter.clear();
        Assertions.assertTrue(myFilter.isEmpty());
    }
    @Test
    void collectionTest() {
        int numOfExpElements = 40000;
        int size = 100000;
        BloomFilter myFilter = new BloomFilter(size, numOfExpElements);
        String arr[] = new String[numOfExpElements];
        for (int i = 0;i<numOfExpElements;i++){
            String a = RandomStringUtils.random(20,true,true);
            arr[i] = a;
        }
        myFilter.addAll(List.of(arr));
        Assertions.assertTrue(myFilter.containsAll(List.of(arr)));

    }


   @Test
    void removeTest() {
        int numOfExpElements = 20000;
        int size = 100000;
        BloomFilter myFilter = new BloomFilter(size, numOfExpElements);

        String arr[] = new String[numOfExpElements];
        for (int i = 0;i<numOfExpElements;i++){
            String a = RandomStringUtils.random(20,true,true);
            myFilter.add(a);
            arr[i] = a;
        }
        int fails = 0;
        for (int i = numOfExpElements;i<numOfExpElements;i++){
            myFilter.remove(arr[i]);
        }
        for (int i = numOfExpElements;i<numOfExpElements;i++){
            if(myFilter.contains(arr[i])) fails++;
        }
        Assertions.assertTrue(fails<1);
    }
    @Test
    void removeCollectionTest() {
        int numOfExpElements = 20000;
        int size = 100000;
        BloomFilter myFilter = new BloomFilter(size, numOfExpElements);

        String arr[] = new String[numOfExpElements];
        String arr2[] = new String[numOfExpElements/2];
        String arr3[] = new String[numOfExpElements/2];
        for (int i = 0;i<numOfExpElements;i++){
            String a = RandomStringUtils.random(20,true,true);
            myFilter.add(a);
            arr[i] = a;
            if(i<numOfExpElements/2) arr2[i] = a; else arr3[i-numOfExpElements/2] = a;
        }
        int fails = 0;
        myFilter.removeAll(List.of(arr2));
        myFilter.retainAll(List.of(arr3));
        for (int i = 0;i<numOfExpElements/2;i++){
            if(!myFilter.contains(arr3[i])) fails++;
        }
        System.out.println(fails);
        Assertions.assertTrue(fails<1);



    }



}


