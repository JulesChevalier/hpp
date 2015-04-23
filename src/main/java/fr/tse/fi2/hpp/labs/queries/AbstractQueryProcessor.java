package fr.tse.fi2.hpp.labs.queries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.queries.impl.NaiveLineWriter;

/**
 * Every query must extend this class that provides basic functionalities such
 * as :
 * <ul>
 * <li>Receives event from {@link StreamingDispatcher}</li>
 * <li>Notify start/end time</li>
 * <li>Manages thread synchronization</li>
 * <li>Grid mapping: maps lat/long to x,y in a discrete grid of given size</li>
 * </ul>
 * 
 * @author Julien
 * 
 */
public abstract class AbstractQueryProcessor implements Runnable {

    final static Logger logger = LoggerFactory.getLogger(AbstractQueryProcessor.class);

    /**
     * Counter to uniquely identify the query processors
     */
    private final static AtomicInteger COUNTER = new AtomicInteger();
    /**
     * Unique ID of the query processor
     */
    private final int id = COUNTER.incrementAndGet();
    /**
     * LineWriter
     */
    AbstractLineWriter lineWriter;
    /**
     * Internal queue of events
     */
    public final BlockingQueue<DebsRecord> eventqueue;
    /**
     * Global measurement
     */
    private final QueryProcessorMeasure measure;
    /**
     * For synchronisation purpose
     */
    private CountDownLatch latch;

    /**
     * Default constructor. Initialize event queue and writer
     */
    public AbstractQueryProcessor(final QueryProcessorMeasure measure) {
        // Set the global measurement instance
        this.measure = measure;
        // Initialize queue
        this.eventqueue = new LinkedBlockingQueue<>();

        // Initialize writer
        this.lineWriter = null;
        try {
            this.lineWriter = new NaiveLineWriter(this.id, new BufferedWriter(new FileWriter(new File("result/query" + this.id + ".txt"))));
        } catch (final IOException e) {
            logger.error("Cannot open output file for " + this.id, e);
            System.exit(-1);
        }
    }

    public void setLatch(final CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        logger.info("Starting query processor " + this.id);
        // Notify beginning of processing
        this.measure.notifyStart(this.id);
        this.lineWriter.start();
        while (true) {
            try {
                final DebsRecord record = this.eventqueue.take();
                if (record.isPoisonPill()) {
                    break;
                } else {
                    this.process(record);
                }
            } catch (final InterruptedException e) {
                logger.error("Error taking element from internal queue, processor " + this.id, e);
                break;
            }
        }
        // Finish, close the writer and notify the measurement
        this.finish();
        logger.info("Closing query processor " + this.id);
    }

    /**
     * 
     * @param record
     *            record to be processed
     */
    protected abstract void process(DebsRecord record);

    /**
     * 
     * @param record
     *            the record to process
     * @return the route in a 600*600 grid
     */
    protected Route convertRecordToRoute(final DebsRecord record) {
        // Convert pickup coordinates into cell
        final float lat1 = record.getPickup_latitude();
        final float long1 = record.getPickup_longitude();
        final GridPoint pickup = this.convert(lat1, long1);
        // Convert pickup coordinates into cell
        final float lat2 = record.getDropoff_latitude();
        final float long2 = record.getDropoff_longitude();
        final GridPoint dropoff = this.convert(lat2, long2);
        return new Route(pickup, dropoff);
    }

    /**
     * 
     * @param lat1
     * @param long1
     * @return The lat/long converted into grid coordinates
     */
    private GridPoint convert(final float lat1, final float long1) {
        return new GridPoint(this.cellX(lat1), this.cellY(long1));
    }

    /**
     * Provided by Syed and Abderrahmen
     * 
     * @param x
     * @return
     */
    private int cellX(final float x) {

        // double x=0;
        final double x_0 = -74.913585;
        final double delta_x = 0.005986 / 2;

        // double cell_x;
        final Double cell_x = 1 + Math.floor((x - x_0) / delta_x + 0.5);

        return cell_x.intValue();
    }

    /**
     * Provided by Syed and Abderrahmen
     * 
     * @param y
     * @return
     */
    private int cellY(final double y) {

        final double y_0 = 41.474937;
        final double delta_y = 0.004491556 / 2;

        final Double cell_y = 1 + Math.floor((y_0 - y) / delta_y + 0.5);

        return cell_y.intValue();

    }

    /**
     * @return the id of the query processor
     */
    public final int getId() {
        return this.id;
    }

    /**
     * 
     * @param line
     *            the line to write as an answer
     */
    protected void writeLine(final String line) {
        this.lineWriter.addLine(line);

    }

    /**
     * Poison pill has been received, close output
     */
    protected void finish() {
        // Notify finish time
        this.measure.notifyFinish(this.id);
        this.lineWriter.addLine("");
        logger.info("Sending poison pill to line writer " + this.id);
        try {
            this.lineWriter.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        // Decrease latch count
        this.latch.countDown();
    }

}
