package com.robert.vesta.service.impl.populater;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;

public interface IdPopulator {

    void populateId(Id id, IdMeta idMeta);

}
