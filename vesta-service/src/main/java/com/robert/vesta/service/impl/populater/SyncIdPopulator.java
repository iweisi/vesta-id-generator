package com.robert.vesta.service.impl.populater;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;

public class SyncIdPopulator extends BasePopulator {

    public SyncIdPopulator() {
        super();
    }

    public synchronized void populateId(Id id, IdMeta idMeta) {
        super.populateId(id, idMeta);
    }

}
