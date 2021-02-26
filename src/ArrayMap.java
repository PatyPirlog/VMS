import java.util.*;

public class ArrayMap<K, V>  extends AbstractMap {
    ArrayList<ArrayMapEntry> list;

    public ArrayMap() {
        list = new ArrayList<ArrayMapEntry>();
    }

    class ArrayMapEntry<K,V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public ArrayMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public ArrayMapEntry() {

        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }

        public String toString() {
            return "Key is " + this.key + " and value is " + this.value;
        }

        public boolean equals(Object obj) {
            if(!(obj instanceof ArrayMapEntry))
                return false;
            else {
                ArrayMapEntry<K, V> a1 = this;
                ArrayMapEntry<K, V> a2 = (ArrayMapEntry<K, V>) obj;
                return (a1.getKey() == null ?
                        a2.getKey() == null : a1.getKey().equals(a2.getKey())) &&
                        (a1.getValue() == null ?
                                a2.getValue() == null : a1.getValue().equals(a2.getValue()));

               }
        }

        public int hashCode() {
            return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^
                    (this.getValue() == null ? 0 : this.getValue().hashCode());
        }
    }

    public V put(Object key, Object value) {
        ArrayMapEntry<K, V> obj = new ArrayMapEntry<>();
        obj.key = (K) key;
        obj.value = (V) value;
        list.add(obj);
        return (V)obj;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set s = new HashSet();
        s.addAll(list);
        return s;
    }

    public int size() {
        return list.size();
    }

    public V get(Object key) {
        for(ArrayMapEntry entry : list)
            if (entry.key == (K) key) return (V) entry.value;
        return null;
    }

    public boolean containsKey(Object key) {
        for(ArrayMapEntry entry : list)
            if (entry.key == (K) key) return true;
        return false;
    }
}
