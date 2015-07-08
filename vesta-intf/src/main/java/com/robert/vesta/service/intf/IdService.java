package com.robert.vesta.service.intf;

import java.util.Date;

import com.robert.vesta.service.bean.Id;

public interface IdService {

	public long genId();

	public Id expId(long id);

	public long makeId(long time, long seq);

	public long makeId(long machine, long time, long seq);

	public long makeId(long genMethod, long machine, long time, long seq);

	public long makeId(long type, long genMethod, long machine, long time,
			long seq);

	public long makeId(long version, long type, long genMethod, long machine,
			long time, long seq);

	public Date transTime(long time);
}
