package fr.tse.fi2.hpp.labs.queries.impl;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class SampleQueryProcessor extends AbstractQueryProcessor {

    public SampleQueryProcessor(QueryProcessorMeasure measure) {
        super(measure);
    }

    @Override
    protected void process(DebsRecord record) {
        // Print the payment type
        writeLine(record.getTrip_distance() + " miles");
    }

}