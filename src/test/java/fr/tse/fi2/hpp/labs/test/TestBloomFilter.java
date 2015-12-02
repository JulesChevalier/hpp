package fr.tse.fi2.hpp.labs.test;

import java.util.HashSet;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.BloomFilter;
import fr.tse.fi2.hpp.labs.utils.RandomString;

public class TestBloomFilter {

    @Test
    public void testBloom() {

        final int n = 100_000;
        final String[] wordsIn = new String[n];
        final String[] wordsNotIn = new String[n];

        final HashSet<String> words = new HashSet<String>();
        final Random rnd = new Random();
        while (words.size() < n * 2) {
            words.add(RandomString.nextString(rnd.nextInt(n / 100)));
        }

        int i = 0;
        final String wordsa[] = words.toArray(new String[n * 2]);
        while (i < n) {
            wordsIn[i] = wordsa[i];
            i++;
        }
        while (i < n * 2) {
            wordsNotIn[i - n] = wordsa[i];
            i++;
        }

        final BloomFilter bm = new BloomFilter(n);

        for (final String word : wordsIn) {
            bm.add(word);
        }

        for (final String word : wordsIn) {
            Assert.assertEquals(true, bm.contains(word));
        }

        int errors = 0;
        for (final String word : wordsNotIn) {
            if (bm.contains(word)) {
                errors++;
            }
        }
        System.out.println("Error: " + errors * 100. / n + "%");
    }
}
