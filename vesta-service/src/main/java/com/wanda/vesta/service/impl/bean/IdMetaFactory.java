package com.wanda.vesta.service.impl.bean;

public class IdMetaFactory {
	private static IdMeta maxPeak = new IdMeta((byte) 20, (byte) 30, (byte) 10,
			(byte) 2, (byte) 1, (byte) 1);

	private static IdMeta minGranularity = new IdMeta((byte) 10, (byte) 40,
			(byte) 10, (byte) 2, (byte) 1, (byte) 1);

	public static IdMeta getIdMeta(IdType type) {
		if (IdType.MAX_PEAK.equals(type)) {
			return maxPeak;
		} else if (IdType.MIN_GRANULARITY.equals(type)) {
			return minGranularity;
		}
		return null;
	}
}
