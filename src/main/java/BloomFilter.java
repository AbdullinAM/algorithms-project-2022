import java.util.*;
import org.apache.commons.lang3.StringUtils;
import static java.lang.Math.log;
import static java.lang.Math.log10;

public class BloomFilter implements Set<Object> {
    private final int size;
    private final int hashNum;
    private int hashes[];

    public BloomFilter(int size, int numberOfExpectedElements){
        this.size = size;
        hashNum = (int) Math.ceil(size/numberOfExpectedElements*log(2));
        hashes = new int[size];
    }

    public boolean add(Object o) {
        for (int i = 0; i < hashNum; i++){
            hashes[hash(o,i)]++;
        }
        return true;
    }

    public boolean addAll(Collection<? extends Object> c) {
        for (Object o: c){
            this.add(o);
        }
        return true;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        for(int i = 0; i < size; i++){
            if (hashes[i] > 0) return false;
        }
        return true;
    }

    public boolean contains(Object o) {
        int collisions = 0;
        for (int i = 0; i < hashNum; i++){
            if (hashes[hash(o,i)] > 0) collisions++;
        }
        return collisions == hashNum;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o:c){
            if(!this.contains(o)) return false;
        }
        return true;
    }

    public void clear() {
        for(int i = 0; i < size; i++){
            hashes[i] = 0;
        }
    }

    private int hash(Object o, int NumberOfHashFunction){
        return Integer.parseInt(StringUtils.left(String.valueOf(Math.abs(Objects.hashCode
                (Objects.hashCode(o)+ (NumberOfHashFunction)))),(int)Math.floor(log10(size))));
        //укорачиваю хэшкоды до log10(size) символов, чтобы они поместились в массив размером size
    }

    public boolean remove(Object o) {
        if(!this.contains(o)) return false;
        for (int i = 0; i < hashNum; i++){
            hashes[hash(o,i)]--;
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        for (Object o:c){
            this.remove(o);
        }
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        this.clear();
        this.addAll(c);
        return true;
    }

    public Iterator<Object> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }
}
