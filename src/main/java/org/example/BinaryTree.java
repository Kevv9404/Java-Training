package org.example;

import java.util.Iterator;
import java.util.Stack;

// 1. Worst case, devolves into a Linked List. O(n^2) for building, and O(n) for put/get.
// 2. Best case, recursively divides the "search space" in two at every level, leading to a depth of log_2(n).
// 3. BUILDING the tree takes Nlog(N) steps. (cost of sorting)
// 4. There are more advanced versions that "rebalance themselves" on the fly. See Red-Black Tree.
// Usually used as "Map-like" things (like your HashSet)
class Node<K, V> {
    public MapEntry<K, V> entry;
    public Node<K, V> left = null, right = null;

    public Node(K k, V v) {
        this.entry = new MapEntry<>(k, v);
    }
}

public class BinaryTree<K extends Comparable<K>, V> {
    Node<K, V> root;
    int count = 0;

    private void internalPut(Node<K, V> tree, K k, V v) {
        K currentK = tree.entry.key;

        if (currentK.compareTo(k) == 0)
            tree.entry.value = v;

        else if (currentK.compareTo(k) > 0) {
            if (tree.left == null) {
                tree.left = new Node<>(k, v);
                count++;
            } else
                internalPut(tree.left, k, v);

        } else if (currentK.compareTo(k) < 0) {
            if (tree.right == null) {
                tree.right = new Node<>(k, v);
                count++;
            } else
                internalPut(tree.right, k, v);
        }
    }

    public void put(K key, V value) {
        if (this.root == null) {
            this.root = new Node<>(key, value);
            count++;
            return;
        }
        internalPut(root, key, value);
    }

    public V get(K key) {
        return internalGet(this.root, key);
    }

    private V internalGet(Node<K, V> tree, K k) {
        if (tree == null)
            return null;
        K currentK = tree.entry.key;

        if (currentK.compareTo(k) == 0)
            return tree.entry.value;
        else if (currentK.compareTo(k) > 0)
            return internalGet(tree.left, k);

        return internalGet(tree.right, k);
    }

    public int size() {
        return count;
    }

    class BinaryTreeIterator<K, V> implements Iterator<MapEntry<K, V>> {
        Stack<Node<K, V>> path;

        BinaryTreeIterator(Node<K, V> root) {
            this.path = new Stack<>();
            if (root != null)
                walkLeft(root);
        }

        private void walkLeft(Node<K, V> n) {
            path.push(n);
            if (n.left != null)
                walkLeft(n.left);

        }

        @Override
        public boolean hasNext() {
            return !path.isEmpty();
        }

        @Override
        public MapEntry<K, V> next() {
            Node<K, V> n = path.pop();
            if (n.right != null)
                walkLeft(n.right);
            return n.entry;
        }
    }

    public Iterator<MapEntry<K, V>> iterator() {
        return null;
    }


    public static void main(String[] args) {
        BinaryTree<String, Integer> t = new BinaryTree<>();

        t.put("Foo", 22);
        t.put("Bar", 28);
        t.put("Hey", 30);
        t.put("Kevin", 31);
        t.put("Kevin", 36);
        t.put("Shaki", 16);
        t.put("David", 25);
        t.put("Jhoan", 30);
        t.put("Kevin", 10);

        System.out.println(t.count);
        System.out.println(t.get("Kevin"));

    }
}
