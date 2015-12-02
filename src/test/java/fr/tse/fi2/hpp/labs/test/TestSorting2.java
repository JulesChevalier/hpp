package fr.tse.fi2.hpp.labs.test;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.Sorting2;

public class TestSorting2 {

    private final static int MAX_SIZE = 10_000;
    private final static int MAX_VALUE = Integer.MAX_VALUE;
    private final static int TEST_NUMBER = 100;

    @Test
    public void mergeTest() {
        final int[] array = { 8, 2, 4 };
        final int[] array_merged = { 2, 4, 8 };

        Sorting2.merge(array, 0, array.length);
        Assert.assertArrayEquals(array, array_merged);
    }

    @Test
    public void simpleTest() {

        final int[] array = { 7, 5, 8, 4, 2 };
        final int[] array_insertion = new int[array.length];
        final int[] array_mergeSort = new int[array.length];
        final int[] array_mergeSortForkJoin = new int[array.length];

        System.arraycopy(array, 0, array_insertion, 0, array.length);
        System.arraycopy(array, 0, array_mergeSort, 0, array.length);
        System.arraycopy(array, 0, array_mergeSortForkJoin, 0, array.length);

        Arrays.sort(array);
        Sorting2.insertionSort(array_insertion);
        Sorting2.mergeSort(array_mergeSort);
        Sorting2.mergeSortForkJoin(array_mergeSortForkJoin);

        Assert.assertArrayEquals(array, array_insertion);
        Assert.assertArrayEquals(array, array_mergeSort);
        Assert.assertArrayEquals(array, array_mergeSortForkJoin);
    }

    @Test
    public void randomTest() {
        final Random rnd = new Random();
        int size;
        int[] array;
        int[] array_mergeSort = null;
        int[] array_mergeSortForkJoin = null;

        for (int j = 0; j < TEST_NUMBER; j++) {

            /* Generation */
            size = rnd.nextInt(MAX_SIZE - 1) + 1;
            array = new int[size];
            array_mergeSort = new int[size];
            array_mergeSortForkJoin = new int[size];
            for (int i = 0; i < array.length; i++) {
                array[i] = rnd.nextInt(MAX_VALUE);
            }
            System.arraycopy(array, 0, array_mergeSort, 0, array.length);
            System.arraycopy(array, 0, array_mergeSortForkJoin, 0, array.length);

            /* Sort */

            Arrays.sort(array);
            Sorting2.mergeSort(array_mergeSort);
            Sorting2.mergeSortForkJoin(array_mergeSortForkJoin);

            Assert.assertArrayEquals(array, array_mergeSort);
            Assert.assertArrayEquals(array, array_mergeSortForkJoin);
        }
    }
}
