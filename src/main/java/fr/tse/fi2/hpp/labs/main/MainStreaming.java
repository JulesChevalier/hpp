package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class MainStreaming {

    final static Logger logger = LoggerFactory.getLogger(MainStreaming.class);

    /**
     * @param args
     * @throws IOException
     */

    public static void main(final String[] args) throws IOException {
        // Init query time measure
        final QueryProcessorMeasure measure = new QueryProcessorMeasure();
        // Init dispatcher
        final StreamingDispatcher dispatch = new StreamingDispatcher("src/main/resources/data/sorted_data.csv");

        // Query processors
        final List<AbstractQueryProcessor> processors = new ArrayList<>();
        // Add you query processor here
        processors.add(new RouteMembershipProcessor(measure));
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

}
