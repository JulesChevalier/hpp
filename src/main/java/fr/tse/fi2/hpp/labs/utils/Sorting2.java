package fr.tse.fi2.hpp.labs.utils;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class Sorting2 {

    public static final int SIZE_LIMIT_INSERTION = 20;
    public static final int SIZE_LIMIT_MUTLITHREAD = 100;

    private Sorting2() {

    }

    /* SORTS */

    public static void insertionSort(final int[] array) {
        insertionSort(array, 0, array.length);
    }

    public static void insertionSort(final int[] array, final int index, final int length) {
        int tmp, j;
        for (int i = index + 1; i < index + length; i++) {
            tmp = array[i];
            j = i;
            while (j > index && array[j - 1] > tmp) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = tmp;
        }
    }

    public static void mergeSort(final int[] array) {
        mergeSort(array, 0, array.length);
    }

    public static void mergeSort(final int[] array, final int index, final int length) {
        if (length > SIZE_LIMIT_INSERTION) {
            mergeSort(array, index, length / 2);
            mergeSort(array, index + length / 2, length - length / 2);
            merge(array, index, length);
        } else {
            insertionSort(array, index, length);
        }
    }

    public static void mergeSortForkJoin(final int[] array) {
        final ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkSort(array));
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* MERGES */

    public static void merge2(final int[] array, final int index, final int length) {
        if (array[index + length / 2 - 1] <= array[index + length / 2]) {
            return;
        }
        int i1 = index;
        int i2 = index + length / 2;
        int tmp;
        while (i1 < i2 && i2 < index + length) {

            if (array[i1] > array[i2]) {
                tmp = array[i2];
                for (int j = i2; j >= i1; j--) {
                    if (j > index) {
                        array[j] = array[j - 1];
                    }
                }
                array[i1] = tmp;
                i2++;
            } else {
                i1++;
            }
        }

    }

    public static void merge(final int[] array, final int index, final int length) {
        if (array[index + length / 2 - 1] <= array[index + length / 2]) {
            return;
        }
        int i1 = index;
        int i2 = index + length / 2;
        int tmp;
        while (i1 < i2 && i2 < index + length) {

            if (array[i1] > array[i2]) {
                tmp = array[i2];
                System.arraycopy(array, i1, array, i1 + 1, i2 - i1);
                array[i1] = tmp;
                i2++;
            } else {
                i1++;
            }
        }

    }

    /* FORKS */

    private static class ForkSort extends RecursiveAction {

        /**
         * 
         */
        private static final long serialVersionUID = -8700511576865001752L;

        private final int[] array;
        private final int index;
        private final int length;

        public ForkSort(final int[] array) {
            this.array = array;
            this.index = 0;
            this.length = array.length;
        }

        public ForkSort(final int[] array, final int start, final int length) {
            this.array = array;
            this.index = start;
            this.length = length;
        }

        @Override
        protected void compute() {
            if (this.length < SIZE_LIMIT_MUTLITHREAD) {
                mergeSort(this.array, this.index, this.length);
                return;
            }
            final ForkSort fs1 = new ForkSort(this.array, this.index, this.length / 2);
            final ForkSort fs2 = new ForkSort(this.array, this.index + this.length / 2, this.length - this.length / 2);

            fs1.fork();
            fs2.fork();

            fs1.join();
            fs2.join();

            merge(this.array, this.index, this.length);
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

    public static String print(final int[] array, final int index, final int length) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        for (; i < array.length - 1; i++) {
            if (i == index) {
                sb.append("|");
            }
            sb.append(array[i]);
            if (i == index + length - 1) {
                sb.append("|");
            }
            sb.append(",");
        }
        if (array.length > 0) {
            sb.append(array[i]);
            if (i == index + length - 1) {
                sb.append("|");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
