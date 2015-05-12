package fr.tse.fi2.hpp.labs.queries.impl;

import java.util.ArrayList;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.utils.BloomFilter;

public class RecordMixMembershipProcessor extends AbstractQueryProcessor {

    private final List<DebsRecord> records;
    private final BloomFilter bm;

    public RecordMixMembershipProcessor(final QueryProcessorMeasure measure) {
        super(measure);
        this.records = new ArrayList<DebsRecord>();
        this.bm = new BloomFilter(2_000_000);
    }

    @Override
    protected void process(final DebsRecord record) {
        this.records.add(record);
        this.bm.add(record.toString());
    }

    public boolean exists(final DebsRecord record) {
        if (!this.bm.contains(record.toString())) {
            return false;
        }
        return this.records.contains(record);
    }
}