package com.robert.vesta.service.impl.converter;

import com.robert.vesta.service.bean.Id;

public interface IdConverter {

    public long convert(Id id);

    public Id convert(long id);

}
