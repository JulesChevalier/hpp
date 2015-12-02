package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.utils.Sorting;

@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 2)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkSorters {

    final static Logger logger = LoggerFactory.getLogger(BenchmarkSorters.class);

    // @Param({ "100000", "1000000", "10000000" })
    @Param({ "1000000" })
    private static int SIZE;

    private int[] array;

    /**
     * @param args
     * @throws IOException
     */

    @Setup(Level.Iteration)
    public void loadData() {

        final Random rnd = new Random();

        this.array = new int[SIZE];
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = rnd.nextInt();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void sortNaive() {
        Sorting.mergeSortNaive(this.array);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void sortSmartMerge() {
        Sorting.mergeSortSmartMerge(this.array);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void mergeSortSmartMergeInsertion() {
        Sorting.mergeSortSmartMergeInsertion(this.array);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void mergeSortSmartMergeInsertionMultithread() {
        Sorting.mergeSortSmartMergeInsertionMultithread(this.array);
    }
}
