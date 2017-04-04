package com.robert.vesta.service.impl.bean;

public class IdMeta {
	private byte seqBits;
	private byte timeBits;
	private byte machineBits;
	private byte genMethodBits;
	private byte typeBits;
	private byte versionBits;

	public IdMeta(byte seqBits, byte timeBits, byte machineBits,
			byte genMethodBits, byte typeBits, byte versionBits) {
		super();
		this.seqBits = seqBits;
		this.timeBits = timeBits;
		this.machineBits = machineBits;
		this.genMethodBits = genMethodBits;
		this.typeBits = typeBits;
		this.versionBits = versionBits;
	}

	public byte getSeqBits() {
		return seqBits;
	}

	public void setSeqBits(byte seqBits) {
		this.seqBits = seqBits;
	}

	public long getSeqBitsMask() {
		return -1L ^ -1L << seqBits;
	}

	public byte getTimeBits() {
		return timeBits;
	}

	public void setTimeBits(byte timeBits) {
		this.timeBits = timeBits;
	}

	public long getTimeBitsStartPos() {
		return seqBits;
	}

	public long getTimeBitsMask() {
		return -1L ^ -1L << timeBits;
	}

	public byte getMachineBits() {
		return machineBits;
	}

	public void setMachineBits(byte machineBits) {
		this.machineBits = machineBits;
	}

	public long getMachineBitsStartPos() {
		return seqBits + timeBits;
	}

	public long getMachineBitsMask() {
		return -1L ^ -1L << machineBits;
	}

	public byte getGenMethodBits() {
		return genMethodBits;
	}

	public void setGenMethodBits(byte genMethodBits) {
		this.genMethodBits = genMethodBits;
	}

	public long getGenMethodBitsStartPos() {
		return seqBits + timeBits + machineBits;
	}

	public long getGenMethodBitsMask() {
		return -1L ^ -1L << genMethodBits;
	}

	public byte getTypeBits() {
		return typeBits;
	}

	public void setTypeBits(byte typeBits) {
		this.typeBits = typeBits;
	}

	public long getTypeBitsStartPos() {
		return seqBits + timeBits + machineBits + genMethodBits;
	}

	public long getTypeBitsMask() {
		return -1L ^ -1L << typeBits;
	}

	public byte getVersionBits() {
		return versionBits;
	}

	public void setVersionBits(byte versionBits) {
		this.versionBits = versionBits;
	}

	public long getVersionBitsStartPos() {
		return seqBits + timeBits + machineBits + genMethodBits + typeBits;
	}

	public long getVersionBitsMask() {
		return -1L ^ -1L << versionBits;
	}
}
