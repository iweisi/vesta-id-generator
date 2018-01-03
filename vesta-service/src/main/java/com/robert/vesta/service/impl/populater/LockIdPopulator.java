package com.robert.vesta.service.impl.populater;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.util.TimeUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockIdPopulator implements IdPopulator {

    private long sequence = 0;

    private long lastTimestamp = -1;

    private Lock lock = new ReentrantLock();

    public LockIdPopulator() {
        super();
    }

    public void populateId(Id id, IdMeta idMeta) {
        lock.lock();
        try {
            long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(lastTimestamp, timestamp);

            if (timestamp == lastTimestamp) {
                sequence++;
                sequence &= idMeta.getSeqBitsMask();
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                lastTimestamp = timestamp;
                sequence = 0;
            }

            id.setSeq(sequence);
            id.setTime(timestamp);

        } finally {
            lock.unlock();
        }
    }



}
