package fr.tse.fi2.hpp.labs.queries.impl;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class IncrementalAveragePrice extends AbstractQueryProcessor {

    float avg;
    float nb;

    public IncrementalAveragePrice(final QueryProcessorMeasure measure) {
        super(measure);
        this.avg = this.nb = 0;
    }

    @Override
    protected void process(final DebsRecord record) {
        final float price = record.getFare_amount();
        this.avg = (this.avg * this.nb + price) / ++this.nb;
        this.writeLine(this.avg + "");
    }
}