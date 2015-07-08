package com.robert.vesta.service.impl;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.bean.IdMeta;
import com.robert.vesta.service.impl.bean.IdMetaFactory;
import com.robert.vesta.service.impl.bean.IdType;

public class IdConverterImpl implements IdConverter {
	private IdType idType;

	public IdConverterImpl() {
	}

	public IdConverterImpl(IdType idType) {
		this.idType = idType;
	}

	public long convert(Id id) {
		return doConvert(id, IdMetaFactory.getIdMeta(idType));
	}

	protected long doConvert(Id id, IdMeta idMeta) {
		long ret = 0;
		
		ret |= id.getSeq();

		ret |= id.getTime() << idMeta.getTimeBitsStartPos();
		
		ret |= id.getMachine() << idMeta.getMachineBitsStartPos();

		ret |= id.getGenMethod() << idMeta.getGenMethodBitsStartPos();

		ret |= id.getType() << idMeta.getTypeBitsStartPos();

		ret |= id.getVersion() << idMeta.getVersionBitsStartPos();

		return ret;
	}

	public Id convert(long id) {
		return doConvert(id, IdMetaFactory.getIdMeta(idType));
	}

	protected Id doConvert(long id, IdMeta idMeta) {
		Id ret = new Id();

		ret.setSeq(id & idMeta.getSeqBitsMask());

		ret.setTime((id >>> idMeta.getTimeBitsStartPos())
				& idMeta.getTimeBitsMask());
		
		ret.setMachine((id >>> idMeta.getMachineBitsStartPos())
				& idMeta.getMachineBitsMask());		

		ret.setGenMethod((id >>> idMeta.getGenMethodBitsStartPos())
				& idMeta.getGenMethodBitsMask());

		ret.setType((id >>> idMeta.getTypeBitsStartPos())
				& idMeta.getTypeBitsMask());

		ret.setVersion((id >>> idMeta.getVersionBitsStartPos())
				& idMeta.getVersionBitsMask());

		return ret;
	}

	public IdType getIdType() {
		return idType;
	}

	public void setIdType(IdType idType) {
		this.idType = idType;
	}
}
