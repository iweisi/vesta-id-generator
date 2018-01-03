package com.robert.vesta.service.impl;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.service.impl.populater.IdPopulator;
import com.robert.vesta.service.impl.populater.LockIdPopulator;
import com.robert.vesta.util.TimeUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IdServiceImpl extends AbstractIdServiceImpl {

    private IdPopulator idPopulator;

    public IdServiceImpl() {
        super();

        idPopulator = new LockIdPopulator();
    }

    public IdServiceImpl(String type)
    {
        super(type);

        idPopulator = new LockIdPopulator();
    }

    public IdServiceImpl(IdType type) {
        super(type);

        idPopulator = new LockIdPopulator();
    }

    protected void populateId(Id id) {
        idPopulator.populateId(id, this.idMeta);
    }
}
