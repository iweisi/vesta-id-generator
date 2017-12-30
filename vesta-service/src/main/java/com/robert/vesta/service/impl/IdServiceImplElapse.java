package com.robert.vesta.service.impl;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.service.intf.IdService;

public abstract class IdServiceImplElapse extends AbstractIdServiceImpl implements IdService {

    public IdServiceImplElapse() {
        super();
    }

    public IdServiceImplElapse(String type) {
        super(type);
    }

    public IdServiceImplElapse(IdType type) {
        super(type);
    }

    protected abstract void populateId(Id id);

    protected void validateTimestamp(long lastTimestamp, long timestamp) {
        if (timestamp < lastTimestamp) {
            if (log.isErrorEnabled())
                log.error(String
                        .format("Clock moved backwards.  Refusing to generate id for %d %s.",
                                lastTimestamp - timestamp,
                                idType == IdType.MAX_PEAK ? "second"
                                        : "milisecond"));

            throw new IllegalStateException(
                    String.format(
                            "Clock moved backwards.  Refusing to generate id for %d %s.",
                            lastTimestamp - timestamp,
                            idType == IdType.MAX_PEAK ? "second" : "milisecond"));
        }
    }

    protected long tillNextTimeUnit(final long lastTimestamp) {
        if (log.isInfoEnabled())
            log.info(String
                    .format("Ids are used out during %d in machine %d. Waiting till next %s.",
                            lastTimestamp, machineId,
                            idType == IdType.MAX_PEAK ? "second" : "milisecond"));

        long timestamp = this.genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = this.genTime();
        }

        if (log.isInfoEnabled())
            log.info(String.format("Next %s %d is up.",
                    idType == IdType.MAX_PEAK ? "second" : "milisecond",
                    timestamp));

        return timestamp;
    }
}
