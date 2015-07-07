package com.wanda.vesta.service.impl;

import com.wanda.vesta.service.bean.Id;

public interface IdConverter {

	public long convert(Id id);

	public Id convert(long id);

}
