package edu.iwhite;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores data with an array of Keys mapped to Values
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class HashTable<Key, Value> {

    private Node[] table;
    private int count = 0;

    /**
     * Returns a Set of all keys in HashTable.
     */
    public Set<Key> keySet() {
        final HashSet<Key> keys = new HashSet<>();

        for (Node p : table) {
            for (Node q = p; q != null; q = q.next) {
                keys.add((Key) q.key);
            }
        }

        return keys;
    }

    /**
     * Returns Value associated with provided Key.
     */
    public Value get(Key key) {
        final int h = key.hashCode();
        final int i = h & (table.length - 1);

        for (Node p = table[i]; p != null; p = p.next) {
            if (key.equals(p.key)) {
                return (Value) p.value;
            }
        }

        return null;
    }

    /**
     * Returns Value associated with Key or returns given default value.
     */
    public Value getOrDefault(Key key, Value defaultValue) {
        return contains(key) ? get(key) : defaultValue;
    }

    /**
     * Adds Key associated with some Value to HashTable. If Key is already present,
     * Value is overwritten. Initiates resize() if HashTable exceeds a certain size.
     */
    public void add(Key key, Value value) {
        if ((float) count / table.length > 0.75) resize();

        final int h = key.hashCode();
        final int i = h & (table.length - 1);  // h % table.length

        for (Node p = table[i]; p != null; p = p.next) {
            if (key.equals(p.key)) {
                p.value = value;
                return;
            }
        }

        table[i] = new Node(key, value, table[i]);
        ++count;
    }

    /**
     * Doubles size of HashTable.
     */
    private void resize() {
        final Node[] old = table;
        table = new Node[old.length << 1];
        count = 0;

        for (Node p : old) {
            for (Node q = p; q != null; q = q.next) {
                add((Key) q.key, (Value) q.value);
            }
        }
    }

    /**
     * Returns boolean if given Key is present in HashTable.
     */
    public boolean contains(Key key) {
        final int h = key.hashCode();
        final int i = h & (table.length - 1);

        for (Node p = table[i]; p != null; p = p.next) {
            if (key.equals(p.key))
                return true;
        }

        return false;
    }

    public HashTable() {
        table = new Node[8];
    }

    private static final class Node<Key, Value> {
        final Key key;
        Value value;
        final Node<Key, Value> next;

        Node(Key key, Value value, Node<Key, Value> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}