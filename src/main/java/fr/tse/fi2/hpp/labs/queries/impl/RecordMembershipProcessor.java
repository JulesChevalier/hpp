package fr.tse.fi2.hpp.labs.queries.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RecordMembershipProcessor extends AbstractQueryProcessor {

    private final List<DebsRecord> records;

    public RecordMembershipProcessor(final QueryProcessorMeasure measure) {
        super(measure);
        this.records = new ArrayList<DebsRecord>();
    }

    @Override
    protected void process(final DebsRecord record) {
        this.records.add(record);
    }

    public boolean exists(final DebsRecord record) {
        return this.records.contains(record);
    }

    public DebsRecord getRandomRecord() {
        final Random rnd = new Random();
        if (rnd.nextBoolean()) {
            return new DebsRecord("", "", 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, false);
        }
        return this.records.get(rnd.nextInt(this.records.size()));
    }
}