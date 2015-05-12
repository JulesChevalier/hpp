package fr.tse.fi2.hpp.labs.queries.impl;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.utils.BloomFilter;

public class RecordBloomMembershipProcessor extends AbstractQueryProcessor {

    private final BloomFilter bm;

    public RecordBloomMembershipProcessor(final QueryProcessorMeasure measure) {
        super(measure);
        this.bm = new BloomFilter(2_000_000);
    }

    @Override
    protected void process(final DebsRecord record) {
        this.bm.add(record.toString());
    }

    public boolean exists(final DebsRecord record) {
        return this.bm.contains(record.toString());
    }
}