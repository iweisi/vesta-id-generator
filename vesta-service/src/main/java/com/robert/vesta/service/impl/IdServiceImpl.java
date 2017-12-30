package com.robert.vesta.service.impl;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdType;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IdServiceImpl extends IdServiceImplElapse {

    private long sequence = 0;
    private long lastTimestamp = -1;

    private Lock lock = new ReentrantLock();

    public IdServiceImpl() {
        super();
    }

    public IdServiceImpl(String type) {
        super(type);
    }

    public IdServiceImpl(IdType type) {
        super(type);
    }

    protected void populateId(Id id) {
        lock.lock();
        try {
            long timestamp = this.genTime();
            validateTimestamp(lastTimestamp, timestamp);

            if (timestamp == lastTimestamp) {
                sequence++;
                sequence &= idMeta.getSeqBitsMask();
                if (sequence == 0) {
                    timestamp = this.tillNextTimeUnit(lastTimestamp);
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
