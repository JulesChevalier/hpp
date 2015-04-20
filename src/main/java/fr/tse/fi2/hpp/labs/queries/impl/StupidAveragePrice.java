package fr.tse.fi2.hpp.labs.queries.impl;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class StupidAveragePrice extends AbstractQueryProcessor {

    java.util.List<Float> prices;

    public StupidAveragePrice(final QueryProcessorMeasure measure) {
        super(measure);
        this.prices = new ArrayList<Float>();
    }

    @Override
    protected void process(final DebsRecord record) {
        this.prices.add(record.getFare_amount());
        float avg = 0;
        for (final float price : this.prices) {
            avg += price;
        }
        avg = avg / this.prices.size();
        this.writeLine(avg + "");
    }
}