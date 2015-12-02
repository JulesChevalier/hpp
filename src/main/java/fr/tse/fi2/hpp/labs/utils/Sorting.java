package fr.tse.fi2.hpp.labs.utils;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import org.apache.commons.lang.ArrayUtils;

public class Sorting {

    private Sorting() {

    }

    /* SORTS */

    public static void insertionSort(final int[] array) {
        int tmp, j;
        for (int i = 1; i < array.length; i++) {
            tmp = array[i];
            j = i;
            while (j > 0 && array[j - 1] > tmp) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = tmp;
        }
    }

    public static int[] mergeSortNaive(final int[] array) {
        if (array.length > 2) {
            final int[] a1 = mergeSortNaive(ArrayUtils.subarray(array, 0, array.length / 2));
            final int[] a2 = mergeSortNaive(ArrayUtils.subarray(array, array.length / 2, array.length));
            final int[] result = new int[array.length];
            mergeNaive(a1, a2, result);
            return result;
        }
        if (array.length == 1) {
            return array;
        }
        if (array[0] <= array[1]) {
            return array;
        }
        final int tmp = array[1];
        array[1] = array[0];
        array[0] = tmp;
        return array;
    }

    public static int[] mergeSortSmartMerge(final int[] array) {
        if (array.length > 2) {
            final int[] a1 = mergeSortSmartMerge(ArrayUtils.subarray(array, 0, array.length / 2));
            final int[] a2 = mergeSortSmartMerge(ArrayUtils.subarray(array, array.length / 2, array.length));
            final int[] result = new int[array.length];
            mergeSmart(a1, a2, result);
            return result;
        }
        if (array.length == 1) {
            return array;
        }
        if (array[0] <= array[1]) {
            return array;
        }
        final int tmp = array[1];
        array[1] = array[0];
        array[0] = tmp;
        return array;
    }

    public static int[] mergeSortSmartMergeInsertion(final int[] array) {
        if (array.length > 20) {
            final int[] a1 = mergeSortSmartMergeInsertion(ArrayUtils.subarray(array, 0, array.length / 2));
            final int[] a2 = mergeSortSmartMergeInsertion(ArrayUtils.subarray(array, array.length / 2, array.length));
            final int[] result = new int[array.length];
            mergeSmart(a1, a2, result);
            return result;
        }
        final int[] result = Arrays.copyOf(array, array.length);
        insertionSort(result);
        return result;
    }

    public static int[] mergeSortSmartMergeInsertionMultithread(final int[] array) {
        final int[] result = Arrays.copyOf(array, array.length);
        final Thread t1;
        try {
            t1 = new MergeSortThread(result);
            t1.start();
            t1.join();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static int[] mergeSortSmartMergeInsertionForkJoin(final int[] array) {
        final int[] result = Arrays.copyOf(array, array.length);
        final ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkSort(result, 0, result.length));
        return result;
    }

    /* MERGES */

    private static void mergeNaive(final int[] a1, final int[] a2, final int[] result) {
        int i1 = 0, i2 = 0;
        for (int j = 0; j < result.length; j++) {
            if (i1 >= a1.length) {
                result[j] = a2[i2++];
            } else if (i2 >= a2.length) {
                result[j] = a1[i1++];
            } else if (a1[i1] <= a2[i2]) {
                result[j] = a1[i1++];
            } else {
                result[j] = a2[i2++];
            }
        }
    }

    private static void mergeSmart(final int[] a1, final int[] a2, final int[] result) {
        if (a1[a1.length - 1] <= a2[0]) {
            int i = 0;
            for (final int element : a1) {
                result[i++] = element;
            }
            for (final int element : a2) {
                result[i++] = element;
            }
        } else {
            mergeNaive(a1, a2, result);
        }
    }

    /* CALLABLES */

    private static class MergeSortThread extends Thread {

        private final int[] array;

        public MergeSortThread(final int[] array) {
            this.array = array;
        }

        @Override
        public void run() {
            if (this.array.length > 100) {

                final int[] a1 = ArrayUtils.subarray(this.array, 0, this.array.length / 2);
                final int[] a2 = ArrayUtils.subarray(this.array, this.array.length / 2, this.array.length);

                final Thread t1 = new MergeSortThread(a1);
                final Thread t2 = new MergeSortThread(a2);
                try {
                    t1.join();
                    t2.join();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                mergeSmart(a1, a2, this.array);
            }
            System.arraycopy(mergeSortSmartMergeInsertion(this.array), 0, this.array, 0, this.array.length);
        }

    }

    private static class ForkSort extends RecursiveAction {

        /**
         * 
         */
        private static final long serialVersionUID = -8700511576865001752L;

        private final int[] mSource;
        private final int mStart;
        private final int mLength;

        public ForkSort(final int[] src, final int start, final int length) {
            this.mSource = src;
            this.mStart = start;
            this.mLength = length;
        }

        // Average pixels from source, write results into destination.
        protected void computeDirectly() {
            System.arraycopy(Sorting.mergeSortSmartMergeInsertion(this.mSource), this.mStart, this.mSource, this.mStart, this.mLength);
        }

        protected int sThreshold = 10;

        @Override
        protected void compute() {
            if (this.mLength < this.sThreshold) {
                this.computeDirectly();
                return;
            }
            final int split = this.mLength / 2;
            invokeAll(new ForkSort(this.mSource, this.mStart, split), new ForkSort(this.mSource, this.mStart + split, this.mLength - split));
            this.mergeOne(this.mSource, this.mStart, split, this.mStart + split, this.mLength - split);
        }

        protected void mergeOne(final int[] source, final int i1, final int length1, final int i2, final int length2) {

        }
    }

    /* UTILS */

    public static String print(final int[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        for (; i < array.length - 1; i++) {
            sb.append(array[i]);
            sb.append(",");
        }
        if (array.length > 0) {
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
