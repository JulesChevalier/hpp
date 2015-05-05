package fr.tse.fi2.hpp.labs.beans.measure;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

/**
 * Store the time required for each {@link AbstractQueryProcessor} to process
 * the full stream of events
 * 
 * @author Julien
 * 
 */
public class QueryProcessorMeasure {

    final static Logger logger = LoggerFactory.getLogger(QueryProcessorMeasure.class);
    /**
     * Program starts
     */
    long startTime;
    /**
     * Program finishes
     */
    long endTime;
    /**
     * Maps PID <-> start time
     */
    private final ConcurrentHashMap<Integer, Long> timePerProcessorStart;
    /**
     * Maps PID <-> finish time
     */
    private final ConcurrentHashMap<Integer, Long> timePerProcessorFinish;
    /**
     * Number of records processed
     */
    private long records;

    public QueryProcessorMeasure() {
        this.timePerProcessorStart = new ConcurrentHashMap<>();
        this.timePerProcessorFinish = new ConcurrentHashMap<>();
        this.startTime = System.nanoTime();
    }

    /**
     * 
     * @param processorId
     *            id of the {@link AbstractQueryProcessor} that just started
     */
    public void notifyStart(final int processorId) {
        this.timePerProcessorStart.put(processorId, System.nanoTime());
    }

    /**
     * 
     * @param processorId
     *            id of the {@link AbstractQueryProcessor} that just started
     */
    public void notifyFinish(final int processorId) {
        this.timePerProcessorFinish.put(processorId, System.nanoTime());
    }

    public void setProcessedRecords(final long records) {
        this.records = records;
    }

    /**
     * Writes the results into a file in the result/ directory
     */
    public void outputMeasure() {
        this.endTime = System.nanoTime();
        final Set<Integer> pids = this.timePerProcessorStart.keySet();
        final StringBuffer sbTime = new StringBuffer();
        sbTime.append("Global execution time " + (this.endTime - this.startTime) / 1_000_000 + "ms\n");
        for (final Integer pid : pids) {
            final long nanoDiff = this.timePerProcessorFinish.get(pid) - this.timePerProcessorStart.get(pid);
            final long msDiff = nanoDiff / 1_000_000;
            sbTime.append("Query " + pid + " runtime: " + msDiff + "ms\n");
            final long throughput = this.records * 1_000_000 / nanoDiff;
            sbTime.append("Query " + pid + " throughput: " + throughput + "K events/second\n");
        }
        try {
            FileUtils.writeStringToFile(new File("result/result.txt"), sbTime.toString());
        } catch (final IOException e) {
            logger.error("Error while saving running time stats", e);
        }
    }

}
