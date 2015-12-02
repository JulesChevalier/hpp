package fr.tse.fi2.hpp.labs.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.Sorting;

public class TestSorting {

    private final static int MAX_SIZE = 100_000;
    private final static int MAX_VALUE = Integer.MAX_VALUE;

    @Test
    public void simpleTest() {

        final int[] array = { 5, 7, 8, 4, 2, 10, 1, 3, 6, 5 };

        final int[] array_mergeSortNaive_sorted = Sorting.mergeSortNaive(array);
        final int[] array_mergeSortSmartMerge_sorted = Sorting.mergeSortSmartMerge(array);
        final int[] array_mergeSortSmartMergeInsertion_sorted = Sorting.mergeSortSmartMergeInsertion(array);
        final int[] array_mergeSortSmartMergeInsertionMultithread_sorted = Sorting.mergeSortSmartMergeInsertionMultithread(array);
        final int[] array_mergeSortSmartMergeInsertionForkJoin_sorted = Sorting.mergeSortSmartMergeInsertionForkJoin(array);

        Arrays.sort(array);
        System.out.println(Sorting.print(array));
        System.out.println(Sorting.print(array_mergeSortSmartMergeInsertionForkJoin_sorted));

        Assert.assertArrayEquals(array, array_mergeSortNaive_sorted);
        Assert.assertArrayEquals(array, array_mergeSortSmartMerge_sorted);
        Assert.assertArrayEquals(array, array_mergeSortSmartMergeInsertion_sorted);
        Assert.assertArrayEquals(array, array_mergeSortSmartMergeInsertionMultithread_sorted);
        Assert.assertArrayEquals(array, array_mergeSortSmartMergeInsertionForkJoin_sorted);
    }
    // @Test
    // public void randomTest() {
    // final Random rnd = new Random();
    // int size;
    // int[] array;
    //
    // int[] array_mergeSortNaive_sorted;
    // int[] array_mergeSortSmartMerge_sorted;
    // int[] array_mergeSortSmartMergeInsertion_sorted;
    // int[] array_mergeSortSmartMergeInsertionMultithread_sorted;
    //
    // for (int j = 0; j < 100; j++) {
    //
    // /* Generation */
    // size = rnd.nextInt(MAX_SIZE - 1) + 1;
    // array = new int[size];
    // for (int i = 0; i < array.length; i++) {
    // array[i] = rnd.nextInt(MAX_VALUE);
    // }
    // /* Sort */
    //
    // array_mergeSortNaive_sorted = Sorters.mergeSortNaive(array);
    // array_mergeSortSmartMerge_sorted = Sorters.mergeSortSmartMerge(array);
    // array_mergeSortSmartMergeInsertion_sorted = Sorters.mergeSortSmartMergeInsertion(array);
    // array_mergeSortSmartMergeInsertionMultithread_sorted = Sorters.mergeSortSmartMergeInsertionMultithread(array);
    // final int[] array_mergeSortSmartMergeInsertionForkJoin_sorted =
    // Sorters.mergeSortSmartMergeInsertionForkJoin(array);
    //
    // Arrays.sort(array);
    //
    // Assert.assertArrayEquals(array, array_mergeSortNaive_sorted);
    // Assert.assertArrayEquals(array, array_mergeSortSmartMerge_sorted);
    // Assert.assertArrayEquals(array, array_mergeSortSmartMergeInsertion_sorted);
    // Assert.assertArrayEquals(array, array_mergeSortSmartMergeInsertionMultithread_sorted);
    // Assert.assertArrayEquals(array, array_mergeSortSmartMergeInsertionForkJoin_sorted);
    // }
    // }
}
