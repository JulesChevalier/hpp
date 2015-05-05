package fr.tse.fi2.hpp.labs.queries.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor {

    List<DebsRecord> records;

    public RouteMembershipProcessor(final QueryProcessorMeasure measure) {
        super(measure);
        this.records = new ArrayList<DebsRecord>();
    }

    @Override
    protected void process(final DebsRecord record) {
        this.records.add(record);
    }

    public boolean existsPickupPoint(final float longitude, final float latitude) {
        final Iterator<DebsRecord> it = this.records.iterator();

        while (it.hasNext()) {
            final DebsRecord record = it.next();
            if (record.getPickup_longitude() == longitude && record.getPickup_latitude() == latitude) {
                return true;
            }
        }
        return false;
    }

    public boolean existsDropoffPoint(final float longitude, final float latitude) {
        final Iterator<DebsRecord> it = this.records.iterator();

        while (it.hasNext()) {
            final DebsRecord record = it.next();
            if (record.getDropoff_longitude() == longitude && record.getDropoff_latitude() == latitude) {
                return true;
            }
        }
        return false;
    }

    public boolean existsTaxiLicense(final String license) {
        final Iterator<DebsRecord> it = this.records.iterator();

        while (it.hasNext()) {
            final DebsRecord record = it.next();
            if (record.getHack_license().equals(license)) {
                return true;
            }
        }
        return false;
    }

    public DebsRecord getRandomRecord() {
        final Random rnd = new Random();
        if (rnd.nextBoolean()) {
            return new DebsRecord("", "", 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, false);
        }
        return this.records.get(rnd.nextInt(this.records.size()));
    }
}