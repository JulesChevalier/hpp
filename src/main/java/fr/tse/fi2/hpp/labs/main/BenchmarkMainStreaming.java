package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.RouteMembershipProcessor;

/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */
@State(Scope.Thread)
@Fork(5)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkMainStreaming {

    final static Logger logger = LoggerFactory.getLogger(BenchmarkMainStreaming.class);

    private static RouteMembershipProcessor processor;

    /**
     * @param args
     * @throws IOException
     */

    @Setup
    public static void loadData() {
        // Init query time measure
        final QueryProcessorMeasure measure = new QueryProcessorMeasure();
        // Init dispatcher
        final StreamingDispatcher dispatch = new StreamingDispatcher("src/main/resources/data/sorted_data.csv");

        // Query processors
        final List<AbstractQueryProcessor> processors = new ArrayList<>();
        // Add you query processor here
        processor = new RouteMembershipProcessor(measure);
        processors.add(processor);
        // Register query processors
        for (final AbstractQueryProcessor queryProcessor : processors) {
            dispatch.registerQueryProcessor(queryProcessor);
        }
        // Initialize the latch with the number of query processors
        final CountDownLatch latch = new CountDownLatch(processors.size());
        // Set the latch for every processor
        for (final AbstractQueryProcessor queryProcessor : processors) {
            queryProcessor.setLatch(latch);
        }
        // Start everything
        for (final AbstractQueryProcessor queryProcessor : processors) {
            // queryProcessor.run();
            final Thread t = new Thread(queryProcessor);
            t.setName("QP" + queryProcessor.getId());
            t.start();
        }
        final Thread t1 = new Thread(dispatch);
        t1.setName("Dispatcher");
        t1.start();

        // Wait for the latch
        try {
            latch.await();
        } catch (final InterruptedException e) {
            logger.error("Error while waiting for the program to end", e);
        }
        // Output measure and ratio per query processor
        measure.setProcessedRecords(dispatch.getRecords());
        measure.outputMeasure();

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean searchPickupPoint() {
        final DebsRecord record = processor.getRandomRecord();
        return processor.existsPickupPoint(record.getPickup_longitude(), record.getPickup_latitude());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean searchDropoffPoint() {
        final DebsRecord record = processor.getRandomRecord();
        return processor.existsPickupPoint(record.getDropoff_longitude(), record.getDropoff_latitude());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean searchTaxiLicense() {
        final DebsRecord record = processor.getRandomRecord();
        return processor.existsTaxiLicense(record.getHack_license());
    }
}
