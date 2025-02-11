package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.Iterator;
import java.util.Random;

public class BinaryTreeTest {
    @Test
    public void saves_key_value_pairs() {
        BinaryTree<Integer, Integer> t = new BinaryTree<>();
        Random r = new Random();
        HashSet<Integer> numbers = new HashSet<>(10);
        for (int i = 0; i < 50; i++)
            numbers.add(r.nextInt());

        Iterator<Integer> it = numbers.iterator();
        while (it.hasNext()) {
            int b = it.next();
            t.put(b, b);
        }

        it = numbers.iterator();
        while (it.hasNext()) {
            int b = it.next();
            assertEquals(b, t.get(b).intValue());
        }
    }

    @Test
    public void put_overwrites_existing_values_for_a_key() {
        BinaryTree<Integer, Integer> t = new BinaryTree<>();
        t.put(1, 1);
        assertEquals(1, t.get(1).longValue());
        t.put(1, 5);
        assertEquals(5, t.get(1).longValue());
    }

    @Test
    public void can_track_the_size_of_the_tree_even_when_duplicate_keys_are_used() {
        BinaryTree<Integer, Integer> t = new BinaryTree<>();
        t.put(1, 1);
        assertEquals(1, t.size());
        t.put(2, 1);
        assertEquals(2, t.size());
        t.put(3, 1);
        assertEquals(3, t.size());
        t.put(4, 1);
        assertEquals(4, t.size());
        t.put(4, 4);
        assertEquals(4, t.size());
    }

    @Test
    public void can_generate_an_ordered_iterator() {
        BinaryTree<Integer, Integer> t = new BinaryTree<>();
        Random r = new Random();
        HashSet<Integer> numbers = new HashSet<>(10);
        for (int i = 0; i < 50; i++) {
            int n = r.nextInt();
            if (n != Integer.MIN_VALUE)
                numbers.add(r.nextInt());
        }

        // Put UP TO 50 random numbers in a tree. The set ensures they are unique, and the tree should put them
        // in order.
        Iterator<Integer> it = numbers.iterator();
        while (it.hasNext()) {
            int b = it.next();
            t.put(b, b);
        }

        // Walk the tree. We should find that we step over an equal number of items as there are unique numbers,
        // and since we ensured they are unique, then next MUST be bigger than the prior one. We start at MIN_VALUE
        // for this check (which we made sure NOT to include in the numbers generated).
        Iterator<MapEntry<Integer, Integer>> ei = t.iterator();
        int lastKey = Integer.MIN_VALUE;
        int count = 0;
        while (ei.hasNext()) {
            count++;
            int b = ei.next().key;
            assertTrue(b > lastKey);
            lastKey = b;
        }
        assertEquals(count, numbers.size());
    }
}