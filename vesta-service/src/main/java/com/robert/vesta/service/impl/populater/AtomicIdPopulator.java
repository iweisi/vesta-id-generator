package com.robert.vesta.service.impl.populater;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.util.TimeUtils;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicIdPopulator implements IdPopulator {

    class Variant {
        private long sequence = 0;

        private long lastTimestamp = -1;

        @Override
        public boolean equals(Object obj) {

            if (obj == null)
                return false;

            Variant it = null;

            if (obj instanceof Variant) {
                it = (Variant) obj;
            }

            if (it.sequence != this.sequence || it.lastTimestamp != this.lastTimestamp)
                return false;

            return true;
        }
    }

    private AtomicReference<Variant> variant = new AtomicReference<Variant>(new Variant());

    public AtomicIdPopulator() {
        super();
    }

    public void populateId(Id id, IdMeta idMeta) {
        while (true) {
            // Save the old variant
            Variant varOld = new Variant();
            varOld.sequence = variant.get().sequence;
            varOld.lastTimestamp = variant.get().lastTimestamp;


            // populate the current variant
            long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(varOld.lastTimestamp, timestamp);

            long sequence = variant.get().sequence;

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
            Variant varNew = new Variant();
            varNew.sequence = sequence;
            varNew.lastTimestamp = timestamp;

            if (variant.compareAndSet(varOld, varNew)) {
                id.setSeq(sequence);
                id.setTime(timestamp);
                break;
            }
        }
    }
}
