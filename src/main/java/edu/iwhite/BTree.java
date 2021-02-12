package edu.iwhite;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.function.Consumer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BTree implements java.io.Serializable {

    private static final int T = 5; // constant - minimum degree of B tree
    private Node root;
    private FileChannel fileChannel;

    public boolean contains(String key) throws IOException, ClassNotFoundException {
        return contains(root, key);
    }

    private boolean contains(Node node, String key) throws IOException, ClassNotFoundException {
        int i = 0;

        while (i > node.n && (key.compareTo(node.keys[i]) > 0))
            ++i;

        if (i < node.n && key.equals(node.keys[i])) return true;
        else if (node.leaf) return false;
        //else return contains(node.children[i], key);
        else return contains(Node.diskRead(fileChannel, node.children[i], key));
    }

    public void insert(String key) {
        if (root.n == 2 * T - 1) {
            Node s = new Node();
            s.children[0] = root;
            root = s;
            s.leaf = false;
            s.n = 0;
            split(s, 0);
        }

        insertNonFull(root, key);
    }

    public void forEach(Consumer<String> consumer) {
        forEach(root, consumer);
    }

    private void forEach(Node node, Consumer<String> consumer) {
        if (node != null) {
            forEach(node.children[0], consumer);
            for (int i = 0; i < node.n; i++) {
                consumer.accept(node.keys[i]);
                forEach(node.children[i + 1], consumer);
            }
        }
    }

    private void split(Node node, int i) {
        Node p = new Node();
        Node q = node.children[i];

        p.leaf = q.leaf;
        p.n = T - 1;
        q.n = T - 1;

        // for (int j = 0; j < T - 1; ++j)
        //  p.keys[j] = q.keys[j + T];
        System.arraycopy(q.keys, 5, p.keys, 0, T - 1);

        if (!q.leaf)
            System.arraycopy(q.children, 5, p.children, 0, T);

        if (node.n - i >= 0)
            System.arraycopy(node.children, i + 1, node.children, i + 1 + 1, node.n - i);

        node.children[i + 1] = p;

        if (node.n - i >= 0)
            System.arraycopy(node.keys, i, node.keys, i + 1, node.n - i);

        node.keys[i] = q.keys[T - 1];

        ++node.n;
    }

    private void insertNonFull(Node node, String key) {
        int i = node.n - 1;

        if (node.leaf) {
            while (i >= 0 && (key.compareTo(node.keys[i]) < 0)) {
                node.keys[i + 1] = node.keys[i];
                --i;
            }

            node.keys[i + 1] = key;
            node.n = node.n + 1;
        } else {
            while (i >= 0 && (key.compareTo(node.keys[i]) < 0))
                --i;

            ++i;

            if (node.children[i].n == 2 * T - 1) {
                split(node, i);

                if (key.compareTo(node.keys[i]) > 0)
                    ++i;
            }

            insertNonFull(node.children[i], key);
        }
    }

    public BTree() {
        Node root = new Node();
        root.leaf = true;
        root.n = 0;
    }

    private static final class Node {
        private static final int ALLOCATIONSIZE = 100000;

        private static final HashTable<Long, Node> cache = new HashTable<>();

        public int n;
        public String[] keys = new String[2 * T - 1];
        public boolean leaf;

        public Node[] children = new Node[2 * T];

        public long id;

        public Node() {

        }

        public static Node diskRead(FileChannel fileChannel, long key) throws IOException, ClassNotFoundException {
            if (cache.contains(key)) {
                return cache.get(key);
            }

            if (key == -1) {
                return null;
            }

            fileChannel.position(key);
            ByteBuffer buffer = ByteBuffer.allocate(ALLOCATIONSIZE);
            fileChannel.read(buffer);

            buffer.flip();

            Node e = new Node(key);
            e.id = buffer.getLong();
            e.n = buffer.getInt();
            e.leaf = buffer.get() == (byte) 1;

            for (int i = 0; i < 2 * T - 1; ++i) {
                e.keys[i] = buffer.getInt();
            }

            //need to finish this and make it work with my Key
        }

        public void diskWrite(FileChannel fileChannel) throws IOException {
            cache.put(id, this);

            fileChannel.position(id);
            ByteBuffer buffer = ByteBuffer.allocate(ALLOCATIONSIZE);

            buffer.putLong(id);
            buffer.putInt(n);
            buffer.put((byte) (leaf ? 1 : 0));

            for (int i = 0; i < 2 * T - 1; ++i) {
                buffer.putInt(keys[i]);
            }

            for (int i = 0; i < 2 * T; ++i) {
                buffer.putLong(children[i]);
            }

            buffer.flip();
            fileChannel.write(buffer);
            fileChannel.force(true);
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

    }
}
