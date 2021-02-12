package edu.iwhite;

import java.util.HashSet;
import java.util.Set;

/**
 * Extends HashTable of Strings mapped to Integers to include frequencies for each of those Strings.
 * Features
 */
public class FrequencyTable extends HashTable<String, Integer> {

    /**
     * Overridden utility method for add(). If FrequencyTable contains a word, count is increase.
     */
    @Override
    public void add(String word, Integer count) {
        super.add(word, super.contains(word) ? (super.get(word) + count) : count);
    }

    /**
     * Adds a given String to FrequencyTable.
     */
    public void add(String word) {
        add(word, 1);
    }

    /**
     * Static method for comparing two FrequencyTables. Creates a union of FT a and FT b comprised
     * a set of Strings. Returns cosine similarity of a and b.
     */
    public static double compare(FrequencyTable ft1, FrequencyTable ft2) {
        final Set<String> strs = new HashSet<>(ft1.keySet());
        strs.addAll(ft2.keySet());

        int ab = 0;
        int aa = 0;
        int bb = 0;

        for (String str : strs) {
            final int p = ft1.getOrDefault(str, 0);
            final int q = ft2.getOrDefault(str, 0);

            ab += p * q;
            aa += p * p;
            bb += q * q;
        }

        return ab / (Math.sqrt(aa) * Math.sqrt(bb));
    }
}