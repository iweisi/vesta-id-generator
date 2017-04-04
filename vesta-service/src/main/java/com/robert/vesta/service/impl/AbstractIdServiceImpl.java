package com.robert.vesta.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;
import com.robert.vesta.service.impl.bean.IdMetaFactory;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.service.impl.converter.IdConverter;
import com.robert.vesta.service.impl.converter.IdConverterImpl;
import com.robert.vesta.service.impl.provider.MachineIdProvider;
import com.robert.vesta.service.intf.IdService;

public abstract class AbstractIdServiceImpl implements IdService {
	public static final long EPOCH = 1420041600000L;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected long machineId = -1;
	protected long genMethod = 0;
	protected long type = 0;
	protected long version = 0;

	protected IdType idType;
	protected IdMeta idMeta;

	protected IdConverter idConverter;

	protected MachineIdProvider machineIdProvider;

	public AbstractIdServiceImpl() {
		idType = IdType.MAX_PEAK;
	}

	public AbstractIdServiceImpl(String type) {
		idType = IdType.parse(type);
	}

	public AbstractIdServiceImpl(IdType type) {
		idType = type;
	}

	public void init() {
		this.machineId = machineIdProvider.getMachineId();

		if (machineId < 0) {
			log.error("The machine ID is not configured properly so that Vesta Service refuses to start.");

			throw new IllegalStateException(
					"The machine ID is not configured properly so that Vesta Service refuses to start.");

		}

		setIdMeta(IdMetaFactory.getIdMeta(idType));
		setType(idType.value());
		setIdConverter(new IdConverterImpl(idType));
	}

	public long genId() {
		Id id = new Id();

		populateId(id);

		id.setMachine(machineId);
		id.setGenMethod(genMethod);
		id.setType(type);
		id.setVersion(version);

		long ret = idConverter.convert(id);

		// Use trace because it cause low performance
		if (log.isTraceEnabled())
			log.trace(String.format("Id: %s => %d", id, ret));

		return ret;
	}

	protected abstract void populateId(Id id);

	protected long genTime() {
		if (idType == IdType.MAX_PEAK)
			return (System.currentTimeMillis() - EPOCH) / 1000;
		else if (idType == IdType.MIN_GRANULARITY)
			return (System.currentTimeMillis() - EPOCH);

		return (System.currentTimeMillis() - EPOCH) / 1000;
	}

	public Id expId(long id) {
		return idConverter.convert(id);
	}

	public long makeId(long time, long seq) {
		return makeId(time, seq, machineId);
	}

	public long makeId(long time, long seq, long machine) {
		return makeId(genMethod, time, seq, machine);
	}

	public long makeId(long genMethod, long time, long seq, long machine) {
		return makeId(type, genMethod, time, seq, machine);
	}

	public long makeId(long type, long genMethod,  long time,
			long seq, long machine) {
		return makeId(version, type, genMethod, time, seq, machine);
	}

	public long makeId(long version, long type, long genMethod, 
			long time, long seq, long machine) {
		IdType idType = IdType.parse(type);

		Id id = new Id(machine, seq, time, genMethod, type, version);
		IdConverter idConverter = new IdConverterImpl(idType);

		return idConverter.convert(id);
	}

	public Date transTime(long time) {
		if (idType == IdType.MAX_PEAK) {
			return new Date(time * 1000 + EPOCH);
		} else if (idType == IdType.MIN_GRANULARITY) {
			return new Date(time + EPOCH);
		}

		return null;
	}

	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}

	public void setGenMethod(long genMethod) {
		this.genMethod = genMethod;
	}

	public void setType(long type) {
		this.type = type;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public void setIdConverter(IdConverter idConverter) {
		this.idConverter = idConverter;
	}

	public void setIdMeta(IdMeta idMeta) {
		this.idMeta = idMeta;
	}

	public void setMachineIdProvider(MachineIdProvider machineIdProvider) {
		this.machineIdProvider = machineIdProvider;
	}
}