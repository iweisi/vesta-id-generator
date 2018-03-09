package com.robert.vesta.service.impl.populater;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.util.TimeUtils;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicIdPopulator implements IdPopulator, ResetPopulator {

    class Variant {

        private long sequence = 0;
        private long lastTimestamp = -1;

    }

    private AtomicReference<Variant> variant = new AtomicReference<Variant>(new Variant());

    public AtomicIdPopulator() {
        super();
    }

    public void populateId(Id id, IdMeta idMeta) {
        Variant varOld, varNew;
        long timestamp, sequence;

        while (true) {

            // Save the old variant
            varOld = variant.get();

            // populate the current variant
            timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(varOld.lastTimestamp, timestamp);

            sequence = varOld.sequence;

            if (timestamp == varOld.lastTimestamp) {
                sequence++;
                sequence &= idMeta.getSeqBitsMask();
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(varOld.lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                sequence = 0;
            }

            // Assign the current variant by the atomic tools
            varNew = new Variant();
            varNew.sequence = sequence;
            varNew.lastTimestamp = timestamp;

            if (variant.compareAndSet(varOld, varNew)) {
                id.setSeq(sequence);
                id.setTime(timestamp);

                break;
            }

        }
    }

    public void reset() {
        variant = new AtomicReference<Variant>(new Variant());
    }

}
