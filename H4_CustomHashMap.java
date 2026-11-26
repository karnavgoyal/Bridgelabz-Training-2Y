/*
 * ============================================================================
 *  H4 : IMPLEMENT A CUSTOM HASH MAP  (separate chaining)
 *  Problem : Build a hash map class supporting insertion, deletion, retrieval.
 *  Design  : - table  = array of buckets; each bucket is a singly linked list
 *            - hash function:  index = (key.hashCode() & 0x7fffffff) % capacity
 *              (& 0x7fffffff makes the hash non-negative)
 *            - COLLISION: two keys with the same index are chained in the same
 *              bucket's linked list (separate chaining)
 *            - RESIZE: when size / capacity > 0.75 (load factor) the table is
 *              doubled and every entry is re-hashed, keeping chains short
 *  Time    : put / get / remove -> O(1) average, O(n) worst case
 *            (worst case = every key lands in one bucket)
 *  Space   : O(n)
 *  Run     : javac H4_CustomHashMap.java   then   java H4_CustomHashMap
 * ============================================================================
 */
public class H4_CustomHashMap {

    // --------------------------- ENTRY (NODE) -------------------------------
    static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;                         // next entry in the same bucket
        Entry(K key, V value) { this.key = key; this.value = value; }
    }

    // ----------------------------- HASH MAP ---------------------------------
    static class MyHashMap<K, V> {
        private static final double LOAD_FACTOR = 0.75;
        private Entry<K, V>[] table;
        private int size;

        @SuppressWarnings("unchecked")
        MyHashMap(int capacity) {
            table = (Entry<K, V>[]) new Entry[capacity];
        }

        // Hash function: converts a key to a bucket index
        private int indexFor(K key, int capacity) {
            return (key.hashCode() & 0x7fffffff) % capacity;
        }

        // ---- INSERT / UPDATE ----------------------------------- O(1) avg ---
        void put(K key, V value) {
            if (key == null) throw new IllegalArgumentException("null keys are not supported");
            int idx = indexFor(key, table.length);

            for (Entry<K, V> e = table[idx]; e != null; e = e.next) {
                if (e.key.equals(key)) {          // key already present -> update value
                    e.value = value;
                    return;
                }
            }
            Entry<K, V> node = new Entry<>(key, value);
            node.next = table[idx];               // insert at the head of the chain
            table[idx] = node;
            size++;

            if ((double) size / table.length > LOAD_FACTOR) resize();
        }

        // ---- RETRIEVE ------------------------------------------ O(1) avg ---
        V get(K key) {
            if (key == null) return null;
            for (Entry<K, V> e = table[indexFor(key, table.length)]; e != null; e = e.next)
                if (e.key.equals(key)) return e.value;
            return null;                          // key not found
        }

        boolean containsKey(K key) {
            if (key == null) return false;
            for (Entry<K, V> e = table[indexFor(key, table.length)]; e != null; e = e.next)
                if (e.key.equals(key)) return true;
            return false;
        }

        // ---- DELETE -------------------------------------------- O(1) avg ---
        boolean remove(K key) {
            if (key == null) return false;
            int idx = indexFor(key, table.length);
            Entry<K, V> prev = null, cur = table[idx];
            while (cur != null) {
                if (cur.key.equals(key)) {
                    if (prev == null) table[idx] = cur.next;   // removing the first node of the chain
                    else prev.next = cur.next;                 // bypass the node
                    cur.next = null;
                    size--;
                    return true;
                }
                prev = cur;
                cur = cur.next;
            }
            return false;                         // key was not present
        }

        int size() { return size; }
        boolean isEmpty() { return size == 0; }

        // ---- RESIZE & REHASH ----------------------------------- O(n) -------
        @SuppressWarnings("unchecked")
        private void resize() {
            Entry<K, V>[] old = table;
            Entry<K, V>[] bigger = (Entry<K, V>[]) new Entry[old.length * 2];
            for (Entry<K, V> head : old) {
                Entry<K, V> e = head;
                while (e != null) {
                    Entry<K, V> next = e.next;                 // save before relinking
                    int idx = indexFor(e.key, bigger.length);  // NEW index (capacity changed)
                    e.next = bigger[idx];
                    bigger[idx] = e;
                    e = next;
                }
            }
            table = bigger;
            System.out.println("   [resized: capacity " + old.length + " -> " + bigger.length + "]");
        }

        // Prints every bucket so collisions (chains) are visible
        void display() {
            System.out.println("  size=" + size + ", capacity=" + table.length);
            for (int i = 0; i < table.length; i++) {
                StringBuilder sb = new StringBuilder("  bucket " + i + ": ");
                if (table[i] == null) sb.append("-");
                for (Entry<K, V> e = table[i]; e != null; e = e.next) {
                    sb.append("[").append(e.key).append("=").append(e.value).append("]");
                    if (e.next != null) sb.append(" -> ");
                }
                System.out.println(sb);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test 1: Integer keys that COLLIDE (1, 9, 17 all map to bucket 1 when capacity = 8) ===");
        MyHashMap<Integer, String> map = new MyHashMap<>(8);
        map.put(1, "one");
        map.put(9, "nine");
        map.put(17, "seventeen");
        map.put(2, "two");
        map.display();

        System.out.println("\n=== Retrieval ===");
        System.out.println("get(9)  = " + map.get(9));
        System.out.println("get(17) = " + map.get(17));
        System.out.println("get(5)  = " + map.get(5) + "   (missing key)");
        System.out.println("containsKey(2) = " + map.containsKey(2));

        System.out.println("\n=== Update existing key ===");
        map.put(9, "NINE");
        System.out.println("get(9)  = " + map.get(9) + ", size still " + map.size());

        System.out.println("\n=== Deletion (middle of chain, then missing key) ===");
        System.out.println("remove(9)  -> " + map.remove(9));
        System.out.println("remove(99) -> " + map.remove(99));
        map.display();

        System.out.println("\n=== Test 2: String keys + automatic resizing (capacity 4) ===");
        MyHashMap<String, Integer> marks = new MyHashMap<>(4);
        marks.put("Karan", 91);
        marks.put("Riya", 85);
        marks.put("Aman", 78);
        marks.put("Neha", 88);        // triggers resize (4th entry > 0.75 load)
        marks.put("Dev", 70);
        marks.display();
        System.out.println("Riya's marks = " + marks.get("Riya"));
    }
}
