package org.example;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;

// What is a set???
// * A collection that does not allow duplicates. Could be used for:
//   * Deduplication
//   * Set Logic: Union, Intersection, Difference
// * Common operations?
//   * add to the set
//   * contains an item?
//   * Iteration of the items
public class HashSet<K> {
    protected LinkedList<K>[] buckets;
    protected int count = 0;

    public HashSet(int initialSize) {
        this.buckets = new LinkedList[initialSize];
    }

    Iterator<K> iterator() {
        return new Iterator<K>() {
            // this is accessible as a Closure
            int currentBucket = -1; // so I can know I need to initialize during hasNext
            Iterator<K> currentBucketIterator;

            private void moveToNextPopulatedBucket(int startingFrom) {
                int bi;
                for (bi = startingFrom; bi < buckets.length; bi++) {
                    if (buckets[bi] != null && buckets[bi].iterator().hasNext()) {
                        break;
                    }
                }
                // IF bi is == length, then I walked off then END!
                currentBucket = bi;
                if (bi != buckets.length)
                    currentBucketIterator = buckets[bi].iterator();
            }

            @Override
            public boolean hasNext() {
                if (currentBucket == -1) {
                    moveToNextPopulatedBucket(0);
                }
                return (currentBucket < buckets.length) && currentBucketIterator.hasNext();
            }

            @Override
            public K next() {
                if (currentBucket == -1) {
                    moveToNextPopulatedBucket(0);
                }
                if (currentBucketIterator == null)
                    return null;
                if (currentBucketIterator.hasNext()) {
                    K result = currentBucketIterator.next();
                    if (!currentBucketIterator.hasNext())
                        moveToNextPopulatedBucket(currentBucket + 1);
                    return result;
                }
                return null;
            }
        };
    }

    /**
     * Puts the given value in the table at key. Overwrites prior values for that key.
     * <p>
     * //         * @param value The thing you want to put in the table.
     */
    public void add(K key) {
        int code = Math.abs(key.hashCode());
        int bucketIndex = code % this.buckets.length;
        if (this.buckets[bucketIndex] == null)
            this.buckets[bucketIndex] = new LinkedList<>();
        if (!this.buckets[bucketIndex].contains(key)) {
            this.buckets[bucketIndex].add(key);
            count++;
        }
        if (needsResized())
            resize();
    }

    private void resize() {
        int newSize = buckets.length * 3;
        // Make a new table
        LinkedList<K>[] newBuckets = new LinkedList[newSize];
        // Copy the items from the old to the new, but at the correct location
        for (LinkedList<K> b : buckets) {
            if (b != null)
                for (K item : b) {
                    int newBucketIndex = Math.abs(item.hashCode()) % newSize;
                    if (newBuckets[newBucketIndex] == null)
                        newBuckets[newBucketIndex] = new LinkedList<>();
                    newBuckets[newBucketIndex].add(item);
                }
        }
        this.buckets = newBuckets;
    }

    private boolean needsResized() {
        return (buckets.length * 2 < count);
    }

    public boolean contains(K key) {
        int code = key.hashCode();
        int bucketIndex = code % this.buckets.length;
        LinkedList<K> list = buckets[bucketIndex];
        if (list != null)
            for (K e : list) {
                if (e == key)
                    return true;
            }
        return false;
    }

    public void delete(K key) {
        int code = key.hashCode();
        int bucketIndex = code % this.buckets.length;
        K me = null;
        LinkedList<K> list = buckets[bucketIndex];
        for (K e : list) {
            if (e == key) {
                me = e;
                break;
            }
        }
        if (me != null) {
            list.remove(me);
            count--;
        }
    }

    // this U b
    public HashSet<K> union(HashSet<K> b) {
        HashSet<K> union = new HashSet<>(this.size() + b.size());
        Iterator<K> firstIteration = this.iterator();

        while (firstIteration.hasNext())
            union.add(firstIteration.next());

        Iterator<K> secondIteration = b.iterator();
        while (secondIteration.hasNext())
            union.add(secondIteration.next());

        return union;
    }

    // this I b
    public HashSet<K> intersect(HashSet<K> b) {
        HashSet<K> intersect = new HashSet<>(Math.min(this.size(), b.size()));
        HashSet<K> smaller = (this.size() < b.size() ? this : b);
        HashSet<K> larger = (this.size() < b.size() ? b : this);

        Iterator<K> iterator = smaller.iterator();

        while (iterator.hasNext()) {
            K element = iterator.next();
            if (larger.contains(element))
                intersect.add(element);
        }
        return intersect;
    }

    // this - b
    public HashSet<K> difference(HashSet<K> b) {
        HashSet<K> difference = new HashSet<>(this.size());

        Iterator<K> iterator = this.iterator();

        while (iterator.hasNext()) {
            K element = iterator.next();
            if (!b.contains(element))
                difference.add(element);
        }
        return difference;
    }

    public int size() {
        return count;
    }

    public String toString() {
        StringWriter result = new StringWriter();
        result.append("Table has " + count + " entries\n");
        for (int i = 0; i < buckets.length; i++) {
            result.append(i + " -> ");
            if (buckets[i] != null)
                for (K e : buckets[i]) {
                    result.append("[" + e + "] -> ");
                }
            result.append("ø\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        HashSet<String> data = new HashSet<>(2);

        data.add("A");
        data.add("B");
        data.add("C");
        data.add("D");


        HashSet<String> data1 = new HashSet<>(2);

        data1.add("C");
        data1.add("A");
        data1.add("G");
        data1.add("A");
        System.out.println(data.union(data1));

        Iterator<String> i = data.iterator();
        while (i.hasNext())
            System.out.print(i.next() + " ");
    }
}

