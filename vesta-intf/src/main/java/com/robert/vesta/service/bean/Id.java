package com.robert.vesta.service.bean;

import java.io.Serializable;

public class Id implements Serializable {

    private static final long serialVersionUID = 6870931236218221183L;

    private long machine;
    private long seq;
    private long time;
    private long genMethod;
    private long type;
    private long version;

    public Id(long machine, long seq, long time, long genMethod, long type,
              long version) {
        super();
        this.machine = machine;
        this.seq = seq;
        this.time = time;
        this.genMethod = genMethod;
        this.type = type;
        this.version = version;
    }

    public Id() {
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMachine() {
        return machine;
    }

    public void setMachine(long machine) {
        this.machine = machine;
    }

    public long getGenMethod() {
        return genMethod;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append("machine=").append(machine).append(",");
        sb.append("seq=").append(seq).append(",");
        sb.append("time=").append(time).append(",");
        sb.append("genMethod=").append(genMethod).append(",");
        sb.append("type=").append(type).append(",");
        sb.append("version=").append(version).append("]");

        return sb.toString();
    }

}
